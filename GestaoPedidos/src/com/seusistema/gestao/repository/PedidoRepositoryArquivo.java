package com.seusistema.gestao.repository;

// Importando GSON
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

// Importando nosso modelo
import com.seusistema.gestao.modelo.Pedido; // mudado

// Imports de I/O
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

// Imports de Coleções
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// nome da classe e contrato MUDADOS
// temos o Repositório de Pedidos em Arquivo JSON agora
public class PedidoRepositoryArquivo implements PedidoRepository {

    // filename MUDADO
    private static final String FILENAME = "pedidos.json";
    
    // cache em memória MUDADO
    private Map<Integer, Pedido> pedidos; 
    
    // contador de ID
    private int proximoId;
    
    // instância do GSON
    private final Gson gson;

    // CONSTRUTOR MUDADO

    //lógica do construtor é idêntica carregar do arquivo
    // e calcular o próximo ID baseado no que foi lido
    public PedidoRepositoryArquivo() {
        this.gson = new GsonBuilder().setPrettyPrinting().create(); 
        
        // Nomes de variáveis MUDADOS
        this.pedidos = carregarDoArquivo();
        
        if (this.pedidos.isEmpty()) {
            this.proximoId = 1;
        } else {
            // Nome da variável MUDADO
            this.proximoId = Collections.max(this.pedidos.keySet()) + 1;
        }
        // Log MUDADO
        System.out.println("[RepoArquivoPedido] Pedidos carregados. Próximo ID: " + this.proximoId);
    }

    // --- MÉTODOS PRIVADOS ---

    // Tipo de retorno MUDADO
    //o método que lê o .json e retorna o mapa
    private Map<Integer, Pedido> carregarDoArquivo() {
        try (Reader reader = new FileReader(FILENAME)) {
            
            //só precisamos dizer a ele Você vai ler um Mapa de Pedidos
            Type tipoDoMapa = new TypeToken<HashMap<Integer, Pedido>>(){}.getType();
            
            // 'gson.fromJson' cuida de toda a complexidade  recria o Pedido e
            // automaticamente recria o Cliente e a Lista de Itens
            // que estão dentro dele não precisamos de lógica extra
            Map<Integer, Pedido> mapaCarregado = gson.fromJson(reader, tipoDoMapa);
            
            if (mapaCarregado == null) {
                return new HashMap<>();
            }
            return mapaCarregado;
            
        } catch (FileNotFoundException e) {
            // Log MUDADO
            System.out.println("[RepoArquivoPedido] " + FILENAME + " não encontrado. Criando um novo.");
            return new HashMap<>();
        } catch (IOException e) {
            System.err.println("Erro grave ao carregar " + FILENAME);
            e.printStackTrace();
            return new HashMap<>();
        }
    }
    //O mesmo vale para salvar 'gson.toJson(pedidos)' já entende a estrutura complexa
    // e aninhada do Pedido e salva tudo corretamente
    private void persistirNoArquivo() {
        try (Writer writer = new FileWriter(FILENAME)) {
        
            gson.toJson(this.pedidos, writer);
        } catch (IOException e) {
            System.err.println("Erro grave ao salvar " + FILENAME);
            e.printStackTrace();
        }
    }

    // --- MÉTODOS DA INTERFACE ---
    // Aqui cumprimos o contrato do ID

    @Override
    public int getProximoId() {
        return proximoId++;
    }

    //os métodos'salvar', 'buscarPorId' e 'listarTodos' cumprem
    // o contrato, lendo e escrevendo no cache
    @Override
    public void salvar(Pedido pedido) {

        pedidos.put(pedido.getId(), pedido);
        persistirNoArquivo();
        System.out.println("[RepoArquivoPedido] Pedido ID " + pedido.getId() + " salvo em " + FILENAME);
    }
   
    @Override
    public Pedido buscarPorId(int id) {
        return pedidos.get(id);
    }

    
    @Override
    public List<Pedido> listarTodos() {
        return new ArrayList<>(pedidos.values());
    }
}