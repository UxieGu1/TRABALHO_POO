package org.ecommerce.views;

import org.ecommerce.modules.auth.AuthService;
import org.ecommerce.modules.usuario.administrador.Administrador;
import org.ecommerce.modules.usuario.administrador.repository.AdministradorRepositoryImpl;
import org.ecommerce.modules.usuario.cliente.Cliente;
import org.ecommerce.modules.usuario.cliente.repository.ClienteRepositoryImpl;
import org.ecommerce.utils.DatabaseInitializer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginView extends JFrame {

    private final AuthService authService;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JRadioButton radioCliente;
    private JRadioButton radioAdmin;
    private JLabel lblMensagem;

    // Cores do Tema
    private final Color COLOR_PRIMARY = new Color(52, 152, 219); // Azul
    private final Color COLOR_BG = new Color(245, 246, 250); // Cinza muito claro
    private final Color COLOR_TEXT = new Color(44, 62, 80); // Cinza escuro

    public LoginView(AuthService authService) {
        this.authService = authService;
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("E-Commerce");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new GridBagLayout());
    }

    private void inicializarComponentes() {
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel lblTitulo = new JLabel("Bem-vindo");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(COLOR_TEXT);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        cardPanel.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Faça login para continuar");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        cardPanel.add(lblSubtitulo, gbc);

        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridy = 2;
        cardPanel.add(criarLabel("E-mail"), gbc);

        txtEmail = criarTextField();
        gbc.gridy = 3;
        cardPanel.add(txtEmail, gbc);

        gbc.gridy = 4;
        cardPanel.add(criarLabel("Senha"), gbc);

        txtSenha = criarPasswordField();
        gbc.gridy = 5;
        cardPanel.add(txtSenha, gbc);

        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTipo.setBackground(Color.WHITE);
        radioCliente = new JRadioButton("Sou Cliente", true);
        radioAdmin = new JRadioButton("Sou Administrador");
        estilizarRadio(radioCliente);
        estilizarRadio(radioAdmin);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(radioCliente);
        grupo.add(radioAdmin);

        panelTipo.add(radioCliente);
        panelTipo.add(radioAdmin);

        gbc.gridy = 6;
        gbc.insets = new Insets(15, 0, 10, 0);
        cardPanel.add(panelTipo, gbc);

        // 5. Botão Entrar
        JButton btnEntrar = new JButton("ENTRAR NO SISTEMA");
        estilizarBotao(btnEntrar, COLOR_PRIMARY);
        btnEntrar.addActionListener(this::executarLogin);

        gbc.gridy = 7;
        gbc.ipady = 10; // Botão mais alto
        cardPanel.add(btnEntrar, gbc);

        // 6. Botão Cadastrar
        JButton btnCadastrar = new JButton("Não tem conta? Cadastre-se");
        btnCadastrar.setBorderPainted(false);
        btnCadastrar.setContentAreaFilled(false);
        btnCadastrar.setForeground(COLOR_PRIMARY);
        btnCadastrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCadastrar.addActionListener(e -> abrirModalCadastro());

        gbc.gridy = 8;
        gbc.ipady = 0;
        cardPanel.add(btnCadastrar, gbc);

        // 7. Mensagem de Erro
        lblMensagem = new JLabel(" ");
        lblMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMensagem.setForeground(new Color(231, 76, 60)); // Vermelho
        lblMensagem.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 9;
        cardPanel.add(lblMensagem, gbc);

        // Adiciona o card na janela principal
        add(cardPanel);
    }

    // --- Helpers de Estilo ---
    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(COLOR_TEXT);
        return lbl;
    }

    private JTextField criarTextField() {
        JTextField txt = new JTextField(20);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10) // Padding interno
        ));
        return txt;
    }

    private JPasswordField criarPasswordField() {
        JPasswordField txt = new JPasswordField(20);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return txt;
    }

    private void estilizarRadio(JRadioButton rb) {
        rb.setBackground(Color.WHITE);
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rb.setFocusPainted(false);
    }

    private void estilizarBotao(JButton btn, Color cor) {
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- Lógica (Mantida Igual) ---
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
        new ClienteView(cliente).setVisible(true);
    }

    private void abrirModalCadastro() {
        // (Mantive simplificado para não estourar o tamanho, mas idealmente seria um JDialog customizado igual a tela de login)
        JTextField txtNome = new JTextField();
        JTextField txtEmailCadastro = new JTextField();
        JPasswordField txtSenhaCadastro = new JPasswordField();
        JTextField txtEndereco = new JTextField();
        JTextField txtTelefone = new JTextField();

        Object[] message = {
                "Nome Completo:", txtNome,
                "E-mail:", txtEmailCadastro,
                "Senha:", txtSenhaCadastro,
                "Endereço:", txtEndereco,
                "Telefone:", txtTelefone
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Novo Cadastro", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                authService.registrarCliente(
                        txtNome.getText(), txtEmailCadastro.getText(), new String(txtSenhaCadastro.getPassword()),
                        txtEndereco.getText(), txtTelefone.getText()
                );
                JOptionPane.showMessageDialog(this, "Conta criada! Faça login.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // ATIVA O TEMA NIMBUS (O segredo da beleza)
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }

            DatabaseInitializer.initialize();
            ClienteRepositoryImpl clienteRepo = new ClienteRepositoryImpl();
            AdministradorRepositoryImpl adminRepo = new AdministradorRepositoryImpl();
            AuthService auth = new AuthService(clienteRepo, adminRepo);
            new LoginView(auth).setVisible(true);
        });
    }
}