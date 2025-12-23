package org.ecommerce.views;

import org.ecommerce.modules.carrinho.Carrinho;
import org.ecommerce.modules.carrinho.ItemCarrinho;
import org.ecommerce.modules.produto.Produto;
import org.ecommerce.modules.produto.repository.ProdutoRepositoryImpl;
import org.ecommerce.modules.usuario.cliente.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ClienteView extends JFrame {

    private final Cliente clienteLogado;
    private final ProdutoRepositoryImpl produtoRepository;
    private final Carrinho carrinho;

    private DefaultTableModel modeloTabelaProdutos;
    private DefaultTableModel modeloTabelaCarrinho;
    private JTable tabelaProdutos;
    private JTable tabelaCarrinho;
    private JLabel lblTotalCarrinho;

    // Cores
    private final Color COLOR_ACCENT = new Color(52, 152, 219); // Azul
    private final Color COLOR_SUCCESS = new Color(46, 204, 113); // Verde
    private final Color COLOR_BG = new Color(236, 240, 241);

    public ClienteView(Cliente cliente) {
        this.clienteLogado = cliente;
        this.produtoRepository = new ProdutoRepositoryImpl();
        this.carrinho = new Carrinho();

        configurarJanela();
        inicializarComponentes();
        carregarProdutosDoBanco();
    }

    private void configurarJanela() {
        setTitle("Loja Virtual | " + clienteLogado.getNome());
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        // --- Topo ---
        JPanel panelTopo = new JPanel(new BorderLayout());
        panelTopo.setBackground(Color.WHITE);
        panelTopo.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblLogo = new JLabel("Minha Loja Virtual");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(new Color(44, 62, 80));

        JPanel panelUser = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelUser.setBackground(Color.WHITE);
        JLabel lblUser = new JLabel("Olá, " + clienteLogado.getNome());
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(149, 165, 166));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.addActionListener(e -> logout());

        panelUser.add(lblUser);
        panelUser.add(Box.createHorizontalStrut(10));
        panelUser.add(btnSair);

        panelTopo.add(lblLogo, BorderLayout.WEST);
        panelTopo.add(panelUser, BorderLayout.EAST);
        add(panelTopo, BorderLayout.NORTH);

        // --- Área Principal (SplitPane) ---

        // 1. Painel da Esquerda (Loja)
        JPanel panelLoja = new JPanel(new BorderLayout());
        panelLoja.setBackground(COLOR_BG);
        panelLoja.setBorder(new EmptyBorder(10, 10, 10, 10));

        TitledBorder borderLoja = BorderFactory.createTitledBorder("Vitrine de Produtos");
        borderLoja.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
        panelLoja.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10,10,10,5), borderLoja));

        String[] colunasLoja = {"ID", "Produto", "Preço (R$)", "Disp."};
        modeloTabelaProdutos = new DefaultTableModel(colunasLoja, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaProdutos = criarTabelaEstilizada(modeloTabelaProdutos);

        JButton btnAdicionar = criarBotaoGrande("ADICIONAR AO CARRINHO >", COLOR_ACCENT);
        btnAdicionar.addActionListener(e -> acaoAdicionarAoCarrinho());

        panelLoja.add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);
        panelLoja.add(btnAdicionar, BorderLayout.SOUTH);

        // 2. Painel da Direita (Carrinho)
        JPanel panelCarrinho = new JPanel(new BorderLayout());
        panelCarrinho.setBackground(COLOR_BG);
        panelCarrinho.setPreferredSize(new Dimension(450, 0));

        TitledBorder borderCarrinho = BorderFactory.createTitledBorder("Seu Carrinho");
        borderCarrinho.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
        borderCarrinho.setTitleColor(new Color(192, 57, 43));
        panelCarrinho.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10,5,10,10), borderCarrinho));

        String[] colunasCarrinho = {"Item", "Qtd", "Subtotal"};
        modeloTabelaCarrinho = new DefaultTableModel(colunasCarrinho, 0);
        tabelaCarrinho = criarTabelaEstilizada(modeloTabelaCarrinho);

        // Footer do Carrinho
        JPanel panelTotal = new JPanel(new GridLayout(3, 1, 5, 5));
        panelTotal.setOpaque(false);
        panelTotal.setBorder(new EmptyBorder(10, 0, 0, 0));

        lblTotalCarrinho = new JLabel("Total: R$ 0,00", SwingConstants.CENTER);
        lblTotalCarrinho.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTotalCarrinho.setForeground(new Color(44, 62, 80));

        JButton btnRemover = new JButton("Remover Item");
        btnRemover.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRemover.addActionListener(e -> acaoRemoverDoCarrinho());

        JButton btnFinalizar = criarBotaoGrande("FINALIZAR COMPRA", COLOR_SUCCESS);
        btnFinalizar.addActionListener(e -> acaoFinalizarCompra());

        panelTotal.add(btnRemover);
        panelTotal.add(lblTotalCarrinho);
        panelTotal.add(btnFinalizar);

        panelCarrinho.add(new JScrollPane(tabelaCarrinho), BorderLayout.CENTER);
        panelCarrinho.add(panelTotal, BorderLayout.SOUTH);

        // Juntar no SplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLoja, panelCarrinho);
        splitPane.setDividerSize(0); // Divisor invisível
        splitPane.setResizeWeight(0.6);
        splitPane.setBackground(COLOR_BG);

        add(splitPane, BorderLayout.CENTER);
    }

    // --- Helpers de Estilo ---
    private JTable criarTabelaEstilizada(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230,230,230));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.GRAY);
        return table;
    }

    private JButton criarBotaoGrande(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(0, 50)); // Altura fixa
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- Lógica (Mantida Igual) ---
    private void carregarProdutosDoBanco() {
        modeloTabelaProdutos.setRowCount(0);
        List<Produto> produtos = produtoRepository.buscarProdutos();
        for (Produto p : produtos) {
            if (p.getEstoque() > 0) {
                modeloTabelaProdutos.addRow(new Object[]{p.getId(), p.getNome(), String.format("%.2f", p.getPreco()), p.getEstoque()});
            }
        }
    }

    private void atualizarTabelaCarrinho() {
        modeloTabelaCarrinho.setRowCount(0);
        for (ItemCarrinho item : carrinho.getItens()) {
            modeloTabelaCarrinho.addRow(new Object[]{item.getProduto().getNome(), item.getQuantidade(), String.format("%.2f", item.getTotal())});
        }
        lblTotalCarrinho.setText("Total: R$ " + String.format("%.2f", carrinho.getValorTotal()));
    }

    private void acaoAdicionarAoCarrinho() {
        int row = tabelaProdutos.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Selecione um produto."); return; }

        long id = (long) tabelaProdutos.getValueAt(row, 0);
        Produto p = produtoRepository.buscarProdutoPorId(id);

        String qtdStr = JOptionPane.showInputDialog(this, "Quantas unidades de " + p.getNome() + "?");
        if(qtdStr == null) return;

        try {
            int qtd = Integer.parseInt(qtdStr);
            if(qtd > 0 && qtd <= p.getEstoque()) {
                carrinho.adicionar(p, qtd);
                atualizarTabelaCarrinho();
            } else { JOptionPane.showMessageDialog(this, "Quantidade inválida."); }
        } catch(Exception e) { JOptionPane.showMessageDialog(this, "Número inválido."); }
    }

    private void acaoRemoverDoCarrinho() {
        int row = tabelaCarrinho.getSelectedRow();
        if(row != -1) { carrinho.remover(row); atualizarTabelaCarrinho(); }
    }

    private void acaoFinalizarCompra() {
        if(carrinho.getItens().isEmpty()) return;
        if(JOptionPane.showConfirmDialog(this, "Finalizar compra de " + lblTotalCarrinho.getText() + "?", "Pagamento", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Compra realizada com sucesso! (Simulação)");
            carrinho.limpar();
            atualizarTabelaCarrinho();
        }
    }

    private void logout() {
        dispose();
        System.exit(0);
    }
}