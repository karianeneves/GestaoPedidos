package com.seusistema.gestao.repository;

import com.seusistema.gestao.modelo.Produto;
import java.util.List;

public interface ProdutoRepository {
    
    void salvar(Produto produto);

    Produto buscarPorId(int id);

    List<Produto> listarTodos();
    
    int getProximoId();
}
