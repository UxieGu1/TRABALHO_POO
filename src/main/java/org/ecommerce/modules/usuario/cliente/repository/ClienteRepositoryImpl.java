package org.ecommerce.modules.usuario.cliente.repository;

import org.ecommerce.modules.usuario.cliente.Cliente;
import org.ecommerce.utils.DB;

import java.util.List;


public class ClienteRepositoryImpl implements ClienteRepository {

    @Override
    public List<Cliente> buscarClientes() {
        return DB.query(
                "select * from cliente",
                ClienteMapper::map
        );
    }

    @Override
    public Cliente buscarClientePorId(long id) {
        List<Cliente> clientes = DB.query(
                "select * from cliente where id = ?",
                ClienteMapper::map,
                id
        );
        return clientes.isEmpty() ? null : clientes.getFirst();
    }

    @Override
    public Cliente buscarClientePorEmail(String email) {
        List<Cliente> clientes = DB.query(
                "SELECT * FROM cliente WHERE email = ?",
                ClienteMapper::map,
                email
        );
        return clientes.isEmpty() ? null : clientes.getFirst();
    }

    @Override
    public void salvarCliente(Cliente cliente) {
        DB.execute(
                " insert into cliente(nome, email, senha, endereco, telefone) " +
                "   values (" +
                "       ?, " +
                "       ?, " +
                "       ?, " +
                "       ?, " +
                "       ?  "  +
                "   ) ",
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getSenha(),
                cliente.getEndereco(),
                cliente.getTelefone()
        );
    }

    @Override
    public void atualizarCliente(long id, Cliente novoCliente) {
        DB.execute(
                " update cliente set " +
                        "   nome = ?, " +
                        "   email = ?, " +
                        "   senha = ?, " +
                        "   endereco = ?, " +
                        "   telefone = ? " +
                        " where id = ? ",
                novoCliente.getNome(),
                novoCliente.getEmail(),
                novoCliente.getSenha(),
                novoCliente.getEndereco(),
                novoCliente.getTelefone(),
                id
        );
    }


    @Override
    public void deletarClientePorId(long id) {

        DB.execute("delete from cliente where id = ?", id);
    }
}