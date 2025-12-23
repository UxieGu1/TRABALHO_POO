package org.ecommerce.views;

import org.ecommerce.modules.produto.Produto;
import org.ecommerce.modules.produto.repository.ProdutoRepositoryImpl;
import org.ecommerce.modules.usuario.administrador.Administrador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class AdminDashboardView extends JFrame {

    private final Administrador adminLogado;
    private final ProdutoRepositoryImpl produtoRepository;
    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;

    // Cores
    private final Color COLOR_HEADER = new Color(44, 62, 80); // Midnight Blue
    private final Color COLOR_BG = new Color(236, 240, 241);

    public AdminDashboardView(Administrador admin) {
        this.adminLogado = admin;
        this.produtoRepository = new ProdutoRepositoryImpl();

        configurarJanela();
        inicializarComponentes();
        carregarDadosNaTabela();
    }

    private void configurarJanela() {
        setTitle("Painel Administrativo | E-Commerce Manager");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        // --- Navbar Superior ---
        JPanel panelTopo = new JPanel(new BorderLayout());
        panelTopo.setBackground(COLOR_HEADER);
        panelTopo.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblBemVindo = new JLabel("ADMINISTRADOR: " + adminLogado.getNome().toUpperCase());
        lblBemVindo.setForeground(Color.WHITE);
        lblBemVindo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBemVindo.setIcon(UIManager.getIcon("FileView.computerIcon")); // Ícone genérico do sistema

        JButton btnSair = new JButton("Sair / Logout");
        btnSair.setBackground(new Color(192, 57, 43));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnSair.addActionListener(e -> logout());

        panelTopo.add(lblBemVindo, BorderLayout.WEST);
        panelTopo.add(btnSair, BorderLayout.EAST);
        add(panelTopo, BorderLayout.NORTH);

        // --- Tabela Central ---
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20)); // Margem ao redor da tabela
        panelCentral.setBackground(COLOR_BG);

        String[] colunas = {"ID", "Nome do Produto", "Preço (R$)", "Estoque", "Categoria"};
        tableModel = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabelaProdutos = new JTable(tableModel);
        estilizarTabela(tabelaProdutos); // Aplica o estilo moderno

        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove borda feia do scroll
        scrollPane.getViewport().setBackground(Color.WHITE);

        panelCentral.add(scrollPane, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // --- Barra de Ferramentas Inferior ---
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        panelBotoes.setBackground(Color.WHITE);
        panelBotoes.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200,200,200)));

        JButton btnAdicionar = criarBotao("Novo Produto", new Color(39, 174, 96));
        JButton btnEditar = criarBotao("Editar", new Color(41, 128, 185));
        JButton btnRemover = criarBotao("Excluir", new Color(192, 57, 43));

        btnAdicionar.addActionListener(e -> abrirFormularioProduto(null));
        btnEditar.addActionListener(e -> editarProdutoSelecionado());
        btnRemover.addActionListener(e -> removerProdutoSelecionado());

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnRemover);
        add(panelBotoes, BorderLayout.SOUTH);
    }

    private void estilizarTabela(JTable table) {
        table.setRowHeight(35);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(100, 100, 100));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));
    }

    private JButton criarBotao(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- Métodos de Lógica (Iguais ao anterior) ---
    private void carregarDadosNaTabela() {
        tableModel.setRowCount(0);
        List<Produto> produtos = produtoRepository.buscarProdutos();
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{p.getId(), p.getNome(), String.format("%.2f", p.getPreco()), p.getEstoque(), p.getTipoProduto()});
        }
    }

    private void removerProdutoSelecionado() {
        int row = tabelaProdutos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.");
            return;
        }
        long id = (long) tableModel.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Remover produto?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            produtoRepository.deletarProdutoPorId(id);
            carregarDadosNaTabela();
        }
    }

    private void editarProdutoSelecionado() {
        int row = tabelaProdutos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione para editar.");
            return;
        }
        long id = (long) tableModel.getValueAt(row, 0);
        Produto p = produtoRepository.buscarProdutoPorId(id);
        if (p != null) abrirFormularioProduto(p);
    }

    private void abrirFormularioProduto(Produto produtoExistente) {
        // (Lógica do Dialog mantida simplificada aqui para focar na UI principal)
        // Dica: Para modernizar o dialog, use os mesmos paddings e fontes da LoginView
        JDialog dialog = new JDialog(this, produtoExistente == null ? "Novo Produto" : "Editar", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtNome = new JTextField();
        JTextField txtPreco = new JTextField();
        JTextField txtEstoque = new JTextField();
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Eletrônicos", "Roupas", "Livros", "Casa"});

        if(produtoExistente != null) {
            txtNome.setText(produtoExistente.getNome());
            txtPreco.setText(String.valueOf(produtoExistente.getPreco()));
            txtEstoque.setText(String.valueOf(produtoExistente.getEstoque()));
        }

        panel.add(new JLabel("Nome:")); panel.add(txtNome);
        panel.add(new JLabel("Preço:")); panel.add(txtPreco);
        panel.add(new JLabel("Estoque:")); panel.add(txtEstoque);
        panel.add(new JLabel("Tipo:")); panel.add(comboTipo);

        JButton btnSalvar = new JButton("SALVAR");
        btnSalvar.setBackground(new Color(39, 174, 96));
        btnSalvar.setForeground(Color.WHITE);

        btnSalvar.addActionListener(e -> {
            try {
                String nome = txtNome.getText();
                double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
                int estoque = Integer.parseInt(txtEstoque.getText());
                String tipo = (String) comboTipo.getSelectedItem();

                if(produtoExistente == null) produtoRepository.salvarProduto(new Produto(0, nome, preco, estoque, tipo));
                else produtoRepository.atualizarProduto(produtoExistente.getId(), new Produto(produtoExistente.getId(), nome, preco, estoque, tipo));

                carregarDadosNaTabela();
                dialog.dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(dialog, "Erro nos dados."); }
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnSalvar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void logout() {
        dispose();
        System.exit(0);
    }
}