package com.seusistema.gestao.app;

// modelos para mostrar dados
import com.seusistema.gestao.modelo.Cliente;
import com.seusistema.gestao.modelo.Produto;
import com.seusistema.gestao.modelo.Pedido;
import com.seusistema.gestao.modelo.CategoriaProduto; // cadastro de produto
import com.seusistema.gestao.processing.ProcessadorPedidos;
import com.seusistema.gestao.repository.ClienteRepository;
import com.seusistema.gestao.repository.ClienteRepositoryArquivo;

// Repositorios para Implementações
import com.seusistema.gestao.repository.PedidoRepository;
import com.seusistema.gestao.repository.PedidoRepositoryArquivo;

import com.seusistema.gestao.repository.ProdutoRepository;
import com.seusistema.gestao.repository.ProdutoRepositoryArquivo;
// serviços as fachadas de logica
import com.seusistema.gestao.service.ClienteService;
import com.seusistema.gestao.service.ProdutoService;
import com.seusistema.gestao.service.PedidoService;

// exceções Para o try-catch 
import com.seusistema.gestao.excecoes.DadosInvalidosException;
import com.seusistema.gestao.excecoes.PrecoInvalidoException;

// Utilitários do Java
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class App {

// classe App guarda os serviços, que são as classes de lógica..
    private final ClienteService clienteService;
private final ProdutoService produtoService;
private final PedidoService pedidoService;
private final Scanner scanner;

// no construtor, nos recebemos os serviços prontos. Isso é Injeção de Dependência
public App(ClienteService clienteService, ProdutoService produtoService, PedidoService pedidoService) {
    this.clienteService = clienteService;
    this.produtoService = produtoService;
    this.pedidoService = pedidoService;
    this.scanner = new Scanner(System.in);
}

  // Este é o cérebro da arquitetura. É aqui que provamos o SOLID.
    public static void main(String[] args) {
        
        // mudamos 3 linhas aqui para ligar o Bônus de persistência
        
        ClienteRepository clienteRepo = new ClienteRepositoryArquivo();
       
        ProdutoRepository produtoRepo = new ProdutoRepositoryArquivo();
        
        PedidoRepository pedidoRepo = new PedidoRepositoryArquivo();

    // fila compartilhada
    Queue<Pedido> filaDePedidos = new ConcurrentLinkedQueue<>();

    // Criar os serviços, injetando as dependências
    ClienteService clienteSvc = new ClienteService(clienteRepo);
    ProdutoService produtoSvc = new ProdutoService(produtoRepo);
    PedidoService pedidoSvc = new PedidoService(pedidoRepo, clienteRepo, produtoRepo, filaDePedidos);

    // Iniciar processador
    System.out.println("[MAIN] Iniciando a thread do processador de pedidos...");
    ProcessadorPedidos processador = new ProcessadorPedidos(filaDePedidos);
    Thread threadDoProcessador = new Thread(processador);
    threadDoProcessador.setDaemon(true); 
    threadDoProcessador.start(); // liga a tread

    // inicia a aplicação (Menu)
    App app = new App(clienteSvc, produtoSvc, pedidoSvc);
    app.run();
}

    // loop principal da aplicação mostra o menu
    public void run() {
        System.out.println("--- BEM-VINDO AO SISTEMA DE GESTÃO DE PEDIDOS ---");

        while (true) {
            exibirMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    cadastrarCliente();
                    break;
                case 2:
                    listarClientes();
                    break;
                case 3:
                    cadastrarProduto();
                    break;
                case 4:
                    listarProdutos();
                    break;
                case 5:
                    criarPedido();
                    break;
                case 6:
                    listarPedidos();
                    break;
                case 0:
                    System.out.println("Obrigado por usar o sistema. Até logo!");
                    scanner.close();
                    return; // sai do método run() e encerra o programa
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            // pequena pausa para o usuario ler a saída
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
    }

    // este é o método simples que so imprime o menu de texto 
    private void exibirMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Cadastrar Cliente");
        System.out.println("2. Listar Clientes");
        System.out.println("3. Cadastrar Produto");
        System.out.println("4. Listar Produtos");
        System.out.println("5. Criar Novo Pedido");
        System.out.println("6. Listar Pedidos");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }
    
    // Este é o ponto de QA (qualidade) para garantir a qualidade tratamos os erros da entrada..
    private int lerOpcao() {
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpa o Enter
            return opcao;
        } catch (InputMismatchException e) { //se o usuário digitar letras, o catch pega o erro
    // limpa o scanner e avisa o usuario sem quebrar o programa
            System.out.println("Erro: Por favor, digite apenas números.");
            scanner.nextLine(); // limpa o buffer do scanner
            return -1; // retorna uma opção inválida 
        }
    }

    // método de cadastro de cliente

   private void cadastrarCliente() {
        System.out.println("--- Cadastro de Cliente ---");
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            // Aqui, naõ salva direto, chamamos o 'Serviço' que cuida da lógica.
            Cliente novoCliente = clienteService.cadastrarCliente(nome, email);
            System.out.println("Cliente cadastrado com SUCESSO!");
            System.out.println("ID: " + novoCliente.getId() + ", Nome: " + novoCliente.getNome());

        } catch (DadosInvalidosException e) {
            //se o 'serviço' der erro ex: email inválido
            // o 'catch' mostra o erro sem travar o app
            System.out.println("\nERRO AO CADASTRAR: " + e.getMessage());
        } catch (Exception e) {
            // Captura erros inesperados
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
        }
    }

   private void listarClientes() {   //método para mostrar o Bônus funcionando
        System.out.println("--- Lista de Clientes ---");
        List<Cliente> clientes = clienteService.listarClientes();

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            for (Cliente c : clientes) {
                System.out.println("ID: " + c.getId() + " | Nome: " + c.getNome() + " | Email: " + c.getEmail());
            }
        }
    }

    private void cadastrarProduto() {
        System.out.println("--- Cadastro de Produto ---");
        try {
            System.out.print("Nome do Produto: ");
            String nome = scanner.nextLine();
            //ponto demo de validação
            System.out.print("Preço (ex: 29,90): ");
            double preco = scanner.nextDouble();
            scanner.nextLine(); // Limpa o buffer do nextDouble()
            
            System.out.println("Escolha a Categoria:");
            // Mostra as opções do nosso Enum criado
            for (CategoriaProduto cat : CategoriaProduto.values()) {
                // .ordinal() é a posição (0, 1, 2)
                System.out.println((cat.ordinal() + 1) + ". " + cat.name());
            }
            System.out.print("Opção: ");
            int catOpcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            // Converte o número (1, 2, 3) de volta para o Enum
            CategoriaProduto categoriaSelecionada;
            if (catOpcao > 0 && catOpcao <= CategoriaProduto.values().length) {
                // CategoriaProduto.values() é um array [ALIMENTOS, ELETRONICOS, LIVROS]
                categoriaSelecionada = CategoriaProduto.values()[catOpcao - 1];
            } else {
                throw new DadosInvalidosException("Categoria inválida.");
            }

            // Chama o serviço
            Produto novoProduto = produtoService.cadastrarProduto(nome, preco, categoriaSelecionada);
            System.out.println("Produto cadastrado com SUCESSO!");
            System.out.println("ID: " + novoProduto.getId() + ", Nome: " + novoProduto.getNome());

        } catch (DadosInvalidosException | PrecoInvalidoException e) {
            // exceção criada (PrecoInvalidoException) foi pega aqui e o programa me avisa do erro
             
            System.out.println("\nERRO AO CADASTRAR: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("\nERRO: O preço ou a opção deve ser um número.");
            scanner.nextLine(); // Limpa o buffer
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
        }
    }

   private void listarProdutos() {
        System.out.println("--- Lista de Produtos ---");
        List<Produto> produtos = produtoService.listarProdutos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            for (Produto p : produtos) {
                System.out.println(
                    "ID: " + p.getId() + 
                    " | Produto: " + p.getNome() + 
                    " | Preço: R$" + String.format("%.2f", p.getPreco()) + 
                    " | Categoria: " + p.getCategoria()
                );
            }
        }
    }

   private void criarPedido() { // método da demo principal
        System.out.println("--- Criação de Novo Pedido ---");
        try {
            // Selecionar o Cliente
            System.out.print("Digite o ID do Cliente: ");
            int clienteId = scanner.nextInt();
            scanner.nextLine(); // Limpa buffer
            
            // Valida o cliente ANTES de continuar
            Cliente cliente = clienteService.buscarClientePorId(clienteId);
            if (cliente == null) {
                System.out.println("ERRO: Cliente com ID " + clienteId + " não encontrado.");
                return;
            }
            System.out.println("Cliente selecionado: " + cliente.getNome());

            // Montar o "carrinho" 
            Map<Integer, Integer> itensDoPedido = new HashMap<>();
            
            while (true) {
                System.out.println("\n--- Adicionar Item ---");
                System.out.print("Digite o ID do Produto (ou 0 para finalizar): ");
                int produtoId = scanner.nextInt();
                scanner.nextLine(); // Limpa buffer
                
                if (produtoId == 0) {
                    break; // Sai do loop de adicionar itens
                }

                // Valida o produto
                Produto produto = produtoService.buscarProdutoPorId(produtoId);
                if (produto == null) {
                    System.out.println("ERRO: Produto com ID " + produtoId + " não encontrado. Tente novamente.");
                    continue; // Volta para o início do loop "while(true)"
                }
                
                System.out.print("Digite a Quantidade: ");
                int quantidade = scanner.nextInt();
                scanner.nextLine(); // Limpa buffer

                if (quantidade <= 0) {
                    System.out.println("ERRO: A quantidade deve ser positiva.");
                    continue;
                }
                
                // Adiciona ao carrinho
                itensDoPedido.put(produtoId, quantidade);
                System.out.println("Item adicionado: " + quantidade + "x " + produto.getNome());
            }

            // validar se o carrinho não está vazio
            if (itensDoPedido.isEmpty()) {
                System.out.println("Nenhum item adicionado. Pedido cancelado.");
                return;
            }

            // Chama o Serviço para criar o pedido
            Pedido novoPedido = pedidoService.criarNovoPedido(clienteId, itensDoPedido);

            System.out.println("\nPEDIDO CRIADO COM SUCESSO!");
            System.out.println("ID do Pedido: " + novoPedido.getId());
            System.out.println("Cliente: " + novoPedido.getCliente().getNome());
            System.out.println("Valor Total: R$" + String.format("%.2f", novoPedido.getValorTotal()));
            System.out.println("Status: " + novoPedido.getStatus());
            //menu volta imediatamente O status do pedido é FILA
            // Agora o Processador (a outra Thread) vai pegar esse pedido
           
        } catch (InputMismatchException e) {
            System.out.println("\nERRO: ID ou Quantidade deve ser um número.");
            scanner.nextLine(); // Limpa o buffer
        } catch (DadosInvalidosException e) {
            // Captura erros do PedidoService 
            System.out.println("\nERRO AO CRIAR PEDIDO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace(); // mostra mais detalhes do erro
        }
    }

    private void listarPedidos() {   //método que prova que a Thread funcionou
        System.out.println("--- Lista de Pedidos ---");
        List<Pedido> pedidos = pedidoService.listarPedidos();

        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
        } else {
            for (Pedido p : pedidos) { 
                System.out.println("-------------------------");
                System.out.println("ID Pedido: " + p.getId());
                System.out.println("Cliente: " + p.getCliente().getNome());
                //Ao listar podemos ver o status sendo atualizado em tempo real pela outra Thread
                // mudando de FILA para PROCESSANDO e FINALIZADO
                System.out.println("Status: " + p.getStatus()); 
                System.out.println("Total: R$" + String.format("%.2f", p.getValorTotal()));
                System.out.println("Itens (" + p.getItens().size() + "):");
                
            }
            System.out.println("-------------------------");
        }
    }
}
   