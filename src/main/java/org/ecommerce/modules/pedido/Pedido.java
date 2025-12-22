package org.ecommerce.modules.pedido;

import org.ecommerce.modules.usuario.cliente.Cliente;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private long id;
    private Cliente cliente;
    private List<ItemPedido> itens = new ArrayList<>();
    private double total;
    private LocalDateTime dataPedido;
    private String status;
    private String metodoPagamento;
    private String enderecoEntrega;

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.dataPedido = LocalDateTime.now();
        this.status = "PENDENTE";
        this.enderecoEntrega = cliente.getEndereco();
    }

    public Pedido(long id, Cliente cliente, String dataStr, double total, String status, String pagamento, String endereco) {
        this.id = id;
        this.cliente = cliente;
        this.dataPedido = LocalDateTime.parse(dataStr);
        this.total = total;
        this.status = status;
        this.metodoPagamento = pagamento;
        this.enderecoEntrega = endereco;
    }

    public void adicionarItem(ItemPedido item) {
        this.itens.add(item);
        calcularTotal();
    }

    public void calcularTotal() {
        this.total = itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItens() { return itens; }
    public double getTotal() { return total; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }
    public String getEnderecoEntrega() { return enderecoEntrega; }
}