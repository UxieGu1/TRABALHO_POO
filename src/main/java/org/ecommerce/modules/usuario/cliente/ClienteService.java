package org.ecommerce.modules.usuario.cliente;

import org.ecommerce.modules.usuario.cliente.repository.ClienteRepository;

import java.util.List;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository repository) {
        this.clienteRepository = repository;
    }

    public List<Cliente> buscarClientes() {

        return clienteRepository.buscarClientes();
    }

    public Cliente buscarClientePorId(long id){
        return clienteRepository.buscarClientePorId(id);
    }

    public void salvarCliente(Cliente cliente){
        clienteRepository.salvarCliente(cliente);
    }

    public void atualizarCliente(Cliente novoCliente){
        clienteRepository.atualizarCliente(novoCliente.getId(), novoCliente);
    }

    public void deletar(long id){
        clienteRepository.deletarClientePorId(id);
    }
}
