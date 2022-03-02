## AED2-EP1-Arvore-B

![Exemplo de Árvore B - Fonte: Wikipédia](https://pt.wikipedia.org/wiki/%C3%81rvore_B#/media/Ficheiro:B-tree-definition.png)

<br>

---
### Estrutura das Classes

- **Main**
    - Classe que ~~(meio obviamente)~~ contém o método `main`, ou seja, o que é chamado ao se iniciar o programa, e com isso .
    
    <br>

    - Exemplo de análise:

        ![](img/conclusao.jpg)


- **Simulação**
    -  Responsável por executar uma simulação completa dos 2 algoritmos, dado o **tamanho**, uma **probabilidade** e um **custo máximo**. Isso é feito gerando inicialmente um Digrafo "normal" e rodando os algoritmos e depois é gerado um DAG *(Directed Acyclic Graphs - Digrafo Acíclico)* e rodado novamente os algoritmos. 
    Nesse processo também são calculados os tempos gastos para processamento (através do objeto *MedidorTempo*, conforme descrito abaixo), e por fim, são armazenados os resultados num arquivo **log** para análise posterior.

    <br>

    - Exemplo do arquivo de log (usado como base para gerar o gráfico acima):

        ![](img/log.jpg)

    <br>

- **ArvoreB**
    - Classe responsável por armazenar a estrutura do digrafo, ou seja, a **quantidade de vértices**, o **número de arcos** e o estado de conexão entre os vértices (**lista de adjacência**), representado por instâncias do *Link*, conforme descrito abaixo.

    <br>

- **No**
    - Com essa classe é possível instanciar objetos que representam o grafo acíclico dirigido (DAG), ou seja, isso ...
    
    <br>


- **Link**
    - Classe responsável por armazenar o estado de conexão entre os vértices (arcos), contendo a **posição do vértice de destino**, o **custo** e um apontamento pra outro objeto do tipo **Link**.

    <br>

- **BellmanFord**
    - Esta classe é responsável por implementar o algoritmo de **Bellman-Ford**,  e também disponibiliza um método **caminhosMinimos()** para executar o mesmo, necessitando para isso, receber um objeto do tipo **Digrafo**, um **vértice de origem** e um **custo máximo**.

    <br>

- **Dijkstra**
    - Classe que implementa o algoritmo de **Dijkstra**, e também disponibiliza um método **caminhosMinimos()** para executar o mesmo, necessitando para isso, receber um objeto do tipo **Digrafo** e um **vértice de origem**.

    <br>


- **MinHeap**
    -  Nesta classe é implementado a estrutura de dados "Heap" (mais especificamente min-heap), que permite armazenar um conjunto de elementos de forma ordenada, de modo que o primeiro elemento é o menor, e o último é o maior. São disponbilizados métodos para inserir, remover e atualizar os elementos. 
    <br>Isso é usado como uma fila de prioridade para a execução do algoritmo de **Dijkstra**.

    <br>


- **MedidorTempo**
    - Responsável por calcular o tempo total gasto em cada simulação de cada algoritmo; para não existir interferência nos valores devido a outros processos rodando na máquina durante as simulações, foi usado a classe **ManagementFactory** e **ThreadMXBean**, retornando o tempo total de CPU para o processo específico atual em nanossegundos, que depois foi convertido para milissegundos para melhor visualização.

    <br>


---
### Compilação e Execução
Para compilar o código, basta executar o comando: `javac Main.java`

Para execucar o programa, basta executar o comando: `java Main`

<br>

---
**Créditos**

Professor: Alexandre Freire

Aluno: José Rafael R. Nascimento

Aluno: Luiz Fernando Conceição dos Santos





