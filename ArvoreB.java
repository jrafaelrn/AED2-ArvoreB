import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Stack;


public class ArvoreB {

  	private static final int NULL = -1;
	private static final int TRUE = 1;
	private static final int FALSE = 0;
	private static final int TAMANHO_CABECALHO = 4;
	private String nomeArq;
	private No raiz;
	private int t;
	private int TAMANHO_NO_INTERNO;
	private int TAMANHO_NO_FOLHA;
	private int NUM_MAX_CHAVES;
	private int NUM_MIN_CHAVES;
	private int NUM_MAX_FILHOS;


  	private void inicializaConstantes(int t) {
		this.t = t;
		TAMANHO_NO_INTERNO = 2 * 4 + 4 * (2 * t - 1) + 4 * (2 * t);
		TAMANHO_NO_FOLHA = 2 * 4 + 4 * (2 * t - 1);
		NUM_MAX_CHAVES = 2 * t - 1;
		NUM_MAX_FILHOS = NUM_MAX_CHAVES + 1;
		NUM_MIN_CHAVES = t - 1;
	}



	
	//Construtor da árvore B.
  	public ArvoreB(int ordem, String nomeArq) throws IOException {
    
		this.nomeArq = nomeArq;
		inicializaConstantes(ordem);
		
		//Verifica se o arquivo existe.
		if (!new File(nomeArq).exists()) {
			
			No no = new No(true,t);
			no.endereco = TAMANHO_CABECALHO;
			trocaRaiz(no);
			atualizaNo(no);


		} else {
      		
			//Caso exista apenas lê o endereço que está localizado o nó raiz;
			carregaRaizNaRAM();
      
   		}
   }
     



	//*******************************************
	//		    	  	RAIZ
	//*******************************************

    private void trocaRaiz(No novaRaiz) throws FileNotFoundException, IOException {
		
		RandomAccessFile arq = new RandomAccessFile(nomeArq, "rw");
		this.raiz = novaRaiz;
		arq.writeInt(this.raiz.endereco);
		arq.close();

	}

	private void carregaRaizNaRAM() throws FileNotFoundException, IOException {
		RandomAccessFile arq = new RandomAccessFile(nomeArq, "r");
		this.raiz = leNo(arq.readInt());
    	System.out.println(" \n");
    	System.out.println("TAMANHO ARQ " + arq.length());
		arq.close();
	}





	//*******************************************
	//		    	  LEITURA NO
	//*******************************************

	public No leNo(int endereco) throws IOException {
	
		RandomAccessFile arq = new RandomAccessFile(nomeArq, "r");
    	
		//Se tiver nada devolve null; 
		if (arq.length() == 0 || endereco == NULL) {
			arq.close();
			return null;
		}
    
		//Caso contrário coloque o ponteiro na folha e escreva as folhas;
		arq.seek(endereco);
    	
		//Le o primeiro campo para verificar se é folha
		boolean ehFolha = arq.readInt() == TRUE ? true : false;
    	
		//Le os bytes de uma vez, criando vetor de bytes e ler, o -4 é pq leu o primeiro byte;
		byte[] bytes = ehFolha ? new byte[TAMANHO_NO_FOLHA - 4] : new byte[TAMANHO_NO_INTERNO - 4];
    	
		//Le os bytes de uma vez.
		arq.read(bytes);
    	
		//Le o vetor de bytes em vez do arquivo
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		No no = new No(ehFolha,t);
		no.nChaves = leInt(in);
		no.endereco = endereco;
    	
		//Carrega as chaves
		for (int i = 0; i < no.chaves.length; i++) {
			no.chaves[i] = leInt(in);
		}
    	
		//Se não for folha, então carrega os filhos
		if (!ehFolha) {
			for (int i = 0; i < no.filhos.length; i++) {
				no.filhos[i] = leInt(in);
			}
		}

		
		arq.close();
		return no;
	}
	
	
	private int leInt(ByteArrayInputStream in) {
		byte[] bInt = new byte[4];
		in.read(bInt, 0, 4);//Coloca os 4 bytes na posição 0;
		return ByteBuffer.wrap(bInt).asIntBuffer().get();//converte os bytes em int.
	}

    private void escreveInt(ByteArrayOutputStream out, int i) {
		byte[] num = ByteBuffer.allocate(4).putInt(i).array();
		out.write(num, 0, 4);
	}





	//*******************************************
	//		  	ATUALIZACAO/GRAVACAO NO
	//*******************************************

