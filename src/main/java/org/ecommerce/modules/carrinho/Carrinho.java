package org.ecommerce.modules.carrinho;

import org.ecommerce.modules.produto.Produto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Carrinho {
    private final List<ItemCarrinho> itens = new ArrayList<>();

    public void adicionar(Produto produto, int quantidade) {
        // Verifica se já tem esse produto no carrinho para não repetir a linha
        Optional<ItemCarrinho> existente = itens.stream()
                .filter(i -> i.getProduto().getId() == produto.getId())
                .findFirst();

        if (existente.isPresent()) {
            existente.get().adicionarQuantidade(quantidade);
        } else {
            itens.add(new ItemCarrinho(produto, quantidade));
        }
    }

    public void remover(int index) {
        if (index >= 0 && index < itens.size()) {
            itens.remove(index);
        }
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public double getValorTotal() {
        return itens.stream().mapToDouble(ItemCarrinho::getTotal).sum();
    }

    public void limpar() {
        itens.clear();
    }
}