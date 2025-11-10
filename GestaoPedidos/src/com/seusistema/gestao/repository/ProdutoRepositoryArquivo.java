package com.seusistema.gestao.repository;

//Importando GSON
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

//Importando nosso modelo
import com.seusistema.gestao.modelo.Produto; // <-- MUDADO PARA PRODUTO

//Imports de I/O
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

//Imports de Coleções
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//classe implementa o Bônus para Produto ela assina o contrato 'ProdutoRepository
public class ProdutoRepositoryArquivo implements ProdutoRepository {

    // FILENAME MUDADO
    private static final String FILENAME = "produtos.json";
    
    // Cache em memória MUDADO
    private Map<Integer, Produto> produtos; 
    
    //  O contador de ID
    private int proximoId;
    
    // A instância do GSON
    private final Gson gson;

    // CONSTRUTOR MUDADO
    public ProdutoRepositoryArquivo() {
        this.gson = new GsonBuilder().setPrettyPrinting().create(); 
        
        // Nomes de variáveis MUDADOS
        this.produtos = carregarDoArquivo();
        
        if (this.produtos.isEmpty()) {
            this.proximoId = 1;
        } else {
            // Nome da variável MUDADO
            this.proximoId = Collections.max(this.produtos.keySet()) + 1;
        }
        // Log MUDADO
        System.out.println("[RepoArquivoProduto] Produtos carregados. Próximo ID: " + this.proximoId);
    }

    // MÉTODOS PRIVADOS

    // Tipo de retorno MUDAD
    //método de carregar usa o 'TypeToken' para entender que é um 'Mapa de Produtos
    private Map<Integer, Produto> carregarDoArquivo() {
        try (Reader reader = new FileReader(FILENAME)) {
            
            // Tipo do TypeToken MUDADO
            Type tipoDoMapa = new TypeToken<HashMap<Integer, Produto>>(){}.getType();
            
            // Tipo do mapa MUDADO
            Map<Integer, Produto> mapaCarregado = gson.fromJson(reader, tipoDoMapa);
            
            if (mapaCarregado == null) {
                return new HashMap<>();
            }
            return mapaCarregado;
            
        } catch (FileNotFoundException e) {
            // Log MUDADO
            System.out.println("[RepoArquivoProduto] " + FILENAME + " não encontrado. Criando um novo.");
            return new HashMap<>();
        } catch (IOException e) {
            System.err.println("Erro grave ao carregar " + FILENAME);
            e.printStackTrace();
            return new HashMap<>();
        }
    }
    // 'persistir' salva o cache de volta no arquivo .json.
    private void persistirNoArquivo() {
        try (Writer writer = new FileWriter(FILENAME)) {
            // Variável MUDADA
            gson.toJson(this.produtos, writer);
        } catch (IOException e) {
            System.err.println("Erro grave ao salvar " + FILENAME);
            e.printStackTrace();
        }
    }

    // metodos publicos

    @Override
    public int getProximoId() {
        return proximoId++;
    }

    // Assinatura do método MUDADA
    //salvar (no cache e no arquivo)
    @Override
    public void salvar(Produto produto) {
        // Variáveis MUDADAS
        produtos.put(produto.getId(), produto);
        persistirNoArquivo();
        
        // Log MUDADO
        System.out.println("[RepoArquivoProduto] Produto ID " + produto.getId() + " salvo em " + FILENAME);
    }

    // Assinatura do método MUDADA
    @Override
    public Produto buscarPorId(int id) { //do cache
        // Variável MUDADA
        return produtos.get(id);
    }

    // Assinatura do método MUDADA
    @Override
    public List<Produto> listarTodos() { //do cache
        // Variável MUDADA
        return new ArrayList<>(produtos.values());
    }
}