package org.ecommerce.modules.carrinho;

import org.ecommerce.modules.carrinho.repository.CarrinhoRepository;

import java.util.List;

public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository) {
        this.carrinhoRepository = carrinhoRepository;
    }

    public List<Carrinho> buscarTodosCarrinhos() {
        return carrinhoRepository.buscarTodosCarrinhos();
    }

    public Carrinho buscarCarrinhoPorId(long id) {
        return carrinhoRepository.buscarCarrinhoCompletoPorId(id);
    }

    public Carrinho buscarCarrinhoPorClienteId(long clienteId) {
        Carrinho carrinho = carrinhoRepository.buscarCarrinhoCompletoPorClienteId(clienteId);
        if (carrinho == null) {
            carrinho = new Carrinho(0, clienteId);
            carrinhoRepository.criarCarrinho(carrinho);
        }
        return carrinho;
    }

    public void criarCarrinho(Carrinho carrinho) {
        carrinhoRepository.criarCarrinho(carrinho);
    }

    public void adicionarItem(long carrinhoId, long produtoId, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero");
        }
        carrinhoRepository.adicionarItem(carrinhoId, produtoId, quantidade);
    }

    public void removerItem(long carrinhoId, long produtoId) {
        carrinhoRepository.removerItem(carrinhoId, produtoId);
    }

    public void atualizarQuantidadeItem(long carrinhoId, long produtoId, int quantidade) {
        carrinhoRepository.atualizarQuantidadeItem(carrinhoId, produtoId, quantidade);
    }

    public void limparCarrinho(long carrinhoId) {
        carrinhoRepository.limparCarrinho(carrinhoId);
    }

    public void deletarCarrinho(long id) {
        carrinhoRepository.deletarCarrinho(id);
    }
}
