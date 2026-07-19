package com.example.crdtests.e2e;

import static com.example.crdtests.db.DatabaseClient.equalMoney;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.crdtests.api.ApiClient;
import com.example.crdtests.db.DatabaseClient;
import com.example.crdtests.model.OrderRow;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class CreateOrderWorkflowTest {

    private final ApiClient apiClient = new ApiClient();
    private final DatabaseClient databaseClient = new DatabaseClient();
    private String createdExternalOrderId;

    @AfterEach
    void cleanUp() throws SQLException {
        if (createdExternalOrderId != null) {
            databaseClient.deleteOrderByExternalId(createdExternalOrderId);
        }
    }

    @Test
    void createOrderByApiVerifyDatabaseAndUi() throws SQLException {
        UUID accountId = UUID.fromString(apiClient.listAccounts()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("find { it.accountCode == 'ACC-1001' }.id"));

        UUID securityId = UUID.fromString(apiClient.listSecurities()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("find { it.symbol == 'AAPL' }.id"));

        createdExternalOrderId = "AUTO-ORDER-" + Instant.now().toEpochMilli();

        Response createResponse = apiClient.createMarketOrder(
                createdExternalOrderId,
                accountId,
                securityId,
                new BigDecimal("100"));

        createResponse.then().statusCode(201);
        assertThat(createResponse.jsonPath().getString("externalOrderId")).isEqualTo(createdExternalOrderId);
        assertThat(createResponse.jsonPath().getString("status")).isEqualTo("DRAFT");

        OrderRow order = databaseClient.findOrderByExternalId(createdExternalOrderId);
        assertThat(order).isNotNull();
        assertThat(order.side()).isEqualTo("BUY");
        assertThat(order.orderType()).isEqualTo("MARKET");
        assertThat(equalMoney(order.quantity(), "100.0000")).isTrue();
        assertThat(equalMoney(order.filledQuantity(), "0.0000")).isTrue();
        assertThat(equalMoney(order.remainingQuantity(), "100.0000")).isTrue();
        assertThat(order.status()).isEqualTo("DRAFT");
        assertThat(order.createdBy()).isEqualTo("pm_user");
        assertThat(databaseClient.countAuditEvents(createdExternalOrderId, "ORDER_CREATED")).isEqualTo(1);

        verifyOrderVisibleInUi(createdExternalOrderId);
    }

    private void verifyOrderVisibleInUi(String externalOrderId) {
        String frontendBaseUrl = System.getProperty("frontend.base.url", "http://localhost:5173");

        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true))) {
            Page page = browser.newPage();
            page.navigate(frontendBaseUrl);
            page.getByText(externalOrderId).waitFor();
            assertThat(page.getByText(externalOrderId).isVisible()).isTrue();
        }
    }
}
