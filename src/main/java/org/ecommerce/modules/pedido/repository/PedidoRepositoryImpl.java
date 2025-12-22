package org.ecommerce.modules.pedido.repository;

import org.ecommerce.modules.pedido.ItemPedido;
import org.ecommerce.modules.pedido.Pedido;
import org.ecommerce.modules.produto.Produto;
import org.ecommerce.modules.produto.repository.ProdutoRepositoryImpl;
import org.ecommerce.modules.usuario.cliente.Cliente;
import org.ecommerce.modules.usuario.cliente.repository.ClienteRepositoryImpl;
import org.ecommerce.utils.DB;
import org.ecommerce.utils.SQLiteConnectionManager;

import java.sql.*;
import java.util.List;

public class PedidoRepositoryImpl implements PedidoRepository{

    private final ProdutoRepositoryImpl produtoRepo = new ProdutoRepositoryImpl();
    private final ClienteRepositoryImpl clienteRepo = new ClienteRepositoryImpl();

    public void salvarPedido(Pedido pedido) {
        Connection conn = SQLiteConnectionManager.getConnection();
        PreparedStatement psPedido = null;
        PreparedStatement psItem = null;

        try {
            // 1. Iniciar Transação
            conn.setAutoCommit(false);

            // 2. Inserir Pedido
            String sqlPedido = "INSERT INTO pedido " +
                    "(cliente_id, data_pedido, total, status, metodo_pagamento, endereco_entrega) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setLong(1, pedido.getCliente().getId());
            psPedido.setString(2, pedido.getDataPedido().toString());
            psPedido.setDouble(3, pedido.getTotal());
            psPedido.setString(4, pedido.getStatus());
            psPedido.setString(5, pedido.getMetodoPagamento());
            psPedido.setString(6, pedido.getEnderecoEntrega());
            psPedido.executeUpdate();

            // Pegar ID gerado
            try (ResultSet rs = psPedido.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setId(rs.getLong(1));
                }
            }

            // 3. Inserir Itens
            String sqlItem = "INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
            psItem = conn.prepareStatement(sqlItem);

            for (ItemPedido item : pedido.getItens()) {
                psItem.setLong(1, pedido.getId());
                psItem.setLong(2, item.getProduto().getId());
                psItem.setInt(3, item.getQuantidade());
                psItem.setDouble(4, item.getPrecoUnitario());
                psItem.addBatch(); // Adiciona ao lote
            }
            psItem.executeBatch(); // Executa todos

            // 4. Confirmar Transação
            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback(); // Desfaz se der erro
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erro ao salvar pedido: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
                if (psPedido != null) psPedido.close();
                if (psItem != null) psItem.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Pedido> buscarPedidosPorCliente(long clienteId) {
        String sql = "SELECT * FROM pedido WHERE cliente_id = ? ORDER BY id DESC";

        // Usamos o DB helper aqui pois é apenas leitura
        return DB.query(sql, rs -> {
            try {
                long idPedido = rs.getLong("id");
                Cliente cliente = clienteRepo.buscarClientePorId(rs.getLong("cliente_id"));

                Pedido p = new Pedido(
                        idPedido,
                        cliente,
                        rs.getString("data_pedido"),
                        rs.getDouble("total"),
                        rs.getString("status"),
                        rs.getString("metodo_pagamento"),
                        rs.getString("endereco_entrega")
                );

                // Carregar itens deste pedido
                carregarItens(p);
                return p;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, clienteId);
    }

    private void carregarItens(Pedido pedido) {
        String sql = "SELECT * FROM item_pedido WHERE pedido_id = ?";

        List<ItemPedido> itens = DB.query(sql, rs -> {
            try {
                Produto prod = produtoRepo.buscarProdutoPorId(rs.getLong("produto_id"));
                return new ItemPedido(
                        rs.getLong("id"),
                        prod,
                        rs.getInt("quantidade"),
                        rs.getDouble("preco_unitario")
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, pedido.getId());

        itens.forEach(pedido::adicionarItem);
    }
}