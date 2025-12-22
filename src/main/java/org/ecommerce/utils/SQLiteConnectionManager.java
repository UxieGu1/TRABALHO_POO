package org.ecommerce.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class SQLiteConnectionManager {

    private static final String DB_URL = "jdbc:sqlite:db/ecommerce.db";

    private static Connection connection;

    private SQLiteConnectionManager() {
        throw new UnsupportedOperationException("Classe utilitária");
    }

    public static synchronized Connection getConnection() {
        try {
            if (connection == null || !isConnectionValid(connection)) {
                Path dbPath = Paths.get("db");
                if (!Files.exists(dbPath)) {
                    Files.createDirectories(dbPath);
                }
                
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ignored) {
                        //Ignora
                    }
                }
                
                connection = DriverManager.getConnection(DB_URL);
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter conexão SQLite", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar diretório do banco de dados", e);
        }
    }

    private static boolean isConnectionValid(Connection conn) {
        try {
            return conn != null && !conn.isClosed() && conn.isValid(1);
        } catch (SQLException e) {
            return false;
        }
    }

    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar conexão SQLite", e);
            }
        }
    }
}
