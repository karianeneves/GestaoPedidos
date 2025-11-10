package com.seusistema.gestao.repository;

import com.seusistema.gestao.modelo.Pedido;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoRepositoryMemoria implements PedidoRepository {


    //A diferença é aqui em vez de um arquivo .json, ela usa um HashMap para guardar
    // os dados É um banco de dados temporário em memória
    private Map<Integer, Pedido> pedidos = new HashMap<>();
    private int proximoId = 1;
    @Override
    public void salvar(Pedido pedido) {
        pedidos.put(pedido.getId(), pedido);
    }

    @Override
    public Pedido buscarPorId(int id) {
        return pedidos.get(id);
    }

    @Override
    public List<Pedido> listarTodos() {
        return new ArrayList<>(pedidos.values());
    }
    //aqui ela cumpre o contrato do Id da forma mais simples começando em 1 e incrementando
    @Override
    public int getProximoId() {
        return proximoId++;
    }
}