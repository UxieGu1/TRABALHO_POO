package org.ecommerce.modules.usuario.administrador;

import org.ecommerce.core.Usuario;

public class Administrador extends Usuario {

    private static String cargo = "Administrador";

    public Administrador(long id, String nome, String email, String senha) {

        super(id, nome, email, senha);
    }

    public static String getCargo() {
        return cargo;
    }

    public static void setCargo(String cargo) {
        Administrador.cargo = cargo;
    }


    @Override
    public String toString() {
        return "Administrador{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
