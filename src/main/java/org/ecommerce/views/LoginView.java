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
        setSize(400, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());
    }

    private void inicializarComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Acesso ao Sistema", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitulo, gbc);

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

        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setBackground(new Color(46, 139, 87));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setFocusPainted(false);
        btnEntrar.addActionListener(this::executarLogin);

        gbc.gridy = 6; gbc.ipady = 10;
        add(btnEntrar, gbc);

        JButton btnCadastrar = new JButton("Criar nova conta");
        btnCadastrar.setBorderPainted(false);
        btnCadastrar.setContentAreaFilled(false);
        btnCadastrar.setForeground(new Color(70, 130, 180));
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCadastrar.addActionListener(e -> abrirModalCadastro());

        gbc.gridy = 7; gbc.ipady = 0;
        add(btnCadastrar, gbc);

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
                    lblMensagem.setText("Admin não encontrado ou senha errada.");
                }
            } else {
                Cliente cliente = authService.loginCliente(email, senha);
                if (cliente != null) {
                    abrirAreaCliente(cliente);
                } else {
                    lblMensagem.setText("Cliente não encontrado ou senha errada.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            lblMensagem.setText("Erro ao conectar no banco.");
        }
    }

    private void abrirAreaAdministrador(Administrador admin) {
        this.dispose();
        new AdminDashboardView(admin).setVisible(true);
    }

    private void abrirAreaCliente(Cliente cliente) {
        this.dispose();
        JOptionPane.showMessageDialog(null, "Olá, " + cliente.getNome() + "!\n(Vitrine de Produtos em breve...)");
    }

    private void abrirModalCadastro() {
        JTextField txtNome = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtSenha = new JPasswordField();
        JTextField txtEndereco = new JTextField();
        JTextField txtTelefone = new JTextField();

        Object[] message = {
                "Nome Completo:", txtNome,
                "E-mail:", txtEmail,
                "Senha:", txtSenha,
                "Endereço de Entrega:", txtEndereco,
                "Telefone / Celular:", txtTelefone
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Novo Cadastro de Cliente", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                if (txtNome.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()) {
                    throw new Exception("Nome e E-mail são obrigatórios!");
                }

                authService.registrarCliente(
                        txtNome.getText(),
                        txtEmail.getText(),
                        new String(txtSenha.getPassword()),
                        txtEndereco.getText(),
                        txtTelefone.getText()
                );

                JOptionPane.showMessageDialog(this, "Conta criada com sucesso! Faça login.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseInitializer.initialize();

            ClienteRepositoryImpl clienteRepo = new ClienteRepositoryImpl();
            AdministradorRepositoryImpl adminRepo = new AdministradorRepositoryImpl();
            AuthService auth = new AuthService(clienteRepo, adminRepo);

            new LoginView(auth).setVisible(true);
        });
    }
}