package org.ecommerce.modules.pedido.repository;

import org.ecommerce.modules.pedido.Pedido;
import org.ecommerce.modules.usuario.cliente.Cliente;
import org.ecommerce.modules.usuario.cliente.repository.ClienteRepositoryImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class PedidoMapper {

    private PedidoMapper(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static Pedido map(ResultSet rs) {
        try {
            ClienteRepositoryImpl clienteRepo = new ClienteRepositoryImpl();
            long clienteId = rs.getLong("cliente_id");
            Cliente cliente = clienteRepo.buscarClientePorId(clienteId);

            return new Pedido(
                    rs.getLong("id"),
                    cliente,
                    rs.getString("data_pedido"),
                    rs.getDouble("total"),
                    rs.getString("status"),
                    rs.getString("metodo_pagamento"),
                    rs.getString("endereco_entrega")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao mapear o pedido", e);
        }
    }
}