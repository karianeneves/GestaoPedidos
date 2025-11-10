package com.seusistema.gestao.repository;

import com.seusistema.gestao.modelo.Pedido;
import java.util.List;
// o Pedido também tem seu próprio Contrato (Interface)
public interface PedidoRepository {
    
    void salvar(Pedido pedido);

    Pedido buscarPorId(int id);

    List<Pedido> listarTodos();
// PedidoService usa este contrato e não se importa se os dados estão vindo
// da memória ou de um arquivo JSON
    int getProximoId();
}