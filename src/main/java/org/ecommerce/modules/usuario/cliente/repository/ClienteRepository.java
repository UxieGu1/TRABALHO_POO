package org.ecommerce.modules.usuario.cliente.repository;

import org.ecommerce.modules.usuario.cliente.Cliente;

import java.util.List;


public interface ClienteRepository {

    List<Cliente> buscarClientes();
    Cliente buscarClientePorId(long id);
    Cliente buscarClientePorEmail(String email);
    void salvarCliente(Cliente cliente);
    void atualizarCliente(long id, Cliente novoCliente);
    void deletarClientePorId(long id);

}
