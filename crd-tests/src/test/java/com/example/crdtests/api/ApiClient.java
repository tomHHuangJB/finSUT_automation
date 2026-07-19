package com.example.crdtests.api;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class ApiClient {

    private final String backendBaseUrl;

    public ApiClient() {
        this.backendBaseUrl = System.getProperty("backend.base.url", "http://localhost:8080");
    }

    public Response listAccounts() {
        return given()
                .auth().preemptive().basic("pm_user", "password")
                .baseUri(backendBaseUrl)
                .when()
                .get("/api/v1/accounts");
    }

    public Response listSecurities() {
        return given()
                .auth().preemptive().basic("pm_user", "password")
                .baseUri(backendBaseUrl)
                .when()
                .get("/api/v1/securities");
    }

    public Response createMarketOrder(String externalOrderId, UUID accountId, UUID securityId, BigDecimal quantity) {
        Map<String, Object> payload = Map.of(
                "externalOrderId", externalOrderId,
                "accountId", accountId.toString(),
                "securityId", securityId.toString(),
                "side", "BUY",
                "orderType", "MARKET",
                "quantity", quantity);

        return given()
                .auth().preemptive().basic("pm_user", "password")
                .baseUri(backendBaseUrl)
                .contentType(ContentType.JSON)
                .header("X-Correlation-Id", externalOrderId)
                .body(payload)
                .when()
                .post("/api/v1/orders");
    }
}
