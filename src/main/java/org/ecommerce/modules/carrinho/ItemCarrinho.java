package org.ecommerce.modules.carrinho;

import org.ecommerce.modules.produto.Produto;

public class ItemCarrinho {
    private final Produto produto;
    private int quantidade;

    public ItemCarrinho(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public double getTotal() {
        return produto.getPreco() * quantidade;
    }

    public void adicionarQuantidade(int qtd) {
        this.quantidade += qtd;
    }

    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
}