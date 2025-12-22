package org.ecommerce.modules.pedido.repository;

import org.ecommerce.modules.pedido.ItemPedido;
import org.ecommerce.modules.pedido.Pedido;
import org.ecommerce.modules.produto.Produto;
import org.ecommerce.modules.produto.repository.ProdutoRepositoryImpl;
import org.ecommerce.modules.usuario.cliente.Cliente;
import org.ecommerce.modules.usuario.cliente.repository.ClienteRepositoryImpl;
import org.ecommerce.utils.DB;

import java.sql.SQLException;
import java.util.List;

public class PedidoRepositoryImpl implements PedidoRepository {

    private final ProdutoRepositoryImpl produtoRepo = new ProdutoRepositoryImpl();
    private final ClienteRepositoryImpl clienteRepo = new ClienteRepositoryImpl();

    @Override
    public void salvarPedido(Pedido pedido) {
        // 1. Salva o cabeçalho do pedido e recupera o ID gerado
        String sqlPedido = """
                INSERT INTO pedido (cliente_id, data_pedido, total, status, metodo_pagamento, endereco_entrega)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        long idGerado = DB.executeAndReturnId(sqlPedido,
                pedido.getCliente().getId(),
                pedido.getDataPedido().toString(),
                pedido.getTotal(),
                pedido.getStatus(),
                pedido.getMetodoPagamento(),
                pedido.getEnderecoEntrega()
        );

        pedido.setId(idGerado);

        // 2. Salva os itens do pedido (Um insert por item)
        String sqlItem = """
                INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario)
                VALUES (?, ?, ?, ?)
                """;

        for (ItemPedido item : pedido.getItens()) {
            DB.execute(sqlItem,
                    pedido.getId(),
                    item.getProduto().getId(),
                    item.getQuantidade(),
                    item.getPrecoUnitario()
            );
        }
    }

    @Override
    public List<Pedido> buscarPedidosPorCliente(long clienteId) {
        String sql = "SELECT * FROM pedido WHERE cliente_id = ? ORDER BY id DESC";

        return DB.query(sql, rs -> {
            try {
                // Mapeamento manual (ou via Mapper)
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

                // Carrega os itens deste pedido específico
                carregarItens(p);
                return p;
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao mapear pedido", e);
            }
        }, clienteId);
    }

    private void carregarItens(Pedido pedido) {
        String sql = "SELECT * FROM item_pedido WHERE pedido_id = ?";

        List<ItemPedido> itens = DB.query(sql, rs -> {
            try {
                // Busca o produto completo para preencher o item
                Produto prod = produtoRepo.buscarProdutoPorId(rs.getLong("produto_id"));

                return new ItemPedido(
                        rs.getLong("id"),
                        prod,
                        rs.getInt("quantidade"),
                        rs.getDouble("preco_unitario")
                );
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao mapear item do pedido", e);
            }
        }, pedido.getId());

        itens.forEach(pedido::adicionarItem);
    }
}