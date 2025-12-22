package test;

import org.ecommerce.modules.produto.Produto;
import org.ecommerce.modules.produto.ProdutoService;
import org.ecommerce.modules.produto.repository.ProdutoRepositoryImpl;
import org.ecommerce.utils.DB;

public class ProdutoServiceTest {

    public static Produto criarProdutoPadrao(ProdutoService service) {
        Produto p = new Produto(0, "Notebook", 3000.0, 10, "ELETRONICOS");
        service.salvarProduto(p);
        return service.buscarProdutos().get(0);
    }

    public static void main(String[] args) {
        System.out.println("=== Testando Módulo de Produto ===");
        DB.execute("DELETE FROM produto");

        ProdutoService service = new ProdutoService(new ProdutoRepositoryImpl());
        Produto p = criarProdutoPadrao(service);

        if (p != null) System.out.println("✅ Produto testado e criado: " + p.getNome());
    }
}