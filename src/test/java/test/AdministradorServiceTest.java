package test;

import org.ecommerce.modules.usuario.administrador.Administrador;
import org.ecommerce.modules.usuario.administrador.AdministradorService;
import org.ecommerce.modules.usuario.administrador.repository.AdministradorRepositoryImpl;
import org.ecommerce.utils.DB;
import org.ecommerce.utils.DatabaseInitializer;

import java.util.List;

public class AdministradorServiceTest {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Testes de AdministradorService ===");

        // 1. Inicializa o banco e as tabelas
        DatabaseInitializer.initialize();

        // 2. Limpa a tabela de administradores
        limparTabela();

        // 3. Injeção de Dependência manual
        AdministradorRepositoryImpl repository = new AdministradorRepositoryImpl();
        AdministradorService adminService = new AdministradorService(repository);

        try {
            // TESTE 1: Salvar Administrador
            System.out.println("\nTeste 1: Salvar Administrador...");
            Administrador novoAdmin = new Administrador(0, "Morgana", "morg@ecommerce.com", "senhafoda");
            adminService.salvarAdministrador(novoAdmin);
            System.out.println("✔ Administrador salvo com sucesso.");

            // TESTE 2: Buscar por E-mail
            System.out.println("\nTeste 2: Buscar Administrador por E-mail...");
            Administrador adminRecuperado = adminService.buscarAdministradorPorEmail("morg@ecommerce.com");
            if (adminRecuperado != null && adminRecuperado.getNome().equals("Morgana")) {
                System.out.println("✔ Administrador recuperado: " + adminRecuperado);
            } else {
                throw new RuntimeException("❌ Falha ao recuperar administrador!");
            }

            // TESTE 3: Atualizar Administrador
            System.out.println("\nTeste 3: Atualizar Administrador...");
            adminRecuperado.setNome("Morgana Gomes");
            adminService.atualizarAdministrador(adminRecuperado);

            Administrador adminAtualizado = adminService.buscarAdministradorPorId(adminRecuperado.getId());
            if (adminAtualizado != null && adminAtualizado.getNome().equals("Morgana Gomes")) {
                System.out.println("✔ Administrador atualizado com sucesso.");
            } else {
                throw new RuntimeException("❌ Falha ao atualizar administrador!");
            }

            // TESTE 4: Listar Administradores
            System.out.println("\nTeste 4: Listar todos os Administradores...");
            List<Administrador> todos = adminService.buscarAdministradores();
            System.out.println("✔ Quantidade de administradores: " + todos.size());

            // TESTE 5: Deletar Administrador
            /*System.out.println("\nTeste 5: Deletar Administrador...");
            adminService.deletar(adminRecuperado.getId());
            Administrador adminDeletado = adminService.buscarAdministradorPorId(adminRecuperado.getId());
            if (adminDeletado == null) {
                System.out.println("✔ Administrador deletado com sucesso.");
            } else {
                throw new RuntimeException("❌ Falha ao deletar administrador!");
            }*/

            System.out.println("\n=======================================");
            System.out.println("✅ TODOS OS TESTES DE ADMIN PASSARAM!");
            System.out.println("=======================================");

        } catch (Exception e) {
            System.err.println("\n❌ OCORREU UM ERRO NOS TESTES DE ADMIN:");
            e.printStackTrace();
        }
    }

    private static void limparTabela() {
        // Apaga os dados da tabela correta
        DB.execute("DELETE FROM administrador");
        // Reinicia o auto-incremento do ID no SQLite
        DB.execute("DELETE FROM sqlite_sequence WHERE name='administrador'");
    }
}