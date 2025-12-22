package org.ecommerce.modules.auth;

import org.ecommerce.modules.usuario.administrador.Administrador;
import org.ecommerce.modules.usuario.administrador.repository.AdministradorRepository;
import org.ecommerce.modules.usuario.cliente.Cliente;
import org.ecommerce.modules.usuario.cliente.repository.ClienteRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {

    private final ClienteRepository clienteRepository;
    private final AdministradorRepository administradorRepository;

    public AuthService(ClienteRepository clienteRepository, AdministradorRepository administradorRepository) {
        this.clienteRepository = clienteRepository;
        this.administradorRepository = administradorRepository;
    }

    // ========== CLIENTE ==========

    /**
     * Autentica um cliente usando email e senha
     * @param email Email do cliente
     * @param senha Senha em texto plano
     * @return Cliente autenticado ou null se credenciais inválidas
     */
    public Cliente loginCliente(String email, String senha) {
        if (email == null || senha == null) {
            return null;
        }

        Cliente cliente = clienteRepository.buscarClientePorEmail(email);

        if (cliente == null) {
            return null;
        }

        if (!verificarSenha(senha, cliente.getSenha())) {
            return null;
        }

        return cliente;
    }

    /**
     * Registra um novo cliente
     * @param nome Nome do cliente
     * @param email Email do cliente (deve ser único)
     * @param senha Senha em texto plano (será hashada)
     * @param endereco Endereço do cliente
     * @param telefone Telefone do cliente
     * @return Cliente criado
     * @throws IllegalArgumentException Se email já existe ou dados inválidos
     */
    public Cliente registrarCliente(String nome, String email, String senha,
                                    String endereco, String telefone) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }

        if (!validarEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }

        if (clienteRepository.buscarClientePorEmail(email) != null) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        validarSenha(senha);

        // Hash da senha
        String senhaHash = hashSenha(senha);

        // Criar e salvar cliente
        Cliente cliente = new Cliente(0, nome.trim(), email.trim().toLowerCase(), 
                                     senhaHash, endereco, telefone);
        clienteRepository.salvarCliente(cliente);

        return cliente;
    }

    /**
     * Troca a senha de um cliente
     * @param clienteId ID do cliente
     * @param senhaAntiga Senha atual em texto plano
     * @param senhaNova Nova senha em texto plano
     * @throws IllegalArgumentException Se credenciais inválidas
     */
    public void trocarSenhaCliente(long clienteId, String senhaAntiga, String senhaNova) {
        Cliente cliente = clienteRepository.buscarClientePorId(clienteId);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }

        // Verificar senha antiga
        if (!verificarSenha(senhaAntiga, cliente.getSenha())) {
            throw new IllegalArgumentException("Senha antiga incorreta");
        }

        // Validar nova senha
        validarSenha(senhaNova);

        // Hash da nova senha
        String senhaHash = hashSenha(senhaNova);

        // Atualizar cliente com nova senha
        Cliente clienteAtualizado = new Cliente(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                senhaHash,
                cliente.getEndereco(),
                cliente.getTelefone()
        );
        clienteRepository.atualizarCliente(clienteId, clienteAtualizado);
    }

    // ========== ADMINISTRADOR ==========

    /**
     * Autentica um administrador usando email e senha
     * @param email Email do administrador
     * @param senha Senha em texto plano
     * @return Administrador autenticado ou null se credenciais inválidas
     */
    public Administrador loginAdministrador(String email, String senha) {
        if (email == null || senha == null) {
            return null;
        }

        Administrador administrador = administradorRepository.buscarAdministradorPorEmail(email);

        if (administrador == null) {
            return null;
        }

        if (!verificarSenha(senha, administrador.getSenha())) {
            return null;
        }

        return administrador;
    }

    /**
     * Registra um novo administrador
     * @param nome Nome do administrador
     * @param email Email do administrador (deve ser único)
     * @param senha Senha em texto plano (será hashada)
     * @return Administrador criado
     * @throws IllegalArgumentException Se email já existe ou dados inválidos
     */
    public Administrador registrarAdministrador(String nome, String email, String senha) {
        // Validações
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }

        if (!validarEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }

        // Verificar se email já existe
        if (administradorRepository.buscarAdministradorPorEmail(email) != null) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        // Validar senha
        validarSenha(senha);

        // Hash da senha
        String senhaHash = hashSenha(senha);

        // Criar e salvar administrador
        Administrador administrador = new Administrador(0, nome.trim(), 
                                                        email.trim().toLowerCase(), senhaHash);
        administradorRepository.salvarAdministrador(administrador);

        return administrador;
    }

    /**
     * Troca a senha de um administrador
     * @param administradorId ID do administrador
     * @param senhaAntiga Senha atual em texto plano
     * @param senhaNova Nova senha em texto plano
     * @throws IllegalArgumentException Se credenciais inválidas
     */
    public void trocarSenhaAdministrador(long administradorId, String senhaAntiga, String senhaNova) {
        Administrador administrador = administradorRepository.buscarAdministradorPorId(administradorId);

        if (administrador == null) {
            throw new IllegalArgumentException("Administrador não encontrado");
        }

        // Verificar senha antiga
        if (!verificarSenha(senhaAntiga, administrador.getSenha())) {
            throw new IllegalArgumentException("Senha antiga incorreta");
        }

        // Validar nova senha
        validarSenha(senhaNova);

        // Hash da nova senha
        String senhaHash = hashSenha(senhaNova);

        // Atualizar administrador com nova senha
        Administrador administradorAtualizado = new Administrador(
                administrador.getId(),
                administrador.getNome(),
                administrador.getEmail(),
                senhaHash
        );
        administradorRepository.atualizarAdministrador(administradorId, administradorAtualizado);
    }

    // ========== MÉTODOS PRIVADOS ==========

    /**
     * Gera hash SHA-256 da senha
     * @param senha Senha em texto plano
     * @return Hash hexadecimal da senha
     */
    private String hashSenha(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            
            // Converter bytes para hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash da senha", e);
        }
    }

    /**
     * Verifica se a senha digitada corresponde ao hash armazenado
     * @param senhaDigitada Senha em texto plano
     * @param senhaHash Hash armazenado no banco
     * @return true se a senha corresponde, false caso contrário
     */
    private boolean verificarSenha(String senhaDigitada, String senhaHash) {
        if (senhaDigitada == null || senhaHash == null) {
            return false;
        }
        String hashDigitado = hashSenha(senhaDigitada);
        return hashDigitado.equals(senhaHash);
    }

    /**
     * Valida se a senha atende aos critérios mínimos
     * @param senha Senha a ser validada
     * @throws IllegalArgumentException Se a senha não atende aos critérios
     */
    private void validarSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }

        if (senha.length() < 6) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres");
        }
    }

    /**
     * Valida formato de email (validação simples)
     * @param email Email a ser validado
     * @return true se o email é válido, false caso contrário
     */
    private boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Validação simples: deve conter @ e pelo menos um ponto após o @
        return email.contains("@") && email.indexOf("@") < email.lastIndexOf(".");
    }
}
