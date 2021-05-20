import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;



public class ArvoreB {

  	private int t;
	private final int TAMANHO_NO_INTERNO = 2 * 4 +  4 * (2 * t - 1) + 4 * (2 * t);
	private final int TAMANHO_NO_FOLHA = 2 * 4 +  4 * (2 * t - 1);
	private static final int NULL = -1;
	private static final int BLOCO_VAZIO = -2;
	private static final int END_CABECA_PILHA_BLOCOS_VAZIOS = 0;
	private static final int END_APONTADOR_RAIZ = 4;
	private static final int END_TAMANHO = 8;
	private static final int TAMANHO_CABECALHO = 3 * 4;
	private static final int TRUE = 1;
	private static final int FALSE = 0;
	private String nomeArq;
	private No raiz;


	
	//Construtor da árvore B.
  	public ArvoreB(int ordem, String nomeArq) throws IOException {
    
	   	this.t = ordem;
	  	this.raiz = new No(true, t);  
	  	this.nomeArq = nomeArq;    	

    	RandomAccessFile arq = new RandomAccessFile(nomeArq, "rw");
      
		  //Caso a árvore não exista.
    	if (arq.length() == 0) {

			System.out.println("\n !! Criando arvore !! ");
    		
			//Escrevendo o cabeçalho, ou seja, o primeiro end da árvore.      		
			gravaNo(raiz,END_APONTADOR_RAIZ);
      
		//Se existe apenas leia.
		} else { 
			System.out.println("\n !! Lendo arvore do disco !! ");
			raiz = leNo(END_APONTADOR_RAIZ);
		}
		
		arq.close();
	}
    
    //Posicao para gravar no final do arquivo;
    private int posicaoParaGravar() throws IOException{ 
    
		RandomAccessFile arq = new RandomAccessFile(nomeArq, "rw");
		int posicaoNo = (int) arq.length();
		arq.close();

		return posicaoNo;
    }


	//*******************************************
	//		    LEITURA NO
	//*******************************************

	public No leNo(int end) throws IOException {

		RandomAccessFile arq = new RandomAccessFile(nomeArq, "r");
        
		//Se tiver nada devolve null;
		if (arq.length() == 0 || end == NULL) {
			arq.close();
			return null;
		}
        
		//Caso contrário coloque o ponteiro na folha e escreva as folhas;
		arq.seek(end);
		
		//Le o primeiro campo para verificar se é folha
		boolean ehFolha = arq.readInt() == TRUE ? true : false;

		//Le os bytes de uma vez, criando vetor de bytes e ler, o -4 é pq leu o primeiro byte;
		byte[] bytes = ehFolha ? new byte[TAMANHO_NO_FOLHA - 4] : new byte[TAMANHO_NO_INTERNO - 4];

		//Le os bytes de uma vez.
		arq.read(bytes);
		
		
		//Le o vetor de bytes em vez do arquivo
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		
		No no = new No(ehFolha, t);
		no.nChaves = leInt(in);

		//Carrega as chaves
		for(int i = 0; i < no.chaves.length; i++) {
			no.chaves[i] = leInt(in);
		}

		//Se não for folha, então carrega os filhos
		if(!ehFolha) {
			for(int i = 0; i < no.filhos.length; i++) {
				no.filhos[i] = leInt(in);
			}
		}

		arq.close();

		/*System.out.print("\n... No lido ...");
	  no.imprime();
    */
		return no;
	}
	

	
	private int leInt(ByteArrayInputStream in) {
		byte[] bInt = new byte[4];
		in.read(bInt, 0, 4);//ler os 4 bytes e colocar na posição 0.
		return ByteBuffer.wrap(bInt).asIntBuffer().get();//converte os bytes em int.
	}
	


	//********************************************
	//		    GRAVACAO NO
	//********************************************
	
	public void gravaNo(No no, int end) throws IOException {

		System.out.print("\n... Gravando nó ...");
		no.imprime();

		int nBytes = no.ehFolha() ? TAMANHO_NO_FOLHA : TAMANHO_NO_INTERNO;
		ByteArrayOutputStream out = new ByteArrayOutputStream(nBytes);
		
		escreveInt(out, no.folha);
		escreveInt(out, no.nChaves);
		
		//Armazena as chaves
		for(int i = 0; i < no.chaves.length; i++) {
			escreveInt(out, no.chaves[i]); 
		}

		//Armazena os filhos
		if(!no.ehFolha()) {
			for(int i = 0; i < no.filhos.length; i++) {
				escreveInt(out, no.filhos[i]);
			}
		}
		
		//Abre o arquivo e grava no disco
		RandomAccessFile arq = new RandomAccessFile(nomeArq, "rw");
		arq.seek(end);
		arq.write(out.toByteArray());
		arq.close();

	}


	public int gravaNovoNo(No no) throws IOException {
    
	  int nBytes = no.ehFolha() ? TAMANHO_NO_FOLHA : TAMANHO_NO_INTERNO;
		ByteArrayOutputStream out = new ByteArrayOutputStream(nBytes);
		
		escreveInt(out, no.folha);
		escreveInt(out, no.nChaves);

		//Armazena as chaves
		for(int i = 0; i < no.chaves.length; i++) {
			escreveInt(out, no.chaves[i]); 
		}

		//Armazena os filhos
		if(!no.ehFolha()) {
			for(int i = 0; i < no.filhos.length; i++) {
				escreveInt(out, no.filhos[i]);
			}
		}
		
		RandomAccessFile arq = new RandomAccessFile(nomeArq, "rw");
    	
		int posicaoNo = (int) arq.length();
		arq.seek(posicaoNo);
		arq.write(out.toByteArray());
		arq.close();

		return posicaoNo;
	}


