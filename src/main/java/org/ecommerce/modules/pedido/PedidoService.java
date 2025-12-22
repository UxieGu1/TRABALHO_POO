package org.ecommerce.modules.pedido;

import org.ecommerce.modules.pedido.repository.PedidoRepository;
import org.ecommerce.modules.pedido.repository.PedidoRepositoryImpl;
import org.ecommerce.modules.produto.Produto;
import org.ecommerce.modules.produto.repository.ProdutoRepositoryImpl;

import java.util.List;

public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepositoryImpl produtoRepository;

    public PedidoService() {
        this.pedidoRepository = new PedidoRepositoryImpl();
        this.produtoRepository = new ProdutoRepositoryImpl();
    }

    public void realizarPedido(Pedido pedido, String metodoPagamento) {
        // 1. Validação básica
        if (pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("O pedido não pode estar vazio.");
        }

        // 2. Verificar Estoque (Antes de salvar qualquer coisa)
        for (ItemPedido item : pedido.getItens()) {
            Produto produtoEstoque = produtoRepository.buscarProdutoPorId(item.getProduto().getId());

            if (produtoEstoque == null) {
                throw new IllegalArgumentException("Produto não encontrado: " + item.getProduto().getNome());
            }

            if (produtoEstoque.getEstoque() < item.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " +
                        item.getProduto().getNome() + ". Disponível: " + produtoEstoque.getEstoque());
            }
        }

        // 3. Definir dados finais do pedido
        pedido.setMetodoPagamento(metodoPagamento);
        pedido.setStatus("PAGO"); // Num sistema real, integraria com gateway de pagamento

        // 4. Salvar o Pedido e os Itens (Transação no Repository)
        pedidoRepository.salvarPedido(pedido);

        // 5. Baixar o Estoque
        for (ItemPedido item : pedido.getItens()) {
            Produto p = produtoRepository.buscarProdutoPorId(item.getProduto().getId());
            int novoEstoque = p.getEstoque() - item.getQuantidade();
            p.setEstoque(novoEstoque);

            // Atualiza no banco
            produtoRepository.atualizarProduto(p.getId(), p);
        }
    }

    public List<Pedido> listarPedidosDoCliente(long clienteId) {
        return pedidoRepository.buscarPedidosPorCliente(clienteId);
    }
}