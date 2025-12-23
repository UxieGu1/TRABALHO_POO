package org.ecommerce.views;

import org.ecommerce.modules.produto.Produto;
import org.ecommerce.modules.produto.repository.ProdutoRepositoryImpl;
import org.ecommerce.modules.usuario.administrador.Administrador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboardView extends JFrame {

    private final Administrador adminLogado;
    private final ProdutoRepositoryImpl produtoRepository;
    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;

    public AdminDashboardView(Administrador admin) {
        this.adminLogado = admin;
        this.produtoRepository = new ProdutoRepositoryImpl();

        configurarJanela();
        inicializarComponentes();
        carregarDadosNaTabela();
    }

    private void configurarJanela() {
        setTitle("Painel Administrativo - E-Commerce");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        JPanel panelTopo = new JPanel(new BorderLayout());
        panelTopo.setBackground(new Color(45, 45, 45));
        panelTopo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblBemVindo = new JLabel("Olá, Admin " + adminLogado.getNome());
        lblBemVindo.setForeground(Color.WHITE);
        lblBemVindo.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> logout());

        panelTopo.add(lblBemVindo, BorderLayout.WEST);
        panelTopo.add(btnSair, BorderLayout.EAST);
        add(panelTopo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Preço (R$)", "Estoque", "Tipo"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaProdutos = new JTable(tableModel);
        tabelaProdutos.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnAdicionar = new JButton("Adicionar Produto");
        JButton btnEditar = new JButton("Editar Selecionado");
        JButton btnRemover = new JButton("Remover Selecionado");

        btnAdicionar.setBackground(new Color(46, 139, 87));
        btnAdicionar.setForeground(Color.WHITE);
        btnEditar.setBackground(new Color(70, 130, 180));
        btnEditar.setForeground(Color.WHITE);
        btnRemover.setBackground(new Color(178, 34, 34));
        btnRemover.setForeground(Color.WHITE);

        btnAdicionar.addActionListener(e -> abrirFormularioProduto(null));
        btnEditar.addActionListener(e -> editarProdutoSelecionado());
        btnRemover.addActionListener(e -> removerProdutoSelecionado());

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnRemover);
        add(panelBotoes, BorderLayout.SOUTH);
    }

    private void carregarDadosNaTabela() {
        tableModel.setRowCount(0);
        List<Produto> produtos = produtoRepository.buscarProdutos();

        for (Produto p : produtos) {
            Object[] row = {
                    p.getId(),
                    p.getNome(),
                    String.format("%.2f", p.getPreco()),
                    p.getEstoque(),
                    p.getTipoProduto()
            };
            tableModel.addRow(row);
        }
    }

    private void removerProdutoSelecionado() {
        int row = tabelaProdutos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para remover.");
            return;
        }

        long id = (long) tableModel.getValueAt(row, 0);
        String nome = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover: " + nome + "?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            produtoRepository.deletarProdutoPorId(id);
            carregarDadosNaTabela();
            JOptionPane.showMessageDialog(this, "Produto removido com sucesso!");
        }
    }

    private void editarProdutoSelecionado() {
        int row = tabelaProdutos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
            return;
        }

        long id = (long) tableModel.getValueAt(row, 0);
        Produto produto = produtoRepository.buscarProdutoPorId(id);

        if (produto != null) {
            abrirFormularioProduto(produto);
        }
    }

    private void abrirFormularioProduto(Produto produtoExistente) {
        JDialog dialog = new JDialog(this, produtoExistente == null ? "Novo Produto" : "Editar Produto", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JTextField txtNome = new JTextField();
        JTextField txtPreco = new JTextField();
        JTextField txtEstoque = new JTextField();

        String[] tipos = {"Eletrônicos", "Roupas", "Livros", "Casa", "Alimentos", "Outros"};
        JComboBox<String> comboTipo = new JComboBox<>(tipos);

        if (produtoExistente != null) {
            txtNome.setText(produtoExistente.getNome());
            txtPreco.setText(String.valueOf(produtoExistente.getPreco()));
            txtEstoque.setText(String.valueOf(produtoExistente.getEstoque()));
            comboTipo.setSelectedItem(produtoExistente.getTipoProduto());
        }

        addCampo(panelForm, gbc, 0, "Nome do Produto:", txtNome);
        addCampo(panelForm, gbc, 1, "Preço (R$):", txtPreco);
        addCampo(panelForm, gbc, 2, "Quantidade em Estoque:", txtEstoque);
        addCampo(panelForm, gbc, 3, "Categoria/Tipo:", comboTipo);

        dialog.add(panelForm, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotoes.setBackground(new Color(240, 240, 240));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(46, 139, 87));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 12));

        btnSalvar.addActionListener(e -> {
            try {
                if (txtNome.getText().trim().isEmpty()) throw new Exception("O nome é obrigatório.");

                String nome = txtNome.getText();
                double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
                int estoque = Integer.parseInt(txtEstoque.getText());
                String tipo = (String) comboTipo.getSelectedItem();

                if (produtoExistente == null) {
                    Produto novo = new Produto(0L, nome, preco, estoque, tipo);
                    produtoRepository.salvarProduto(novo);
                } else {
                    Produto atualizado = new Produto(produtoExistente.getId(), nome, preco, estoque, tipo);
                    produtoRepository.atualizarProduto(produtoExistente.getId(), atualizado);
                }

                carregarDadosNaTabela();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Salvo com sucesso!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: Verifique se Preço e Estoque são números válidos.", "Erro de Formatação", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelBotoes.add(btnCancelar);
        panelBotoes.add(btnSalvar);

        dialog.add(panelBotoes, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void addCampo(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent campo) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1; gbc.gridy = y; gbc.weightx = 1.0;
        panel.add(campo, gbc);
    }

    private void logout() {
        this.dispose();
        JOptionPane.showMessageDialog(this, "Sessão encerrada.");
        System.exit(0);
    }
}