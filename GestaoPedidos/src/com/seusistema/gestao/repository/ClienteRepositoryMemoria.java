package com.seusistema.gestao.repository;

import com.seusistema.gestao.modelo.Cliente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Esta é a nossa implementação de Memória a classe
// "assina o contrato" usando a palavra "implements"
public class ClienteRepositoryMemoria implements ClienteRepository {

    //A diferença é aqui em vez de um arquivo .json, ela usa um HashMap para guardar
    // os dados É um banco de dados temporário em memória
    private Map<Integer, Cliente> clientes = new HashMap<>();
    private int proximoId = 1;
    @Override 
    public void salvar(Cliente cliente) {
        // Simplesmente coloca o cliente no mapa, usando seu ID como chave.
        clientes.put(cliente.getId(), cliente);
    }

    @Override
    public Cliente buscarPorId(int id) {
        // Retorna o cliente cuja chave (ID) bate com o ID procurado.
        return clientes.get(id);
    }

    @Override
    public List<Cliente> listarTodos() {
        // Retorna uma nova lista contendo todos os VALORES do mapa.
        return new ArrayList<>(clientes.values());
    }
    //aqui ela cumpre o contrato do Id da forma mais simples começando em 1 e incrementando
    @Override
public int getProximoId() {
    return proximoId++; // Retorna o ID atual e incrementa
}
}
