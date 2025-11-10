package com.seusistema.gestao.modelo;

import com.seusistema.gestao.excecoes.DadosInvalidosException;

public class ItemPedido {

// um ItemPedido tem um Produto e tem uma Quantidade
    private Produto produto; 
    private int quantidade;  

    public ItemPedido(Produto produto, int quantidade) {  //Construtor  para validar os dados..
        if (produto == null) {
            throw new DadosInvalidosException("O produto do item não pode estar nulo.");
        }
        if (quantidade <= 0) {
            throw new DadosInvalidosException("A quantidade do item deve estar positiva.");
        }
        this.produto = produto;
        this.quantidade = quantidade;
    }

    
    
    public double getValorTotalItem() {   // Em vez de calcular isso fora
        // o próprio ItemPedido sabe calcular seu subtotal
        //isso é Encapsulamento de Comportamento lógica mora junto com os dados
        return produto.getPreco() * quantidade;
    }

  //getters
    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
