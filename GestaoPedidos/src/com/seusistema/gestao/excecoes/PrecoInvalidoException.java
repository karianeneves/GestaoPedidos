

package com.seusistema.gestao.excecoes;

public class PrecoInvalidoException extends RuntimeException {

    // construtor que recebe a mensagem de erro
    public PrecoInvalidoException(String mensagem) {
        // "super(mensagem)" envia a mensagem para a classe "pai" (RuntimeException)
        // para que ela possa ser exibida no log de erro.
        super(mensagem);
    }
}