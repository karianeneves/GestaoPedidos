package com.seusistema.gestao.modelo;

import com.seusistema.gestao.excecoes.DadosInvalidosException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections; 

public class Pedido {

    private int id;
    private Cliente cliente;
    private StatusPedido status;
    private double valorTotal;
    
    //Isso é Composição Um Pedido tem muitos Itens
    // entao usamos uma Lista para guardar os vários ItemPedido
    private List<ItemPedido> itens;

    public Pedido(int id, Cliente cliente) {
        if (cliente == null) {
            throw new DadosInvalidosException("O cliente do pedido não pode estar nulo.");
        }
        
        this.id = id;
        this.cliente = cliente;
        
        // O Construtor define os valores padrão seguros
        this.status = StatusPedido.ABERTO; // Todo pedido novo começa como ABERTO
        this.itens = new ArrayList<>();      // Inicia a lista vazia
        this.valorTotal = 0.0;
    }


    // metodos de comportamento (Poo e Calisthenics)

                   //object Calisthenics
    //Nós não temos um setItens ou setValorTotal
    // Temos  método de *comportamento* chamado adicionarItem
    public void adicionarItem(ItemPedido item) {
        if (item != null) {
            this.itens.add(item);
            // Quando o App chama `adicionarItem`
            // o próprio objeto chama seu método *privado* para recalcular o total
            // a lógica fica segura e centralizada aqui, e não espalhada no App
            this.recalcularValorTotal();
        }
    }
                    //encapsulamento
    // Método privado. Ninguém de fora precisa saber "como"
    // o valor total é calculado. (Encapsulamento)
    private void recalcularValorTotal() {
        double total = 0.0;
        for (ItemPedido item : this.itens) {
            total += item.getValorTotalItem();
        }
        this.valorTotal = total;
        
        // Alternativa moderna com Streams:
        // this.valorTotal = this.itens.stream()
        //                           .mapToDouble(ItemPedido::getValorTotalItem)
        //                           .sum();
    }
    
   //o *status* é a única coisa"
    // que precisa ser mudada por outra classe (o `ProcessadorPedidos`)
    // quando ele muda o pedido de FILA para PROCESSANDO
    // Por isso, ele tem um "setter" 
    
    public void setStatus(StatusPedido novoStatus) {
        this.status = novoStatus;
    }

    // getters
    
    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public double getValorTotal() {
        // O valor total é apenas para consulta, ele é
        // calculado internamente pelo método adicionarItem.
        return valorTotal;
    }

    // Getter  para a lista de itens
    public List<ItemPedido> getItens() {

        // programaçao defensiva
        //Retorna uma cópia "apenas leitura" da lista
        // Impede que alguém de fora faça:
        //    meuPedido.getItens().add(outroItem);
        // Isso furaria o encapsulamento e não recalcularia o valor total.
        return Collections.unmodifiableList(this.itens);
    }
}
