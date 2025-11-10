package com.seusistema.gestao.excecoes;

public class DadosInvalidosException extends RuntimeException {

    public DadosInvalidosException(String mensagem) {
        // "super(mensagem)" é o que passa a mensagem de erro (ex: "O nome não pode ser vazio")
        // para a classe "pai" (RuntimeException), para que ela possa ser mostrada no log.
        super(mensagem);
    }
}

