package org.ecommerce.modules.carrinho.repository;

import org.ecommerce.modules.carrinho.Carrinho;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class CarrinhoMapper {

    private CarrinhoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Carrinho map(ResultSet rs) {
        try {
            long id = rs.getLong("id");
            long clienteId = rs.getLong("clienteId");

            return new Carrinho(id, clienteId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao mapear o carrinho", e);
        }
    }
}
