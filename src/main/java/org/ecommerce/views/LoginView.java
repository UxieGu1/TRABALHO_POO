package org.ecommerce.views;

import org.ecommerce.modules.auth.AuthService;
import org.ecommerce.modules.usuario.administrador.Administrador;
import org.ecommerce.modules.usuario.administrador.repository.AdministradorRepositoryImpl;
import org.ecommerce.modules.usuario.cliente.Cliente;
import org.ecommerce.modules.usuario.cliente.repository.ClienteRepositoryImpl;
import org.ecommerce.utils.DatabaseInitializer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginView extends JFrame {

    private final AuthService authService;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JRadioButton radioCliente;
    private JRadioButton radioAdmin;
    private JLabel lblMensagem;

    public LoginView(AuthService authService) {
        this.authService = authService;
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("Login E-Commerce");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());
    }

    private void inicializarComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("Bem-vindo", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        // Inputs
        gbc.gridwidth = 2; gbc.gridy = 1;
        add(new JLabel("E-mail:"), gbc);

        txtEmail = new JTextField(20);
        gbc.gridy = 2;
        add(txtEmail, gbc);

        gbc.gridy = 3;
        add(new JLabel("Senha:"), gbc);

        txtSenha = new JPasswordField(20);
        gbc.gridy = 4;
        add(txtSenha, gbc);

        // Radio Buttons
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        radioCliente = new JRadioButton("Cliente", true);
        radioAdmin = new JRadioButton("Administrador");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(radioCliente);
        grupo.add(radioAdmin);
        panelTipo.add(radioCliente);
        panelTipo.add(radioAdmin);

        gbc.gridy = 5;
        add(panelTipo, gbc);

        // Botão Entrar
        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setBackground(new Color(70, 130, 180));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.addActionListener(this::executarLogin);

        gbc.gridy = 6; gbc.ipady = 10;
        add(btnEntrar, gbc);

        // Botão Cadastro
        JButton btnCadastrar = new JButton("Criar nova conta");
        btnCadastrar.setBorderPainted(false);
        btnCadastrar.setContentAreaFilled(false);
        btnCadastrar.setForeground(Color.BLUE);
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCadastrar.addActionListener(e -> abrirModalCadastro());

        gbc.gridy = 7; gbc.ipady = 0;
        add(btnCadastrar, gbc);

        // Mensagem de Erro
        lblMensagem = new JLabel(" ");
        lblMensagem.setForeground(Color.RED);
        lblMensagem.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 8;
        add(lblMensagem, gbc);
    }

    private void executarLogin(ActionEvent e) {
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            lblMensagem.setText("Preencha todos os campos.");
            return;
        }

        try {
            if (radioAdmin.isSelected()) {
                Administrador admin = authService.loginAdministrador(email, senha);
                if (admin != null) {
                    abrirAreaAdministrador(admin);
                } else {
                    lblMensagem.setText("Credenciais de Admin inválidas.");
                }
            } else {
                Cliente cliente = authService.loginCliente(email, senha);
                if (cliente != null) {
                    abrirAreaCliente(cliente);
                } else {
                    lblMensagem.setText("E-mail ou senha inválidos.");
                }
            }
        } catch (Exception ex) {
            lblMensagem.setText("Erro: " + ex.getMessage());
        }
    }

    // --- MÉTODOS DE NAVEGAÇÃO ---

    private void abrirAreaAdministrador(Administrador admin) {
        this.dispose(); // Fecha tela de login

        // AQUI: Instancie e abra a sua tela de AdminDashboardView
        // Exemplo: new AdminDashboardView(admin).setVisible(true);
        JOptionPane.showMessageDialog(null, "Logado como Admin: " + admin.getNome() + "\n(Abra a tela de Admin aqui)");
    }

    private void abrirAreaCliente(Cliente cliente) {
        this.dispose(); // Fecha tela de login

        // AQUI: Instancie e abra a sua tela de VitrineView
        // Exemplo: new VitrineView(cliente).setVisible(true);
        JOptionPane.showMessageDialog(null, "Logado como Cliente: " + cliente.getNome() + "\n(Abra a tela de Vitrine aqui)");
    }

    private void abrirModalCadastro() {
        // Lógica simples de cadastro rápido apenas para teste
        JTextField nome = new JTextField();
        JTextField email = new JTextField();
        JPasswordField senha = new JPasswordField();

        Object[] message = {"Nome:", nome, "Email:", email, "Senha:", senha};
        int option = JOptionPane.showConfirmDialog(this, message, "Novo Cliente", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                authService.registrarCliente(nome.getText(), email.getText(), new String(senha.getPassword()), "Endereço", "0000");
                JOptionPane.showMessageDialog(this, "Cadastrado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseInitializer.initialize();

            ClienteRepositoryImpl clienteRepo = new ClienteRepositoryImpl();
            AdministradorRepositoryImpl adminRepo = new AdministradorRepositoryImpl();
            AuthService auth = new AuthService(clienteRepo, adminRepo);

            // Garante dados mínimos para teste sem logs
            try {
                if(clienteRepo.buscarClientePorEmail("cliente@teste.com") == null)
                    auth.registrarCliente("Cliente Teste", "cliente@teste.com", "123456", "Rua", "00");

                if(adminRepo.buscarAdministradorPorEmail("admin@teste.com") == null)
                    auth.registrarAdministrador("Admin", "admin@teste.com", "admin123");
            } catch (Exception ignored) {}

            new LoginView(auth).setVisible(true);
        });
    }
}