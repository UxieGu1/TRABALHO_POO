package test;

import org.ecommerce.modules.usuario.cliente.Cliente;
import org.ecommerce.modules.usuario.cliente.ClienteService;
import org.ecommerce.modules.usuario.cliente.repository.ClienteRepositoryImpl;
import org.ecommerce.utils.DB;
import org.ecommerce.utils.DatabaseInitializer;

public class ClienteServiceTest {

    public static Cliente criarClientePadrao(ClienteService service) {
        Cliente c = new Cliente(0, "Morgana", "morg@email.com", "twd", "Rua A, 123", "1194444");
        service.salvarCliente(c);
        return service.buscarClientes().stream()
                .filter(cl -> cl.getEmail().equals("morg@email.com"))
                .findFirst().orElse(null);
    }

    public static void main(String[] args) {
        System.out.println("=== Testando Módulo de Cliente ===");
        DatabaseInitializer.initialize();
        DB.execute("DELETE FROM cliente");

        ClienteService service = new ClienteService(new ClienteRepositoryImpl());
        Cliente c = criarClientePadrao(service);

        if (c != null) System.out.println("✅ Cliente testado e criado: " + c.getNome());
    }
}