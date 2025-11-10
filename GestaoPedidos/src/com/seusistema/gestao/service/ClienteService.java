package com.seusistema.gestao.service;
import java.util.List;
import com.seusistema.gestao.modelo.Cliente;
import com.seusistema.gestao.repository.ClienteRepository;
// Importamos a exceção, pois o construtor do Cliente pode lançá-la
import com.seusistema.gestao.excecoes.DadosInvalidosException; 

public class ClienteService {

    //ponto principal do SOLID Serviço (lógica) não conhece o banco de dados
    // Ele só conhece o CONTRATO (a Interface) do Repositório
    private ClienteRepository clienteRepo;
    
   

    // CONSTRUTOR (Injeção de Dependência)
    // service recebe o Repositório pronto pelo construtor
    // ele não se importa se é a versão de 'Memória' ou 'Arquivo' o que prova o Polimorfismo
    
    public ClienteService(ClienteRepository clienteRepo) {
        this.clienteRepo = clienteRepo;
    }

    // MÉTODO DE NEGÓCIO:
    //    Este método contém a *lógica* de cadastrar um cliente.
    public Cliente cadastrarCliente(String nome, String email) throws DadosInvalidosException {
        // Lógica 1: Gerar um ID
        int id = clienteRepo.getProximoId();
        
        // Lógica 2: Criar o objeto (aqui ele valida os dados,
        // lançando DadosInvalidosException se algo estiver errado)
        Cliente novoCliente = new Cliente(id, nome, email);
        
        // Lógica 3: Salvar no repositório
        clienteRepo.salvar(novoCliente);
        
        return novoCliente;
    }

    // métodos de listar são simples Service só repete o chamado para o Repositório
    public List<Cliente> listarClientes() {
        return clienteRepo.listarTodos();
    }
    
    public Cliente buscarClientePorId(int id) {
        return clienteRepo.buscarPorId(id);
    }
}