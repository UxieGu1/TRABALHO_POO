package org.ecommerce.utils;

public final class DatabaseInitializer {

    private DatabaseInitializer() {
        throw new UnsupportedOperationException("Classe utilitária");
    }

    public static void initialize() {
        createClienteTable();
        createAdministradorTable();
        createProdutoTable();
        createCarrinhoTable();
        createCarrinhoItemTable();
    }

    private static void createClienteTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS cliente (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    email TEXT NOT NULL,
                    senha TEXT NOT NULL,
                    endereco TEXT,
                    telefone TEXT
                )
                """;
        DB.execute(sql);
    }
    private static void createAdministradorTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS administrador (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE,
                    senha TEXT NOT NULL
                )
                """;
        DB.execute(sql);
    }

    private static void createProdutoTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS produto (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    preco REAL NOT NULL,
                    estoque INTEGER NOT NULL,
                    tipoProduto TEXT NOT NULL
                )
                """;
        DB.execute(sql);
    }

    private static void createCarrinhoTable(){
        String sql = """
                CREATE TABLE IF NOT EXISTS carrinho (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    clienteId INTEGER NOT NULL,
                    FOREIGN KEY (clienteId) REFERENCES cliente(id)
                )
                """;
        DB.execute(sql);
    }

    private static void createCarrinhoItemTable(){
        String sql = """
                CREATE TABLE IF NOT EXISTS carrinho_item (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    carrinhoId INTEGER NOT NULL,
                    produtoId INTEGER NOT NULL,
                    quantidade INTEGER NOT NULL,
                    FOREIGN KEY (carrinhoId) REFERENCES carrinho(id),
                    FOREIGN KEY (produtoId) REFERENCES produto(id)
                )
                """;
        DB.execute(sql);
    }
}

