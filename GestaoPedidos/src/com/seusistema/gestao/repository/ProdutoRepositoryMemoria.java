package com.seusistema.gestao.repository;

import com.seusistema.gestao.modelo.Produto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// o Produto também tem seu próprio Contrato (Interface)
public class ProdutoRepositoryMemoria implements ProdutoRepository {
//A diferença é aqui em vez de um arquivo .json, ela usa um HashMap para guardar
    // os dados É um banco de dados temporário em memória
    private Map<Integer, Produto> produtos = new HashMap<>();
    private int proximoId = 1;
    
    @Override
    public void salvar(Produto produto) { 
        produtos.put(produto.getId(), produto);
    }

    @Override
    public Produto buscarPorId(int id) {
        return produtos.get(id);
    }

    @Override
    public List<Produto> listarTodos() {
        return new ArrayList<>(produtos.values());
    }
    @Override
    public int getProximoId() {
        return proximoId++; // Retorna o ID atual e incrementa
    }
}