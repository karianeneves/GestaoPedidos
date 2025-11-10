# Projeto Final – Sistema de Gestão de Pedidos

Este projeto é uma aplicação de console em Java para gerenciar clientes, produtos e pedidos, com foco em aplicar conceitos avançados de POO, princípios SOLID e processamento assíncrono.

---

## Como Compilar e Executar

### Opção 1: Via VS Code (Recomendado)
1.  Verifique se o "Extension Pack for Java" está instalado.
2.  Abra o arquivo `App.java` (localizado em `src/com/seusistema/gestao/app/`).
3.  Clique no botão "Run" (ou "Executar") que aparece acima do método `main`.

### Opção 2: Via Terminal (Manual)
1.  Navegue até a pasta raiz do projeto.
2.  Compile todo o código-fonte para a pasta `bin` (crie-a se não existir):
    ```bash
    # (No Windows - Use ; para separar o classpath)
    javac -d bin -cp "src;lib/*" src/com/seusistema/gestao/app/*.java src/com/seusistema/gestao/excecoes/*.java src/com/seusistema/gestao/modelo/*.java src/com/seusistema/gestao/processing/*.java src/com/seusistema/gestao/repository/*.java src/com/seusistema/gestao/service/*.java
    
    # (No Linux/Mac - Use : para separar o classpath)
    javac -d bin -cp "src:lib/*" $(find src -name "*.java")
    ```
3.  Execute a classe principal:
    ```bash
    # (No Windows)
    java -cp "bin;lib/*" com.seusistema.gestao.app.App
    
    # (No Linux/Mac)
    java -cp "bin:lib/*" com.seusistema.gestao.app.App
    ```

---

## Como o Projeto Foi Feito

Aqui explicamos onde aplicamos os requisitos do projeto, como POO, SOLID e as Threads.

### 1. Fundamentos de POO (Programação Orientada a Objetos)

* **Classes e Objetos:** Usamos classes como `Cliente`, `Produto` e `Pedido` para ser a "planta" dos nossos dados.
* **Encapsulamento:** Esta foi uma regra importante. Todos os atributos (como `nome` ou `preco`) são `private`. O único jeito de criar um `Cliente`, por exemplo, é pelo `construtor`.
* **Validação no Construtor:** Em vez de usar `setters` (como `setEmail()`), nós colocamos as validações (como checar se o email tem "@" ou se o preço é positivo) direto no `construtor`. Se a regra for quebrada, a classe lança uma de nossas exceções e o objeto nem é criado. Isso garante que um objeto "doente" (com dados errados) nunca exista.
* **Herança:** Usamos herança para criar nossas próprias exceções. As classes `DadosInvalidosException` e `PrecoInvalidoException` **herdam** (`extends`) da classe `RuntimeException` do Java.
* **Interfaces (Contratos):** Usamos **Interfaces** (como `ClienteRepository`) para criar um "contrato". O contrato define *o que* uma classe de dados deve fazer (ex: `salvar()`, `buscarPorId()`), mas não *como* ela deve fazer.
* **Polimorfismo:** Este foi o conceito mais importante para o Bônus. Nossa classe de lógica (`PedidoService`) não "conhece" a classe `ClienteRepositoryMemoria` ou `ClienteRepositoryArquivo`. Ela só "conhece" a **Interface** `ClienteRepository`. Isso é Polimorfismo: nos permitiu "plugar" a implementação de Arquivo (o Bônus) no lugar da de Memória, e o `Service` continuou funcionando sem mudar uma linha de código.

### 2. Boas Práticas (SOLID)

Nós focamos em dois princípios SOLID que fizeram a maior diferença:

* **'S' - Responsabilidade Única:** Nós separamos as tarefas. Isso evitou que o `App.java` virasse um arquivo gigante e confuso.
    * `App.java`: Só cuida do menu e de falar com o usuário.
    * `ClienteService.java`: Só cuida da lógica de negócio (as "regras").
    * `ClienteRepository.java`: (Interface) Define o contrato de salvar e buscar os dados.
    * `ProcessadorPedidos.java`: Só cuida da fila de processamento.