	//	Assume que o "nó" ja tem um endereco
	private void atualizaNo(No no) throws IOException {
    
		//System.out.println("\n------------ ATUALIZANDO NÓ -----------");
    	//no.imprime();

		int nBytes = no.ehFolha() ? TAMANHO_NO_FOLHA : TAMANHO_NO_INTERNO;
		ByteArrayOutputStream out = new ByteArrayOutputStream(nBytes);
		escreveInt(out, no.folha);
		escreveInt(out, no.nChaves);

		for (int i = 0; i < no.chaves.length; i++) {
			escreveInt(out, no.chaves[i]);
		}

		if (!no.ehFolha()) {
			for (int i = 0; i < no.filhos.length; i++) {
				escreveInt(out, no.filhos[i]);
			}
		}

		RandomAccessFile arq = new RandomAccessFile(nomeArq, "rw");
		arq.seek(no.endereco);
		arq.write(out.toByteArray());
		arq.close();
	}
	



	// apos chamar a funcao, o "no" terah um endereco
	private int gravaNovoNo(No no) throws IOException {
		
		/*obs 1: "new File" nao cria um arquivo novo, apenas carrega
		 informacoes sobre o arquivo ja existente
		 obs 2: o novo no sera gravado no final do arquivo 
		 (ou seja, vai aumentar o tamanho do arq qdo chamar "gravaNovoNo")*/
    	
		//System.out.println("--------- gravaNovoNo -----------");
		no.endereco = (int) new File(nomeArq).length();
		atualizaNo(no);
    	
		//System.out.println("Nó: " + no + " Posicao end: " + no.endereco);

    	return no.endereco;
	}






	//********************************
	//		      SPLIT
	//********************************

	/*
	Separação os nó;
	Tamanho do Nó folha 2*t-1 e Nó filho 2*t;
	*/
	private void split(No x, int i, No y) throws IOException { 
  
		//System.out.println("\n !!!!  SPLIT   !!!\n");
    	
    	
		boolean yEhFolha =  y.ehFolha();//Passa um valor booleano do Nó y.
    	No auxiliar = new No(yEhFolha, t);
		auxiliar.nChaves = t - 1;
    	
		
		//Copia as chaves
		for(int j = 0 ; j < t - 1; j++){
      		//System.out.println(" Copiando as chaves \t" + j);
			auxiliar.chaves[j] = y.chaves[j + t];
		}

		//Copia os filhos
		if(!y.ehFolha()){
			for(int j = 0; j < t; j++) {
        		//System.out.println(" Copiando os filhos \t" + j);
				auxiliar.filhos[j] = y.filhos[j + t];
			}
		}

		y.nChaves = t - 1;
		
		
		for(int j = x.nChaves; j >= i + 1; j--) { 
      	//System.out.println(" Arrastando os filhos \t" + j);
			x.filhos[j+1] = x.filhos[j];
		}
		 
		x.filhos[i+1] = gravaNovoNo(auxiliar);//Usar o gravaNovoNo


		//Arrasta as chaves para "abrir espaco"
		for(int j = x.nChaves - 1; j >= i; j--){
    		//System.out.println(" Arrastando as chaves \t" + j);
			x.chaves[j + 1] = x.chaves[j];
		}
		
		x.chaves[i] = y.chaves[t-1];
		x.nChaves = x.nChaves+1;

	
		//Apenas atualiza os Nó no arquivo.
    	atualizaNo(y);
		atualizaNo(x);
	}



	
	
	//**********************************
	//		      INSERCAO
	//**********************************	
	
	//Insere valor da chave;
	public void insereChave(int key) throws IOException{
  		
		//System.out.println("Inserindo Chave: " + key);
	   	No r = raiz;
     
		//Verifica se a raiz está cheia, se estiver divide o Nó raiz.
		if (r.nChaves == NUM_MAX_CHAVES) {
   	
			No pai = new No(false,t);//Cria um pai para a raiz;
      		gravaNovoNo(pai);
			trocaRaiz(pai);
			

			
			pai.nChaves = 0;
			pai.filhos[0] = gravaNovoNo(r);//primeiro filho dele é o r;
			split(pai, 0, r);
			insereNo_NoFull(pai, key);

		}
		else{		
			insereNo_NoFull(r, key);
		}
   		

	}



