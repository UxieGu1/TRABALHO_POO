package org.ecommerce.modules.pedido;

import org.ecommerce.modules.produto.Produto;

public class ItemPedido {
    private long id;
    private Produto produto;
    private int quantidade;
    private double precoUnitario; // Preço no momento da compra

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPreco();
    }

    public ItemPedido(Long id, Produto produto, int quantidade, double precoUnitario) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public double getSubtotal() {
        return precoUnitario * quantidade;
    }

    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }
}