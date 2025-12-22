package org.ecommerce.modules.carrinho;

import org.ecommerce.modules.produto.Produto;

import java.util.HashMap;
import java.util.Map;

public class Carrinho {

    private long id;
    private long clienteId;
    private Map<Produto, Integer> itens;
    private int quantidadeTotal;
    private double valorTotal;

    public Carrinho() {
        this.itens = new HashMap<>();
        this.quantidadeTotal = 0;
        this.valorTotal = 0.0;
    }

    public Carrinho(long id, long clienteId) {
        this.id = id;
        this.clienteId = clienteId;
        this.itens = new HashMap<>();
        this.quantidadeTotal = 0;
        this.valorTotal = 0.0;
    }

    public Carrinho(long id, long clienteId, Map<Produto, Integer> itens) {
        this.id = id;
        this.clienteId = clienteId;
        this.itens = itens != null ? new HashMap<>(itens) : new HashMap<>();
        calcularTotais();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClienteId() {
        return clienteId;
    }

    public void setClienteId(long clienteId) {
        this.clienteId = clienteId;
    }

    public Map<Produto, Integer> getItens() {
        return new HashMap<>(itens);
    }

    public void setItens(Map<Produto, Integer> itens) {
        this.itens = itens != null ? new HashMap<>(itens) : new HashMap<>();
        calcularTotais();
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void adicionarItem(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        itens.merge(produto, quantidade, Integer::sum);
        calcularTotais();
    }

    public void removerItem(Produto produto) {
        if (produto != null) {
            itens.remove(produto);
            calcularTotais();
        }
    }

    public void atualizarQuantidade(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        if (quantidade <= 0) {
            removerItem(produto);
        } else {
            itens.put(produto, quantidade);
            calcularTotais();
        }
    }

    public void limpar() {
        itens.clear();
        calcularTotais();
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }

    public int quantidadeDeItens() {
        return itens.size();
    }

    private void calcularTotais() {
        quantidadeTotal = itens.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        valorTotal = itens.entrySet().stream()
                .mapToDouble(entry -> {
                    Produto produto = entry.getKey();
                    int quantidade = entry.getValue();
                    return produto.getPreco() * quantidade;
                })
                .sum();
    }

    @Override
    public String toString() {
        return String.format("Carrinho[id=%d, clienteId=%d, itens=%d, quantidadeTotal=%d, valorTotal=%.2f]",
                id, clienteId, itens.size(), quantidadeTotal, valorTotal);
    }
}