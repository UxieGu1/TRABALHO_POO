package org.ecommerce.modules.usuario.administrador.repository;

import org.ecommerce.modules.usuario.administrador.Administrador;
import org.ecommerce.utils.DB;
import java.util.List;

public class AdministradorRepositoryImpl implements AdministradorRepository {

    @Override
    public List<Administrador> buscarAdministradores() {
        return DB.query(
                "SELECT * FROM administrador",
                AdministradorMapper::map
        );
    }

    @Override
    public Administrador buscarAdministradorPorId(long id) {
        List<Administrador> administradores = DB.query(
                "SELECT * FROM administrador WHERE id = ?",
                AdministradorMapper::map,
                id
        );
        return administradores.isEmpty() ? null : administradores.getFirst();
    }

    @Override
    public Administrador buscarAdministradorPorEmail(String email) {
        List<Administrador> administradores = DB.query(
                "SELECT * FROM administrador WHERE email = ?",
                AdministradorMapper::map,
                email
        );
        return administradores.isEmpty() ? null : administradores.getFirst();
    }

    @Override
    public void salvarAdministrador(Administrador administrador) {
        DB.execute(
                "INSERT INTO administrador(nome, email, senha) " +
                "VALUES (?, ?, ?)",
                administrador.getNome(),
                administrador.getEmail(),
                administrador.getSenha()
        );
    }

    @Override
    public void atualizarAdministrador(long id, Administrador novoAdministrador) {
        DB.execute(
                "UPDATE administrador SET " +
                        "nome = ?, " +
                        "email = ?, " +
                        "senha = ? " +
                        "WHERE id = ?",
                novoAdministrador.getNome(),
                novoAdministrador.getEmail(),
                novoAdministrador.getSenha(),
                id
        );
    }

    @Override
    public void deletarAdministradorPorId(long id) {

        DB.execute("DELETE FROM administrador WHERE id = ?", id);
    }
}
