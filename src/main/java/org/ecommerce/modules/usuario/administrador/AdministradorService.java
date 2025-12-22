package org.ecommerce.modules.usuario.administrador;

import org.ecommerce.modules.usuario.administrador.repository.AdministradorRepository;
import java.util.List;

public class AdministradorService {

    private final AdministradorRepository administradorRepository;

    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public List<Administrador> buscarAdministradores() {
        return administradorRepository.buscarAdministradores();
    }

    public Administrador buscarAdministradorPorId(long id) {
        return administradorRepository.buscarAdministradorPorId(id);
    }

    public Administrador buscarAdministradorPorEmail(String email) {
        return administradorRepository.buscarAdministradorPorEmail(email);
    }

    public void salvarAdministrador(Administrador administrador) {
        administradorRepository.salvarAdministrador(administrador);
    }

    public void atualizarAdministrador(Administrador novoAdministrador) {
        administradorRepository.atualizarAdministrador(novoAdministrador.getId(), novoAdministrador);
    }

    public void deletar(long id) {

        administradorRepository.deletarAdministradorPorId(id);
    }
}


