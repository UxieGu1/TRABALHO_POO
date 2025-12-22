package org.ecommerce.modules.usuario.cliente.repository;

import org.ecommerce.modules.usuario.cliente.Cliente;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ClienteMapper {

    private ClienteMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Cliente map(ResultSet rs) {
        try {
            return new Cliente(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("endereco"),
                    rs.getString("telefone")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao mapear o cliente", e);
        }
    }
}
