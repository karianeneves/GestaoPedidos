package com.seusistema.gestao.modelo;

import com.seusistema.gestao.excecoes.DadosInvalidosException;
import com.seusistema.gestao.excecoes.PrecoInvalidoException;

public class Produto {
//encapsulamento
    private int id;
    private String nome;
    private double preco;
   
    private CategoriaProduto categoria;  //Note que a categoria não e uma String É o nosso Enum 'CategoriaProduto'
    // isso  força a usar ALIMENTOS, ELETRONICOS ou LIVROS, e previne bugs de digitação

    // Construtor
    public Produto(int id, String nome, double preco, CategoriaProduto categoria) {
        
        if (nome == null || nome.trim().isEmpty()) {
            throw new DadosInvalidosException("O nome do produto não pode estar vazio.");
        }

        // Regra de negócio preço ser for <= 0
        if (preco <= 0) {  
        //lançamos a exceção especifica 'PrecoInvalidoException' É por isso que o programa avisa o usuário
        // no menu quando ele digita o preço errado    
            throw new PrecoInvalidoException("O preço do produto deve estar positivo.");
        }
        // também validamos se o Enum não é nulo
        if (categoria == null) {
            throw new DadosInvalidosException("A categoria do produto não pode estar nula.");
        }

        // Atribui
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
    }

    // Getters para ler as informações
    //Isso garante que um produto nunca terá um preço negativo, pois a única
    // forma de definir o preço é pelo construtor que já valida
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public CategoriaProduto getCategoria() {
        return categoria;
    }
}
