package com.seusistema.gestao.repository;

// importando o GSON que adicionamos
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

//Importando nosso modelo
import com.seusistema.gestao.modelo.Cliente;

// Imports do Java para lidar com Arquivos (I/O)
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

// Imports do Java para Coleções
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//  implementação de Arquivo para o repositório de Cliente
// Agora esta classe "sabe COMO" salvar em arquivo JSON
public class ClienteRepositoryArquivo implements ClienteRepository {

    // constante para o nome do arquivo
    private static final String FILENAME = "clientes.json";
    
    //Cache em memória. Não vamos ler o arquivo toda hora.
    // Nós lemos 1 vez (no início) e salvamos 1 vez (quando muda).
    private Map<Integer, Cliente> clientes; 
    
    //contador de ID que agora mora aqui
    private int proximoId;
    
    // instância do GSON que vai fazer a mágica
    private final Gson gson;

    // CONSTRUTOR: Aqui a mágica de "carregar" acontece (bonus)
    public ClienteRepositoryArquivo() {
        // Inicializa o Gson com ".setPrettyPrinting()"
        // Isso faz o arquivo .json ficar formatado e legível
        this.gson = new GsonBuilder().setPrettyPrinting().create(); 
        
        // Tenta carregar os clientes do arquivo para o cache
        this.clientes = carregarDoArquivo();
        
        // logica do id
        // Se o mapa (que veio do arquivo) está vazio o ID começa em 1
        if (this.clientes.isEmpty()) {
            this.proximoId = 1;
        } else {
            // Se não, ache o MAIOR ID que já existe no arquivo
            // e defina o próximo ID como (MAIOR + 1)
            this.proximoId = Collections.max(this.clientes.keySet()) + 1;
        }
        System.out.println("[RepoArquivoCliente] Clientes carregados. Próximo ID: " + this.proximoId);
    }

    // metodos privados para lidar com arquivo
    //o método que lê o .json e retorna o mapa
    private Map<Integer, Cliente> carregarDoArquivo() {
        try (Reader reader = new FileReader(FILENAME)) {
            // e este 'TypeToken' é um 'truque'do Gson para ele entender que queremos
            // um 'Mapa de Clientes'e não um objeto simples
            Type tipoDoMapa = new TypeToken<HashMap<Integer, Cliente>>(){}.getType();
            
            Map<Integer, Cliente> mapaCarregado = gson.fromJson(reader, tipoDoMapa);
            
            // Se o arquivo existir mas estiver vazio (null)
            if (mapaCarregado == null) {
                return new HashMap<>();
            }
            return mapaCarregado;
            
        } catch (FileNotFoundException e) {
            //se o arquivo não existir na primeira vez, este catch o cria
            // sem quebrar o programa
            System.out.println("[RepoArquivoCliente] " + FILENAME + " não encontrado. Criando um novo.");
            return new HashMap<>();
        } catch (IOException e) {
            System.err.println("Erro grave ao carregar " + FILENAME);
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void persistirNoArquivo() {
        // Este método salva o cache (o Mapa inteiro) de volta no arquivo .json
        try (Writer writer = new FileWriter(FILENAME)) {
            gson.toJson(this.clientes, writer);
        } catch (IOException e) {
            System.err.println("Erro grave ao salvar " + FILENAME);
            e.printStackTrace();
        }
    }

    // metodos da interface
    //Aqui cumprimos o contrato do ID
    @Override
    public int getProximoId() {
        return proximoId++;
    }
    //nosso 'salvar' é um processo de 2 passos:
    @Override
    public void salvar(Cliente cliente) {
        // 1- Salva o cliente no cache em memória
        clientes.put(cliente.getId(), cliente);
        
        // 2- Persiste (salva) o cache *inteiro* no arquivo .json
        persistirNoArquivo();
        
        System.out.println("[RepoArquivoCliente] Cliente ID " + cliente.getId() + " salvo em " + FILENAME);
    }

    @Override
    public Cliente buscarPorId(int id) {
        // A busca agora é super rápida, pois lê do cache
        return clientes.get(id);
    }

    @Override
    public List<Cliente> listarTodos() {
        // A listagem também é super rápida
        return new ArrayList<>(clientes.values());
    }
}