package org.ecommerce.modules.produto.repository;

import org.ecommerce.modules.produto.Produto;


import java.util.List;

public interface ProdutoRepository {

    List<Produto> buscarProdutos();
    Produto buscarProdutoPorId(long id);
    void salvarProduto(Produto produto);
    void atualizarProduto(long id, Produto produto);
    void deletarProdutoPorId(long id);
}
