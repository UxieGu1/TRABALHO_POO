package org.ecommerce.modules.produto.repository;

import org.ecommerce.modules.produto.Produto;
import org.ecommerce.utils.DB;

import java.util.List;

public class ProdutoRepositoryImpl implements ProdutoRepository {

    @Override
    public List<Produto> buscarProdutos() {
        return DB.query(
                "SELECT * FROM produto",
                ProdutoMapper::map
        );
    }

    @Override
    public Produto buscarProdutoPorId(long id) {
        List<Produto> produtos = DB.query(
                "SELECT * FROM produto WHERE id = ?",
                ProdutoMapper::map,
                id
        );
        return produtos.isEmpty() ? null : produtos.getFirst();
    }

    @Override
    public void salvarProduto(Produto produto) {
        DB.execute(
                "INSERT INTO produto (nome, preco, estoque, tipoProduto) VALUES (?, ?, ?, ?)",
                produto.getNome(),
                produto.getPreco(),
                produto.getEstoque(),
                produto.getTipoProduto()
        );
    }

    @Override
    public void atualizarProduto(long id, Produto novoProduto) {
        DB.execute(
                "UPDATE produto SET nome = ?, preco = ?, estoque = ?, tipoProduto = ? WHERE id = ?",
                novoProduto.getNome(),
                novoProduto.getPreco(),
                novoProduto.getEstoque(),
                novoProduto.getTipoProduto(),
                id
        );
    }

    @Override
    public void deletarProdutoPorId(long id) {
        String sql = "DELETE FROM produto WHERE id = ?";
        DB.execute(sql, id);
    }
}