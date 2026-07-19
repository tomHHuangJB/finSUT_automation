package com.example.crdtests.db;

import com.example.crdtests.model.OrderRow;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseClient {

    private final String url = System.getProperty("db.url", "jdbc:postgresql://localhost:5432/crd_sut");
    private final String user = System.getProperty("db.user", "crd_app");
    private final String password = System.getProperty("db.password", "crd_app_password");

    public OrderRow findOrderByExternalId(String externalOrderId) throws SQLException {
        String sql = """
                select external_order_id, side, order_type, quantity, filled_quantity, remaining_quantity, status, created_by
                from orders
                where external_order_id = ?
                """;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, externalOrderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return new OrderRow(
                        resultSet.getString("external_order_id"),
                        resultSet.getString("side"),
                        resultSet.getString("order_type"),
                        resultSet.getBigDecimal("quantity"),
                        resultSet.getBigDecimal("filled_quantity"),
                        resultSet.getBigDecimal("remaining_quantity"),
                        resultSet.getString("status"),
                        resultSet.getString("created_by"));
            }
        }
    }

    public int countAuditEvents(String correlationId, String action) throws SQLException {
        String sql = """
                select count(*)
                from audit_events
                where correlation_id = ?
                  and action = ?
                  and entity_type = 'ORDER'
                """;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, correlationId);
            statement.setString(2, action);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1);
            }
        }
    }

    public void deleteOrderByExternalId(String externalOrderId) throws SQLException {
        String sql = "delete from orders where external_order_id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, externalOrderId);
            statement.executeUpdate();
        }
    }

    public static boolean equalMoney(BigDecimal actual, String expected) {
        return actual.compareTo(new BigDecimal(expected)) == 0;
    }
}
