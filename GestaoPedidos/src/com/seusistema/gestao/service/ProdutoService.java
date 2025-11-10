package com.seusistema.gestao.service;

import com.seusistema.gestao.modelo.Produto;
import com.seusistema.gestao.modelo.CategoriaProduto;
import com.seusistema.gestao.repository.ProdutoRepository;
import com.seusistema.gestao.excecoes.DadosInvalidosException;
import com.seusistema.gestao.excecoes.PrecoInvalidoException;
import java.util.List;

public class ProdutoService {
// ProdutoService usa o contrato (interface) 
    private ProdutoRepository produtoRepo;
   
// Injeção de dependência via construtor
    public ProdutoService(ProdutoRepository produtoRepo) {
        this.produtoRepo = produtoRepo;
    }
//método de negócio do Produto
    public Produto cadastrarProduto(String nome, double preco, CategoriaProduto categoria) 
            throws DadosInvalidosException, PrecoInvalidoException {
        
        //Pede o ID para o Repositório
        int id = produtoRepo.getProximoId();
        // Cria o objeto Produto (o construtor valida os dados)
        Produto novoProduto = new Produto(id, nome, preco, categoria);
        // Salva no repositório se validado
        produtoRepo.salvar(novoProduto);
        return novoProduto;
    }
    // métodos simples de listar e buscar produtos

    public List<Produto> listarProdutos() {
        return produtoRepo.listarTodos();
    }

    public Produto buscarProdutoPorId(int id) {
        return produtoRepo.buscarPorId(id);
    }
}
