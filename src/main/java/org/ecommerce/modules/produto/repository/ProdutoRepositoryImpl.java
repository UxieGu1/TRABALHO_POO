package org.ecommerce.modules.produto.repository;

import org.ecommerce.modules.produto.Produto;

import java.util.List;
import org.ecommerce.utils.DB;

public class ProdutoRepositoryImpl implements ProdutoRepository {

    @Override
    public List<Produto> buscarProdutos() {

        return DB.query(
                "select * from produto",
                ProdutoMapper::map
        );
    }

    @Override
    public Produto buscarProdutoPorId(long id) {
        List<Produto> produtos = DB.query(
                "select * from produto where id = ?",
                ProdutoMapper::map,
                id
        );
        return produtos.isEmpty() ? null : produtos.getFirst();
    }

    @Override
    public void salvarProduto(Produto produto) {
        DB.execute(
                " insert into produto(nome, preco, estoque, tipoProduto) " +
                        "   values (" +
                        "       ?, " +
                        "       ?, " +
                        "       ?," +
                        "       ? " +
                        "   ) ",
                produto.getNome(),
                produto.getPreco(),
                produto.getEstoque(),
                produto.getTipoProduto()
        );
    }

    @Override
    public void atualizarProduto(long id, Produto novoProduto) {
        DB.execute(
                " UPDATE produto SET " +
                        "   nome = ?, " +
                        "   preco = ?, " +
                        "   estoque = ?, " +
                        "   tipoProduto = ? " +
                        " WHERE id = ? ",
                novoProduto.getNome(),
                novoProduto.getPreco(),
                novoProduto.getEstoque(),
                novoProduto.getTipoProduto(),
                id
        );
    }

    @Override
    public void deletarProdutoPorId(long id) {
        DB.execute("Delete from where id = ?", id);
    }

}
