package org.ecommerce.modules.usuario.administrador.repository;

import org.ecommerce.modules.usuario.administrador.Administrador;

import java.util.List;

public interface AdministradorRepository {

    List<Administrador> buscarAdministradores();
    Administrador buscarAdministradorPorId(long id);
    Administrador buscarAdministradorPorEmail(String email);
    void salvarAdministrador(Administrador administrador);
    void atualizarAdministrador(long id, Administrador novoAdministrador);
    void deletarAdministradorPorId(long id);

}
