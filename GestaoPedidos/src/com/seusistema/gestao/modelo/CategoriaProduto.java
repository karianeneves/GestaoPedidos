package com.seusistema.gestao.modelo;
// CategoriaProduto é um Enum (tipo especial) que define as categorias permitidas para produtos
// criamos este Enum que só permite estes 3 valores
// É impossível cadastrar uma categoria errada, o que previne muitos bugs
public enum CategoriaProduto {

    ALIMENTOS,
    ELETRONICOS,
    LIVROS
    
//quiser adicionar uma nova categoria a gente só precisaria adicionar aqui nesta lista
}
