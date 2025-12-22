package org.ecommerce.modules.produto;

import org.ecommerce.modules.produto.repository.ProdutoRepositoryImpl;

import java.util.List;

public class ProdutoService {

    private final ProdutoRepositoryImpl produtoRepository;

    public ProdutoService(ProdutoRepositoryImpl produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> buscarProdutos (){
        return produtoRepository.buscarProdutos();
    }

    public Produto buscarProdutoPorId(long id){
        return produtoRepository.buscarProdutoPorId(id);
    }

    public void salvarProduto(Produto produto){
        produtoRepository.salvarProduto(produto);
    }
    public void atualizarProduto(Produto novoProduto){
        produtoRepository.atualizarProduto(novoProduto.getId(), novoProduto);
    }
    public void deletarProdutoPorId(long id){
        produtoRepository.deletarProdutoPorId(id);
    }
}
