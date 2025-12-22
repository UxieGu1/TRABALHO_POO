package test;

import org.ecommerce.modules.carrinho.Carrinho;
import org.ecommerce.modules.carrinho.CarrinhoService;
import org.ecommerce.modules.carrinho.repository.CarrinhoRepositoryImpl;
import org.ecommerce.modules.produto.Produto;
import org.ecommerce.modules.produto.ProdutoService;
import org.ecommerce.modules.produto.repository.ProdutoRepositoryImpl;
import org.ecommerce.modules.usuario.cliente.Cliente;
import org.ecommerce.modules.usuario.cliente.ClienteService;
import org.ecommerce.modules.usuario.cliente.repository.ClienteRepositoryImpl;
import org.ecommerce.utils.DB;
import org.ecommerce.utils.DatabaseInitializer;

public class CarrinhoServiceTest {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TESTE RELACIONAL (CARRINHO) ===");

        // 1. Limpeza total em ordem reversa de dependência
        DatabaseInitializer.initialize();
        limparTudo();

        // 2. Inicialização dos Serviços
        ClienteService clienteService = new ClienteService(new ClienteRepositoryImpl());
        ProdutoService produtoService = new ProdutoService(new ProdutoRepositoryImpl());
        CarrinhoService carrinhoService = new CarrinhoService(new CarrinhoRepositoryImpl());

        try {
            // RELAÇÃO 1: Criar Cliente (usando o teste anterior)
            System.out.println("Passo 1: Integrando com Cliente...");
            Cliente cliente = ClienteServiceTest.criarClientePadrao(clienteService);

            // RELAÇÃO 2: Criar Produto (usando o teste anterior)
            System.out.println("Passo 2: Integrando com Produto...");
            Produto produto = ProdutoServiceTest.criarProdutoPadrao(produtoService);

            // RELAÇÃO 3: Vincular Carrinho ao Cliente
            System.out.println("Passo 3: Criando Carrinho para o Cliente ID: " + cliente.getId());
            Carrinho carrinho = carrinhoService.buscarCarrinhoPorClienteId(cliente.getId());

            // RELAÇÃO 4: Adicionar Produto ao Carrinho (FK de Carrinho + FK de Produto)
            System.out.println("Passo 4: Adicionando " + produto.getNome() + " ao carrinho...");
            carrinhoService.adicionarItem(carrinho.getId(), produto.getId(), 3);

            // Validação Final do Relacionamento
            Carrinho resultado = carrinhoService.buscarCarrinhoPorId(carrinho.getId());

            System.out.println("\n--- RESULTADO FINAL ---");
            System.out.println("Dono do Carrinho: " + cliente.getNome());
            System.out.println("Total de Itens: " + resultado.getQuantidadeTotal());
            System.out.println("Valor Total: R$ " + resultado.getValorTotal());

            if (resultado.getValorTotal() == 9000.0) { // 3 * 3000.0
                System.out.println("\n✅ SUCESSO: Todas as entidades se relacionaram corretamente!");
            }

        } catch (Exception e) {
            System.err.println("\n❌ ERRO NA INTEGRAÇÃO:");
            e.printStackTrace();
        }
    }

    private static void limparTudo() {
        // Filhos primeiro, Pais depois
        DB.execute("DELETE FROM carrinho_item");
        DB.execute("DELETE FROM carrinho");
        DB.execute("DELETE FROM produto");
        DB.execute("DELETE FROM cliente");

        String[] tabelas = {"carrinho_item", "carrinho", "produto", "cliente"};
        for (String t : tabelas) {
            DB.execute("DELETE FROM sqlite_sequence WHERE name='" + t + "'");
        }
    }
}