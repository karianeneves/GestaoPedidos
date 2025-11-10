package com.seusistema.gestao.repository;

import com.seusistema.gestao.modelo.Cliente;
import java.util.List;

// Esta Interface é o 'Contrato' da nossa arquitetura
//  define AS REGRAS (o que?) mas não COMO fazer
public interface ClienteRepository {
    
    //contrato promete que qualquer classe de repositório terá um método para 'salvar'
    void salvar(Cliente cliente);

    // um método para buscar um cliente pelo ID
    Cliente buscarPorId(int id);

    //um método para listar todos os clientes
    List<Cliente> listarTodos();

    //e um método para getProximoId
    // o Service não sabe se isso vem da memória ou de um arquivo JSON só confia no contrato
    // Isso é Inversão de Dependência
    int getProximoId();
}
