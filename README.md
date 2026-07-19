# CRD Practice SUT

This repository contains a practice commission/order-management style SUT and a separate automation project. The current working slice supports creating an order, persisting it to PostgreSQL, showing it in a React UI, and verifying it through API, SQL, and Playwright automation.

## Repository Layout

```text
crd-sut/
  backend/      Spring Boot REST API
  frontend/     React/Vite UI
  database/     PostgreSQL init scripts
  docker-compose.yml

crd-tests/      JUnit automation project
codeit.md       Phase 1 build/run guide
codeitPhase2.md Phase 2 implementation plan
```

## Tech Stack

SUT:

- Backend: Java 21, Spring Boot 4.1, Spring Web MVC, Spring Security, Spring Data JPA, Flyway
- Database: PostgreSQL 16 in Docker
- Frontend: React 19, TypeScript, Vite, Material UI, React Router, TanStack Query, React Hook Form, Axios
- Auth: HTTP Basic Auth with seeded practice users

Automation:

- Test runner: JUnit 5
- API testing: REST Assured
- UI testing: Playwright Java
- Database validation: JDBC with PostgreSQL driver
- Assertions: AssertJ
- Build: Maven Surefire

## Prerequisites

- Java 21
- Maven
- Node.js 20 or newer
- npm
- Docker with Docker Compose v2

Verify tools:

```bash
java -version
mvn -version
node -v
npm -v
docker --version
docker compose version
```

## Start the SUT

Run each service in its own terminal.

### 1. Start PostgreSQL

```bash
cd /Users/tomhuang/prog/crd/crd-sut
docker compose up -d postgres
docker compose ps
```

Expected: `crd-sut-postgres` is `healthy`.

### 2. Start the Backend

```bash
cd /Users/tomhuang/prog/crd/crd-sut/backend
mvn spring-boot:run
```

Expected: logs include `Tomcat started on port 8080`.

Verify from another terminal:

```bash
curl -i http://localhost:8080/api/v1/health
curl -i -u pm_user:password http://localhost:8080/api/v1/orders
```

### 3. Start the Frontend

Install dependencies first if `node_modules` is not present:

```bash
cd /Users/tomhuang/prog/crd/crd-sut/frontend
npm install
```

Start Vite:

```bash
npm run dev
```

Open:

```text
http://localhost:5173
```

The Vite dev server proxies `/api` requests to the backend at `http://localhost:8080`.

## Practice Login/User

The frontend API client uses:

```text
username: pm_user
password: password
```

Other seeded users exist in the database for later role-based work, but the current UI slice uses `pm_user`.

## Manual Smoke Test

With PostgreSQL, backend, and frontend running:

1. Open `http://localhost:5173`.
2. Confirm the Orders page loads.
3. Go to Create Order.
4. Use:

```text
Account: ACC-1001 - Core Growth Account
Security: AAPL - Apple Inc.
Side: BUY
Order Type: MARKET
Quantity: 100
Limit Price: blank/disabled
```

5. Keep the prefilled `UI-ORDER-*` external order ID.
6. Click Create Order.
7. Return to Orders and confirm the new order appears with status `DRAFT`.

## Run Builds

Backend:

```bash
cd /Users/tomhuang/prog/crd/crd-sut/backend
mvn test
```

Frontend:

```bash
cd /Users/tomhuang/prog/crd/crd-sut/frontend
npm run build
```

## Run Automation Tests

Before running `crd-tests`, keep these running:

- PostgreSQL Docker service
- Backend on `http://localhost:8080`
- Frontend on `http://localhost:5173`

Then run:

```bash
cd /Users/tomhuang/prog/crd/crd-tests
mvn test
```

Current automation coverage:

- REST Assured creates a unique order through the backend API.
- JDBC verifies the order row in PostgreSQL.
- JDBC verifies the `ORDER_CREATED` audit event.
- Playwright opens the React Orders page and verifies the order is visible.
- Test cleanup removes the created order.

If Playwright browser binaries are missing:

```bash
cd /Users/tomhuang/prog/crd/crd-tests
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.classpathScope=test -Dexec.args="install chromium"
```

## GitHub CI

GitHub Actions workflow:

```text
.github/workflows/ci.yml
```

The workflow runs on pull requests and pushes to `main` or `master`.

Jobs:

- `build`: starts a PostgreSQL service container, runs backend `mvn test`, installs frontend dependencies with `npm ci`, and runs `npm run build`.
- `integration`: starts a PostgreSQL service container, installs Playwright Chromium, starts the backend and frontend, waits for health/proxy checks, then runs `crd-tests` with Maven.

On integration runs, the workflow uploads backend logs, frontend logs, and Surefire reports as artifacts, even when the test job fails.

## Useful API Checks

```bash
curl -i http://localhost:8080/api/v1/health
curl -i -u pm_user:password http://localhost:8080/api/v1/accounts
curl -i -u pm_user:password http://localhost:8080/api/v1/securities
curl -i -u pm_user:password http://localhost:8080/api/v1/orders
```

Verify the frontend proxy:

```bash
curl -i -u pm_user:password http://localhost:5173/api/v1/orders
```

## Database Access

```bash
docker compose -f /Users/tomhuang/prog/crd/crd-sut/docker-compose.yml exec postgres psql -U crd_app -d crd_sut
```

Example query:

```sql
select external_order_id, side, order_type, quantity, status, created_by
from orders
order by created_at desc;
```

## Reset Local Database

Stop containers:

```bash
cd /Users/tomhuang/prog/crd/crd-sut
docker compose down
```

Delete the PostgreSQL volume and all local SUT data:

```bash
docker compose down -v
```

Use `down -v` only when you intentionally want a clean database.

## Troubleshooting

If the UI shows `Could not load orders`, check the backend first:

```bash
curl -i -u pm_user:password http://localhost:8080/api/v1/orders
```

If that cannot connect, start the backend with `mvn spring-boot:run`. `docker ps` only proves PostgreSQL is running; the backend is a local Java process, not a Docker container.

If the frontend proxy returns `502 Bad Gateway`, the backend is not reachable from Vite.

If the API returns `401`, confirm the request uses Basic Auth:

```bash
-u pm_user:password
```

If Maven or npm downloads fail, confirm network access and rerun the relevant install/build command.

## Current Scope and Next Phase

The current SUT is Phase 1: create/list orders with a basic UI and one integrated automation test.

The broader plan in `cdr-practice-plan.md` also calls for compliance review, approval/release, executions, positions, cash updates, weighted average price, and expanded API/UI/SQL test coverage. Those next steps are mapped in `codeitPhase2.md`.