	private void escreveInt(ByteArrayOutputStream out, int i) {
		byte[] num = ByteBuffer.allocate(4).putInt(i).array();
		out.write(num, 0, 4);
	}


	//*********************************************
	//		    MANIPULAÇOES NA ARVORE-B
	//*********************************************

	//Separação os nó;
	public void split(No x, int i, No y) throws IOException { 
    boolean yEhFolha =  y.ehFolha();//Passa um valor booleano do Nó y.
    No auxiliar = new No(yEhFolha, t);
		auxiliar.nChaves = t - 1;

		//Copia as chaves
		for(int j = 1 ; j < t - 1; j++){
			auxiliar.chaves[j] = y.chaves[j + t];
		}

		//Copia os filhos
		if(!y.ehFolha()){
			for(int j = 1; j < t; j++) {
				auxiliar.filhos[j] = y.filhos[j+t];
			}
		}

		y.nChaves = t-1;
		
		//Arrasta as chaves para "abrir espaco"
		for(int j = x.nChaves; j >= i + 1; j--) { 
			x.filhos[j+1] = x.filhos[j];
		}
		
		x.filhos[i+1] = gravaNovoNo(auxiliar);//Usar o gravaNovoNo
		
		for(int j = x.nChaves - 1; j>=i;j--){
			x.chaves[j+1] = x.chaves[j];
		}
		
		x.chaves[i] = y.chaves[t-1];
		x.nChaves = x.nChaves+1;
		
		//Usar o gravaNo no final do arquivo.
		gravaNo(x, posicaoParaGravar());
		gravaNo(y, posicaoParaGravar());
		gravaNo(auxiliar, posicaoParaGravar());
	}
	
	
	//Insere valor da chave;
	public void insereChave(final int key) throws IOException{
	   	No r = raiz;

		//Verifica se a raiz está cheia
		if (r.nChaves == 2*t-1) {

			No pai = new No(false,t);
      raiz = pai;
			pai.nChaves = 0;
			pai.filhos[0] = gravaNovoNo(r);//r não possui endereço;

			split(pai, 0, r);
			insereNo_NoFull(pai, key);
		}
		else{
			insereNo_NoFull(r, key);
		}

	}



final	private void insereNo_NoFull(No x, int key) throws IOException{


		if (x.ehFolha()){
    	int i = x.nChaves -1;
			while(i >= 0 && key < x.chaves[i]){
				x.chaves[i+1] = x.chaves[i];
				i--;
			}

			x.chaves[i+1] = key;
			x.nChaves++;

			gravaNo(x, posicaoParaGravar());

		}
		else{
    	int i = x.nChaves -1;
			while(i >= 1 && key < x.chaves[i]){
      			i--;
    		}
    		
			i++;
    		No temp = leNo(x.filhos[i]);
     
    		if(temp.isFull()){
      			split(x,i,temp);
    		if(key > x.chaves[i]){
      		i++;
    	}
    }
    
    insereNo_NoFull(leNo(x.filhos[i]),key);

		}

	}


	//**************************************
	//		      BUSCA
	//**************************************
  
   //Verificar se está presente usando o busca.
  public boolean buscaB(int k) throws IOException {
    if (this.buscaB(raiz, k) != null) {
      return true;
    } else {
      return false;
    }
  }
	  
  private No buscaB(No no, int chave) throws IOException{
		
	 	int i = 0;

		if(no == null) {//Verifica se o no é null;
			return no;
		}
		
		for(i = 0; i < no.nChaves; i++){
			if(chave < no.chaves[i]){
				break;
			}
			if(chave == no.chaves[i]) { 
			return no;
			}
		
		}
		//Verifica se é folha.
		if(no.ehFolha()){
			return null;
		
		//Caso contrário lê o nó da árvore.
		}else { 
			//Chamar recursivamente, antes disso precisa ler o índice i; 
			return buscaB(leNo(no.filhos[i]),chave);
		}
	}
  

	//Remove chave;
	public void remove(No no, int chave) throws IOException {

	}


	//Método para saber a quantidade de memória usada no programa, será usado na bateria de testes.
	public void obterMemoriaUsada(){

		final int MB = 1024 * 1024;//converter bytes para megas;
		Runtime runtime = Runtime.getRuntime();
		
		System.out.println("MEMORIA USADA \t" + ((runtime.totalMemory() - runtime.freeMemory())/MB) + " mb");

	}


  //******************************************************************
  //                 IMPRESSÃO RAIZ
  //******************************************************************
	  public void mostrar() throws IOException {
    mostrar(this.raiz);
  }

 
  private void mostrar(No x) throws IOException {
    assert (x == null);
    for (int i = 0; i < x.nChaves; i++) {
      System.out.print(x.chaves[i] + " ");
     }
    //boolean xEhFolha = x.ehFolha();
    if (!x.ehFolha()) {
      for (int i = 0; i < x.nChaves + 1; i++) {
        mostrar(leNo(x.filhos[i]));
      }
    }
  }


	//******************************************************************
	//		    GETs e SETs
	//*******************************************************************

	public int getOrdemArvore(){
		return this.t;
	}

	
	No get(boolean ehFolha) {
		return new No(ehFolha, t);
	}

	public No getRaiz(){
		return this.raiz;
	}
	
	public int getEnderecoRaiz(){
		return this.END_APONTADOR_RAIZ;
	}
}