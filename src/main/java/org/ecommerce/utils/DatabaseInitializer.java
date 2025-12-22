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
        createPedidoTable();
        createItemPedidoTable();
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

    private static void createPedidoTable(){
        String sql = """
                CREATE TABLE IF NOT EXISTS pedido (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            cliente_id INTEGER NOT NULL,
                            data_pedido TEXT NOT NULL,
                            total REAL NOT NULL,
                            status TEXT NOT NULL,
                            metodo_pagamento TEXT,
                            endereco_entrega TEXT,
                            FOREIGN KEY (cliente_id) REFERENCES cliente(id)
                            )
                """;
        DB.execute(sql);
    }

    private static void createItemPedidoTable(){
        String sql = """
                CREATE TABLE IF NOT EXISTS item_pedido (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            pedido_id INTEGER NOT NULL,
                            produto_id INTEGER NOT NULL,
                            quantidade INTEGER NOT NULL,
                            preco_unitario REAL NOT NULL, -- Importante salvar o preço da época!
                                    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
                    FOREIGN KEY (produto_id) REFERENCES produto(id)
                            )
                """;
        DB.execute(sql);
    }


}

