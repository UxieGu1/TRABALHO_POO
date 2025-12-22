package org.ecommerce.modules.pedido.repository;

import org.ecommerce.modules.pedido.Pedido;
import java.util.List;

public interface PedidoRepository {

    void salvarPedido(Pedido pedido);
    List<Pedido> buscarPedidosPorCliente(long clienteId);
}