package org.ecommerce.modules.carrinho.repository;

import org.ecommerce.modules.carrinho.Carrinho;
import org.ecommerce.modules.produto.Produto;
import org.ecommerce.utils.DB;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarrinhoRepositoryImpl implements CarrinhoRepository {

    @Override
    public List<Carrinho> buscarTodosCarrinhos() {
        return DB.query(
                "SELECT * FROM carrinho",
                CarrinhoMapper::map
        );
    }

    @Override
    public Carrinho buscarCarrinhoPorId(long id) {
        List<Carrinho> carrinhos = DB.query(
                "SELECT * FROM carrinho WHERE id = ?",
                CarrinhoMapper::map,
                id
        );
        return carrinhos.isEmpty() ? null : carrinhos.getFirst();
    }

    @Override
    public Carrinho buscarCarrinhoPorClienteId(long clienteId) {
        List<Carrinho> carrinhos = DB.query(
                "SELECT * FROM carrinho WHERE clienteId = ?",
                CarrinhoMapper::map,
                clienteId
        );
        return carrinhos.isEmpty() ? null : carrinhos.getFirst();
    }

    @Override
    public void criarCarrinho(Carrinho carrinho) {
        long id = DB.executeAndReturnId(
                "INSERT INTO carrinho (clienteId) VALUES (?)",
                carrinho.getClienteId()
        );
        carrinho.setId(id);
    }

    @Override
    public void adicionarItem(long carrinhoId, long produtoId, int quantidade) {
        List<Map<String, Object>> itensExistentes = DB.query(
                "SELECT quantidade FROM carrinho_item WHERE carrinhoId = ? AND produtoId = ?",
                rs -> {
                    Map<String, Object> item = new HashMap<>();
                    try {
                        item.put("quantidade", rs.getInt("quantidade"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return item;
                },
                carrinhoId, produtoId
        );

        if (!itensExistentes.isEmpty()) {
            int quantidadeAtual = (Integer) itensExistentes.get(0).get("quantidade");
            atualizarQuantidadeItem(carrinhoId, produtoId, quantidadeAtual + quantidade);
        } else {
            DB.execute(
                    "INSERT INTO carrinho_item (carrinhoId, produtoId, quantidade) VALUES (?, ?, ?)",
                    carrinhoId, produtoId, quantidade
            );
        }
    }

    @Override
    public void removerItem(long carrinhoId, long produtoId) {
        DB.execute(
                "DELETE FROM carrinho_item WHERE carrinhoId = ? AND produtoId = ?",
                carrinhoId, produtoId
        );
    }

    @Override
    public void atualizarQuantidadeItem(long carrinhoId, long produtoId, int quantidade) {
        if (quantidade <= 0) {
            removerItem(carrinhoId, produtoId);
        } else {
            DB.execute(
                    "UPDATE carrinho_item SET quantidade = ? WHERE carrinhoId = ? AND produtoId = ?",
                    quantidade, carrinhoId, produtoId
            );
        }
    }

    @Override
    public void limparCarrinho(long carrinhoId) {
        DB.execute(
                "DELETE FROM carrinho_item WHERE carrinhoId = ?",
                carrinhoId
        );
    }

    @Override
    public void deletarCarrinho(long id) {
        limparCarrinho(id);
        DB.execute("DELETE FROM carrinho WHERE id = ?", id);
    }

    @Override
    public Carrinho buscarCarrinhoCompletoPorId(long id) {
        Carrinho carrinho = buscarCarrinhoPorId(id);
        if (carrinho != null) {
            carregarItens(carrinho);
        }
        return carrinho;
    }

    @Override
    public Carrinho buscarCarrinhoCompletoPorClienteId(long clienteId) {
        Carrinho carrinho = buscarCarrinhoPorClienteId(clienteId);
        if (carrinho != null) {
            carregarItens(carrinho);
        }
        return carrinho;
    }

    private void carregarItens(Carrinho carrinho) {
        List<Map<String, Object>> itensData = DB.query(
                """
                    SELECT ci.produtoId, ci.quantidade, 
                           p.id, p.nome, p.preco, p.estoque, p.tipoProduto
                    FROM carrinho_item ci
                    INNER JOIN produto p ON ci.produtoId = p.id
                    WHERE ci.carrinhoId = ?
                """,
                rs -> {
                    Map<String, Object> item = new HashMap<>();
                    try {
                        item.put("quantidade", rs.getInt("quantidade"));
                        Produto produto = new Produto(
                                rs.getLong("id"),
                                rs.getString("nome"),
                                rs.getDouble("preco"),
                                rs.getInt("estoque"),
                                rs.getString("tipoProduto")
                        );
                        item.put("produto", produto);
                    } catch (SQLException e) {
                        throw new RuntimeException("Erro ao carregar itens do carrinho", e);
                    }
                    return item;
                },
                carrinho.getId()
        );

        Map<Produto, Integer> itens = new HashMap<>();
        for (Map<String, Object> itemData : itensData) {
            Produto produto = (Produto) itemData.get("produto");
            Integer quantidade = (Integer) itemData.get("quantidade");
            itens.put(produto, quantidade);
        }

        carrinho.setItens(itens);
    }
}
