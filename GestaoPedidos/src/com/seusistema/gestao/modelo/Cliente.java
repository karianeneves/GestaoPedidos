package com.seusistema.gestao.modelo;

import com.seusistema.gestao.excecoes.DadosInvalidosException;

public class Cliente {    //encapsulamento.
private int id;
private String nome;
private String email;

// construtor 
// única porta de entrada valida todas as regras de negócio antes do objeto surgir
   
    public Cliente(int id, String nome, String email){

        if (nome == null || nome.trim().isEmpty()){    //validação de nome
            throw new DadosInvalidosException("O nome do cliente não pode estar vazio.");
        }

        if (email == null || email.trim().isEmpty()) {  //validação de email
                throw new DadosInvalidosException("O email do cliente não pode estar vazio.");
        }

        if (!email.contains("@")){   //validação do formato email
            throw new DadosInvalidosException("Formato de email inválido. Deve conter '@'.;");  
        }
    // se todas as validações passarem, atribui os valores aos atributos
        this.id = id;
        this.nome = nome;
        this.email = email;
    }
    // getters (sem setters para manter a integridade dos dados após a criação)
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

}
