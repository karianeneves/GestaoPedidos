package com.seusistema.gestao.service;

import com.seusistema.gestao.modelo.*;
import com.seusistema.gestao.repository.*;
import com.seusistema.gestao.excecoes.DadosInvalidosException;
import java.util.List;
import java.util.Map; // Para receber os itens do pedido
import java.util.Queue;

public class PedidoService {

    //PedidoService precisa de ferramentas (dependências) para trabalharele depende das
    //interfaces e não das classes de arquivo ou memória.
    private PedidoRepository pedidoRepo;
    private ClienteRepository clienteRepo;
    private ProdutoRepository produtoRepo;
    //ele também depende da Fila que é a 'ponte' para a Thread do Processador
    private Queue<Pedido> filaDePedidos;
 

   
    


    // construtor que faz a Injeção de Dependência O main nos entrega todas as ferramentas prontas
    public PedidoService(PedidoRepository pedidoRepo, 
                         ClienteRepository clienteRepo, 
                         ProdutoRepository produtoRepo,
                     Queue<Pedido> filaDePedidos) { 
    this.pedidoRepo = pedidoRepo;
    this.clienteRepo = clienteRepo;
    this.produtoRepo = produtoRepo;
    this.filaDePedidos = filaDePedidos; 
}

    // método de negócio: criar um novo pedido
    public Pedido criarNovoPedido(int clienteId, Map<Integer, Integer> itensDoPedido) 
            throws DadosInvalidosException {
        
        // Lógica 1: Busca o cliente usando o ClienteRepository
        Cliente cliente = clienteRepo.buscarPorId(clienteId);
        if (cliente == null) {
            throw new DadosInvalidosException("Cliente com ID " + clienteId + " não encontrado.");
        }

        // Lógica 2: Pede o ID para o PedidoRepository e cria o pedido
        int idPedido = pedidoRepo.getProximoId();
        Pedido novoPedido = new Pedido(idPedido, cliente);
        
        // Lógica 3 Ele monta o pedido buscando cada produto e criando os Itens
        for (Map.Entry<Integer, Integer> entry : itensDoPedido.entrySet()) {
            int produtoId = entry.getKey();
            int quantidade = entry.getValue();

            Produto produto = produtoRepo.buscarPorId(produtoId);
            if (produto == null) {
                throw new DadosInvalidosException("Produto com ID " + produtoId + " não encontrado.");
            }

            ItemPedido item = new ItemPedido(produto, quantidade);
            
            //O Service não calcula o total manda o objeto 'Pedido' se recalcular 
            //sozinho usando o método 'adicionarItem()
            novoPedido.adicionarItem(item);
        }

        // Lógica 4: Define o status como FILA
        novoPedido.setStatus(StatusPedido.FILA); 

        // Lógica 5: Salva o pedido completo no repositório (no arquivo .json)
        pedidoRepo.salvar(novoPedido);

        // Lógica 6 Adicionar na fila
        filaDePedidos.add(novoPedido);
        
        return novoPedido;
    }
    // métodos simples de listar e buscar pedidos
    public List<Pedido> listarPedidos() {
        return pedidoRepo.listarTodos();
    }
    
    public Pedido buscarPedidoPorId(int id) {
        return pedidoRepo.buscarPorId(id);
    }
}