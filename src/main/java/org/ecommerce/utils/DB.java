package org.ecommerce.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class DB {

    private DB() {}

    public static int execute(String sql, Object... params) {
        try (PreparedStatement stmt =
                     SQLiteConnectionManager.getConnection().prepareStatement(sql)) {

            bindParams(stmt, params);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static long executeAndReturnId(String sql, Object... params) {
        try (PreparedStatement stmt = SQLiteConnectionManager.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            bindParams(stmt, params);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new RuntimeException("Nenhum ID gerado");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> query(
            String sql,
            Function<ResultSet, T> mapper,
            Object... params
    ) {
        try (PreparedStatement stmt =
                     SQLiteConnectionManager.getConnection().prepareStatement(sql)) {

            bindParams(stmt, params);

            try (ResultSet rs = stmt.executeQuery()) {
                List<T> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapper.apply(rs));
                }
                return result;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void bindParams(PreparedStatement stmt, Object... params)
            throws SQLException {

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }
}
