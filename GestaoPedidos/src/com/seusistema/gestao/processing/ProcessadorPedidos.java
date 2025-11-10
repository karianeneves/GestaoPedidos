package com.seusistema.gestao.processing;

import com.seusistema.gestao.modelo.Pedido;
import com.seusistema.gestao.modelo.StatusPedido;
import java.util.Queue;

// Para criar uma Thread, primeiro criamos a TAREFA
// 'implements Runnable' esta classe é uma tarefa
// que pode ser executada por um 'trabalhador' (a Thread)
public class ProcessadorPedidos implements Runnable {

    // Esta é a ponte entre o Menu e o processador
    // É a mesma instância da `ConcurrentLinkedQueue` que criamos
    // no App.java É assim que eles se comunicam
    private Queue<Pedido> filaDePedidos;
    
    //Flag para controlar o loop (opcional, mas boa prática)
    private boolean rodando = true;

    //No construtor, nós recebemos a fila
    // (Injeção de Dependência)
    public ProcessadorPedidos(Queue<Pedido> filaDePedidos) {
        this.filaDePedidos = filaDePedidos;
    }

    // o coração da thread
    //   é o método será chamado Quando a thread dar um start fica rodando em loop infinito
    @Override
    public void run() {
        System.out.println("[PROCESSADOR]  Aguardando pedidos...");

        while (rodando) {
            try {
                //o Processador tenta
                // pegar (.poll()) um pedido da fila
                // poll() é thread-safe ele pega e remove ao mesmo tempo
                // sem risco de bugs
                Pedido pedido = filaDePedidos.poll();

                if (pedido != null) {
                    // aqui ela pega um pedido e começa a processar
                    System.out.println("[PROCESSADOR]  Iniciando Pedido ID: " + pedido.getId());
                    
                    // muda o status o usuário verá isso no menu "Listar".
                    pedido.setStatus(StatusPedido.PROCESSANDO);

                    //aqui está o requisito de "demorar 5 segundos para processar"
                    // isso pausa a thread do processador por 5 segundos
                    // o menu (a Thread principal) continua 100% responsivo
                        Thread.sleep(5000); // 5 segundos
                    pedido.setStatus(StatusPedido.FINALIZADO); // muda o status para FINALIZADO
                    System.out.println("[PROCESSADOR]  Pedido ID: " + pedido.getId() + " FINALIZADO!");

                } else {
                    //se a fila está vazia, não fazemos nada
                    // mas para o loop não ficar rodando loucamente
                    // consumindo CPU, vamos
                    // descansar por 1 segundo antes de checar a fila de novo."
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                // InterruptedException é lançada se a thread for "acordada"
                // ou interrompida.
                System.out.println("[PROCESSADOR]  Fui interrompido!");
                this.rodando = false;
            }
        }
    }
}