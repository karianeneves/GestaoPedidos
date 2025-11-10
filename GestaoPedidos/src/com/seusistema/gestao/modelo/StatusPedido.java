package com.seusistema.gestao.modelo;
// StatusPedido é um Enum que define os possíveis status de um pedido
public enum StatusPedido { //Isso define o ciclo de vida de um pedido
ABERTO,
FILA,
PROCESSANDO,
FINALIZADO
//um Enum aqui foi crucial para a thread de processamento funcionar sem bugs
}
