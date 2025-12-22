package org.ecommerce.modules.usuario.administrador.repository;

import org.ecommerce.modules.usuario.administrador.Administrador;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class AdministradorMapper {

    private AdministradorMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Administrador map(ResultSet rs) {
        try {
            return new Administrador(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao mapear o administrador", e);
        }
    }
}