* **'D' - Inversão de Dependência:** Este é o "D" do SOLID e funciona junto com o Polimorfismo.
    * Nossas classes de `Service` (a lógica) dependem das **Interfaces** (os contratos), e não das classes de `...Memoria` ou `...Arquivo` (as implementações).
    * No `main` do `App.java`, nós "injetamos" (passamos) a implementação que queríamos usar (Memória ou Arquivo) para dentro do `Service`.

### 3. Detalhes Técnicos: Processamento Assíncrono (Threads)

Este era o requisito obrigatório mais complexo. O menu não podia travar enquanto um pedido era processado. Nossa Solução: Usamos um padrão de "Produtor-Consumidor".

* **O Produtor (Menu):** Quando o usuário cria um pedido, o `PedidoService` (o "Produtor") salva o pedido com o status `FILA` e o coloca em uma Fila.
* **O Consumidor (Thread Separada):** Criamos a classe `ProcessadorPedidos`, que roda em uma `Thread` separada. Ele é o "Consumidor" e fica checando a fila em um loop.
* **Processamento:** Quando ele acha um pedido, ele muda o status para `PROCESSANDO`, pausa por 5 segundos (`Thread.sleep(5000)`) para simular o trabalho, e depois muda para `FINALIZADO`.
* **Resultado:** Durante esses 5 segundos, o Menu (a Thread principal) continua 100% responsivo, pois o trabalho pesado está acontecendo em outra Thread.
* **Segurança:** Para evitar bugs de duas threads mexendo na mesma lista ao mesmo tempo, usamos a `ConcurrentLinkedQueue`, que é uma Fila especial do Java já pronta e segura para isso.

### Bônus: Persistência em Arquivo (JSON)

* Implementamos o bônus de salvar e carregar dados em arquivos JSON.
* Para isso, usamos a biblioteca `Gson` (do Google) para converter nossos objetos Java em texto JSON e vice-versa.
* Graças à nossa arquitetura SOLID (usando Interfaces), para "ligar" o bônus, só precisamos trocar **3 linhas** no `App.java` (onde trocamos `new ...Memoria()` por `new ...Arquivo()`).

---

## Integrantes do Grupo

* **Kariane Ferreira Neves**
    * Foco na lógica de negócios principal (classes `Service`).
    * Implementação do processamento assíncrono (a fila e a `Thread`).
    * Desenvolvimento do bônus de persistência em arquivos JSON (`...ArquivoRepository`).

* **Alana Chorobura**
    * Implementação da camada de persistência inicial em memória (`...MemoriaRepository`).
    * Elaboração da documentação técnica final (`README.md`).

* **Matheus Gomes**
    * Modelagem inicial dos dados (classes `Cliente`, `Produto`, `Pedido`).
    * Criação dos `Enums` (Categoria, Status) e das Exceções customizadas.
    * Implementação das validações de dados (ex: preço positivo, email com @).

* **Pedro Bryk**
    * Desenvolvimento do menu e da interface do console (`App.java`).
    * Implementação da leitura de dados do usuário (`Scanner`).
    * Realização dos testes de entrada e de funcionamento do menu.

---

## Referências

Tutoriais da Comunidade

* **YouTube:** Foi nossa principal fonte para ver as coisas *funcionando*. Vimos muitos tutoriais para entender visualmente:
    * Como "startar" uma `Thread`.
    * Como o `Thread.sleep()` funcionava sem travar o menu.
    * Exemplos práticos de como o Gson salvava um objeto em um arquivo.
* **GeeksforGeeks e W3Schools:** Para dúvidas rápidas de sintaxe (ex: "como ler um arquivo JSON em Java" ou "como usar um `Map`").