	private void insereNo_NoFull(No x, int key) throws IOException{
   		
		//System.out.println("...Inserindo em No_NoFull");   		
		
		if (x.ehFolha()){

			int i = 0;
		
			for(i = x.nChaves-1; i >= 0 && key < x.chaves[i]; i--){			
				x.chaves[i+1] = x.chaves[i];
			}

			x.chaves[i+1] = key;
			x.nChaves++;


			atualizaNo(x);

		}
		else{
      		int i = 0;

			for(i = x.nChaves-1; i >= 0 && key < x.chaves[i]; i--){}
			
			i++;
      	
    		No temp = leNo(x.filhos[i]);
     
			if(temp.nChaves == NUM_MAX_CHAVES){
			
				split(x,i,temp);
			
				if(key > x.chaves[i]){
					i++;
				}
      		}
    
    		insereNo_NoFull(leNo(x.filhos[i]),key);

		}


	}




	//**************************************
	//		      	  BUSCA
	//**************************************
  
	//Verificar se está presente usando o busca.
  	public boolean buscaB(int k) throws IOException {

    	if (this.buscaB(raiz, k) != null) {
      		return true;
		} else {
			return false;
		}

  	}

    //Devolve a chave buscada.
  	public int buscaBNovo(int k) throws IOException {

    	if (this.buscaB(raiz, k) != null) {
      		return k;
		} else {
			return NULL;
		}

  	}

	  
	private No buscaB(No no, int chave) throws IOException{
		
	 	int i = 0;

		if(no == null) {//Verifica se o no é null;
			return no;
		}
		
		for(i = 0; i < no.nChaves; i++){
			
			if(chave < no.chaves[i])
				break;
			
			if(chave == no.chaves[i]) 
				return no;
		
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




  	//**************************************
	//		      	REMOCAO
	//**************************************
	
	//Método para ser chamado no Main
	public void remove(int chave) throws IOException {
	
		No no = buscaB(raiz, chave);
	
		if (no == null) {
			return;
		}
		remove(raiz, chave);
	}


	/*O endereço das chaves não está sendo empilhada em blocos vazios, então se excluir ainda continua no arquivo*/

	//Remove chave;
	private void remove(No no, int chave) throws IOException {

		// Preemtive MERGE
		preemtiveMerge(no, chave);

  		
		// Primeiro busca a chave no array de chaves do No Atual
  		int pos = no.buscaChave(chave);
  		
		System.out.println("\n\n\nINICIANDO REMOÇÃO - CHAVE " + chave +"\n");
		System.out.println("Posicao Encontrada no inicio " + pos);

  		
		// Se a chave estiver no NÓ
		if(pos != NULL) { 

			// CASO 1 = Remove de um NÓ FOLHA
			if (no.ehFolha()){
				removeChave_noFolha(no, chave);
				return;
			}

			//	CASO 2 = Remove de um NÓ INTERNO
			removeChave_noInterno(no, chave, pos);				
			return;

		} 
		
		//SEGUNDA PARTE

		/*A altura da árvore diminui. Caso a chave esteja no nó interno e a exclusão da chave levar a um número menor de chaves no nó (ou seja, menos do que o mínimo necessário, usando no.nChaves para saber a quantidade mínima por nó)*/		 

		System.out.println("Chave nao localizada no No Atual: " + no.endereco);
		System.out.println("...Iniciando busca pelas chaves...");
		
		for(pos = 0; pos < no.nChaves; pos++){
			
			System.out.println("\tAvaliando chave[" + pos + "] = " + no.chaves[pos]);
			
			if(no.chaves[pos] > chave){
				System.out.println("\t\tCHAVE[" + pos + "] = " + no.chaves[pos] + " > " + chave);
				break;
			}
		}
	
		//Caso o nó já tenha a quantidade mínima, precisa tentar encontrar um predecessor ou sucessor para ele, chamando recursivamente
		No tmp = leNo(no.filhos[pos]);
		System.out.println("\nNO Temporario: " + tmp.endereco);
		System.out.println("\tChaves NO Temporario: " + tmp.nChaves + " > " + t);

		/*
		if (tmp.nChaves >= t) {
			atualizaNo(no);
			remove(tmp, chave);
			return;
		}*/

		remove(tmp, chave);
		return;
	
		
		
		/*Se ambos os filhos predecessor e sucessor tiver um número mínimo de chave, o empréstimo de chaves não pode ser feito, isso leva a fusão dos filhos
		if(true){
			
			//System.out.println("Caso não seja o número mínimo");
			No b = null;
			int divide = NULL;
				
			//Verificar o número mínimo da direita
			No ler = leNo(no.filhos[pos + 1]);
			
			if(pos != no.nChaves && ler.nChaves >= t-1){
			
				System.out.println("Filho " + no.filhos[0]);
				divide = no.chaves[pos];
				b = leNo(no.filhos[pos + 1]);
				no.chaves[pos] = b.chaves[0];
				tmp.chaves[tmp.nChaves++] = divide;
				tmp.filhos[tmp.nChaves] = b.filhos[0];

				for(int i = 1; i < b.nChaves; i++){
					b.chaves[i - 1] = b.chaves[i];
				}
				for(int i = 1; i <= b.nChaves; i++){
					b.filhos[i - 1] = b.filhos[i];
				}
			
			
				b.nChaves--;
				atualizaNo(no);
				remove(tmp,chave);
				return;
			} 
			
			//Verificar o número mínimo da esquerda
			No ler2 = leNo(no.filhos[pos - 1]);
			
			if (pos != 0 && ler2.nChaves >= t-1){
			
				divide = no.chaves[pos - 1];
				b = leNo(no.filhos[pos - 1]);
				no.chaves[pos - 1] = b.chaves[b.nChaves - 1];
				int filhos = b.filhos[b.nChaves];
				b.nChaves--;

				for (int i = tmp.nChaves; i > 0; i--) {
					tmp.chaves[i] = tmp.chaves[i - 1];
				}
				tmp.chaves[0] = divide;
			
				for (int i = tmp.nChaves + 1; i > 0; i--) {
					tmp.filhos[i] = tmp.filhos[i - 1];
				}
			
				tmp.filhos[0] = filhos;
				tmp.nChaves++;
				atualizaNo(no);
				remove(tmp, chave);
				return;

			}
			else {
				
				No predecessor = null;
				No sucessor = null;
				boolean bool = false;

				if (pos != no.nChaves) {
					divide = no.chaves[pos];
					predecessor = leNo(no.filhos[pos]);
					sucessor = leNo(no.filhos[pos + 1]);
				} 
				else {
					divide = no.chaves[pos - 1];
					sucessor = leNo(no.filhos[pos]);
					predecessor = leNo(no.filhos[pos - 1]);
					bool = true;
					pos--;
				}

				for (int i = pos; i < no.nChaves - 1; i++) {
					no.chaves[i] = no.chaves[i + 1];
				}
				for (int i = pos + 1; i < no.nChaves; i++) {
					no.filhos[i] = no.filhos[i + 1];
				}

				no.nChaves--;
				predecessor.chaves[predecessor.nChaves++] = divide;

				for (int i = 0, j = predecessor.nChaves; i < sucessor.nChaves + 1; i++, j++) {

					if (i < sucessor.nChaves) {
						predecessor.chaves[j] = sucessor.chaves[i];
					}
					
					predecessor.filhos[j] = sucessor.filhos[i];
				}
				
				predecessor.nChaves += sucessor.nChaves;

				if (no.nChaves == 0) {
					if (no == raiz) {
						raiz = leNo(no.filhos[0]);
						trocaRaiz(no);
					}
					no = leNo(no.filhos[0]);
				}

				atualizaNo(no);
				remove(predecessor, chave);
				return;
			}
		}
		*/
		

	}


	private void removeChave_noFolha(No no, int chave) throws IOException {

		System.out.println("\n...Removendo de um NÓ FOLHA");
		int i = 0;
	
		//Passa por todas as chaves para encontrar a posição, se encontrar passa para o outro for
		for (i = 0; i < no.nChaves && no.chaves[i] != chave; i++){};
	
		System.out.println("Ultima posicao: " + i);
	
	
		//Verifica se a posição chegou até o limite do nó, caso não chegou mova as chaves	
		for(;i < no.nChaves-1; i++) { 
			
			//System.out.println("dentro " + i);
			//if(i != 2 * t - 2)
				no.chaves[i] = no.chaves[i+1];
				//System.out.println("Caso esteja com a quantidade mínima " + i);
			
		}
	
		no.nChaves--;//Decrementa a quantidade
		atualizaNo(no);//Atualiza no DISCO
	
	}



	private void removeChave_noInterno(No no, int chave, int pos) throws IOException {

		System.out.println("É FILHO");
		System.out.println("FILHO " + no.filhos[pos]);
		
		//	CASO 2a = PREDECESSOR
		//PREDECESSOR de nó, maior chave no filho esquerdo
		No predecessor = leNo(no.filhos[pos]);

		if(predecessor.nChaves >= t-1){ 
			removeChave_noInterno_predecessor(no, predecessor, pos);
			return;
		}
	
	

		//	CASO 2b = SUCESSOR				
		System.out.println("SEGUNDA PARTE");
		
		//SUCESSOR, menor chave do filho direito
		No sucessor = leNo(no.filhos[pos + 1]);		

		//No sucessor tem pelo menos t chaves;
		if(sucessor.nChaves >= t-1){
			removeChave_noInterno_sucessor(no, sucessor, pos);
			return;
		}


		int temp = predecessor.nChaves + 1;
		predecessor.chaves[predecessor.nChaves++] = no.chaves[pos];

		for(int i = 0, j = predecessor.nChaves; i < sucessor.nChaves; i++){
			predecessor.chaves[j++] = sucessor.chaves[i];
			predecessor.nChaves++;
		}

		//Movendo os filhos que está no sucessor para o predecessor
		for(int i = 0; i < sucessor.nChaves + 1; i++){
			predecessor.filhos[temp++] = sucessor.filhos[i];
		}
		
		//Substituo o filho que foi removido pelo predecessor 
		No removido = leNo(no.filhos[pos]);
		removido = predecessor;
		
		//Ocupar as posições das chaves até a última posição
		for (int i = pos; i < no.nChaves; i++) {
			if (i != 2 * t - 2) {
				no.chaves[i] = no.chaves[i + 1];
			}
		}
		
		//Ocupar as posições dos filhos, até a última posição
		for (int i = pos + 1; i < no.nChaves + 1; i++) {
			if (i != 2 * t - 1) {
				no.filhos[i] = no.filhos[i + 1];
			}
		}
		no.nChaves--;
		
		//Caso o nó que vai ser excluído a chave seja da raiz
		if (no.nChaves == 0) {
			if (no == raiz) {
				raiz = leNo(no.filhos[0]);
				trocaRaiz(no);
			}
			no = leNo(no.filhos[0]);
		}
		
		atualizaNo(no);
		remove(predecessor, chave);


	}



	private void removeChave_noInterno_predecessor(No no, No predecessor, int pos) throws IOException {

		int auxChave = 0;
		//No predecessor tem pelo menos t chaves, se tiver é substituído;

		//executa várias vezes até chegar em uma folha e parar
		for(;;){
			if(predecessor.ehFolha()){
				auxChave = predecessor.chaves[predecessor.nChaves - 1];
				System.out.println("auxChave " + auxChave);
				break;
			} 
			else { 
				//Passando todas as chaves do nó para esse predecessor
				predecessor = leNo(predecessor.filhos[predecessor.nChaves]);
			}
		}


		no.chaves[pos] = auxChave;//Substituindo 
		System.out.println("Substituindo " + no.chaves[pos]);
		atualizaNo(no);
		
		//Eliminando recursivamente e substituindo por um predecessor
		remove(predecessor, auxChave);

	}




	private void removeChave_noInterno_sucessor(No no, No sucessor, int pos) throws IOException {

		int sucessorChave = sucessor.chaves[0];
		//É substituído por um sucessor se tiver mais que a ordem 
		
		if(!sucessor.ehFolha()){
			sucessor = leNo(sucessor.filhos[0]);
		
			for(;;){ 
				if(sucessor.ehFolha()){
					sucessorChave = sucessor.chaves[sucessor.nChaves - 1];
					break;
				}
				else { 
					sucessor = leNo(sucessor.filhos[sucessor.nChaves]);
				}
			}
		}

		no.chaves[pos] = sucessorChave;
		atualizaNo(no);
		remove(sucessor, sucessorChave);
	
	}



	private void fusaoNo(No no, int chave) throws IOException{



	}



	private void preemtiveMerge(No no, int chave) throws IOException{

		System.out.println("\n...Iniciando Preemtive Merge - NO: " + no.endereco);
		System.out.println("\tTotal Chaves: " + no.nChaves);

		if (no.nChaves <= t){
			
			int pos = 0;
			for (; pos < chave && pos < no.nChaves; pos++){}

			No proximoFilho = leNo(no.filhos[pos]);
			System.out.println("\tPrimeiro FILHO = NO.endereco: " + proximoFilho.endereco);

			if (proximoFilho.nChaves <= t){

				System.out.println("\n\tFilho precisa de Merge!");

			}

		}

	}





	

	//Método para saber a quantidade de memória usada no programa, será usado na bateria de testes.
	public void obterMemoriaUsada(){

		final int MB = 1024 * 1024;//converter bytes para megas;
		Runtime runtime = Runtime.getRuntime();
		
		System.out.println("MEMORIA USADA \t" + ((runtime.totalMemory() - runtime.freeMemory())/MB) + " mb");

	}


	//*********************************
	//		    GETs e SETs
	//*********************************

	public int getOrdemArvore(){
		return this.t;
	}
	
	No get(boolean ehFolha) {
		return new No(ehFolha, t);
	}

	public No getRaiz(){
		return this.raiz;
	}
	
}