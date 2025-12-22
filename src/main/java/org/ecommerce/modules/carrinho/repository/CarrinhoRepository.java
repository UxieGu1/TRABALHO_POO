package org.ecommerce.modules.carrinho.repository;

import org.ecommerce.modules.carrinho.Carrinho;

import java.util.List;

public interface CarrinhoRepository {

    List<Carrinho> buscarTodosCarrinhos();
    Carrinho buscarCarrinhoPorId(long id);
    Carrinho buscarCarrinhoPorClienteId(long clienteId);
    void criarCarrinho(Carrinho carrinho);
    void adicionarItem(long carrinhoId, long produtoId, int quantidade);
    void removerItem(long carrinhoId, long produtoId);
    void atualizarQuantidadeItem(long carrinhoId, long produtoId, int quantidade);
    void limparCarrinho(long carrinhoId);
    void deletarCarrinho(long id);
    Carrinho buscarCarrinhoCompletoPorId(long id);
    Carrinho buscarCarrinhoCompletoPorClienteId(long clienteId);

}
