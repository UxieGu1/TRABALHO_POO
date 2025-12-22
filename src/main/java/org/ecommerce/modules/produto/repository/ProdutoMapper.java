package org.ecommerce.modules.produto.repository;

import org.ecommerce.modules.produto.Produto;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ProdutoMapper {

    private ProdutoMapper(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static Produto map(ResultSet rs){
        try{
            return new Produto(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getDouble("preco"),
                    rs.getInt("estoque"),
                    rs.getString("tipoProduto")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao mapear o produto", e);
        }
    }
}
