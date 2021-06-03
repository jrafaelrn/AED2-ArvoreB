import java.io.IOException;
import java.io.File;
import java.util.Random;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.RandomAccessFile;
import java.io.FileWriter;
import java.io.BufferedWriter;



public class Simulacao{

	private ArvoreB arvB;
	private No no;
	private MedidorTempo medidor;
	private int ordem;
	private Random rand = new Random(12345);
	private int[] vetorTeste;
	


	public Simulacao(int ordem) throws IOException {

		//Apagando arquivo antigo, se houver
		File file = new File("arv.txt");
		if (file.exists())
			file.delete();

		this.ordem = ordem;
		this.medidor = new MedidorTempo();
		this.vetorTeste = new int[1000000];

		// Cria vetor com 1 milhao de valores de teste		
		for(int i = 0; i < 1000000; i++){
			this.vetorTeste[i] = rand.nextInt();			
			//System.out.println("Num criado: " + this.vetorTeste[i]);
		}

	}




	public void simulaTudo() throws IOException {

		// TESTES DE INSERCAO
		//System.out.print("Numero Testes: ");
		for (int i = 10; i <= 10000; i+=1000){
			
			this.arvB = new ArvoreB(this.ordem, "arv.txt");
			//System.out.print(i + ", ");

			// (i) Primeiros
			this.simulaInsercao(i);			
			
			// TESTES DE BUSCA
			simulaBusca(i);

			// Corrigi numero para manter arredondamento
			i = i == 10 ? 0 : i;

			//Apaga arquivo
			File file = new File("arv.txt");
			file.delete();	
		}

		


	}


	private void simulaInsercao(int tamanho) throws IOException {
		
		medidor.comeca("");

		// Inserindo as chaves
		for(int j = 0; j < tamanho; j++)
			arvB.insereChave(this.vetorTeste[j]);
		

		long tempo = medidor.termina();
		//System.out.print("\t\t\t" + tempo + "\t");

		gravarLogInsercao("TIPO:INSERCAO,QTD:"+ tamanho + ",TEMPO:" + tempo);

	}

	private void simulaBusca(int tamanho) throws IOException {

		int existente;
		int naoExistente;		
		long tempo;
		boolean temp;

		// 1.000 buscas repetidas
		medidor.comeca("");
		for(int i = 0; i < 10000; i++){
			existente = this.vetorTeste[i];
			temp = this.arvB.buscaB(existente);
		}
		
		tempo = medidor.termina();
		
		//System.out.print(tempo);
		gravarLogBusca("TIPO:BUSCA,SUB-TIPO:EXISTENTE,TAMANHO:" + tamanho + ",TEMPO:" + tempo);
		//System.out.println("\t\t\t !! Encontrado !!");
		

		medidor.comeca("");		
		for(int i = 0; i < 10000; i++){
			naoExistente = this.vetorTeste[i]+1;
			temp = arvB.buscaB(naoExistente);
		}
		
		tempo = medidor.termina();
		//System.out.println(tempo);
		gravarLogBusca("TIPO:BUSCA,SUB-TIPO:NAO_EXISTENTE,TAMANHO:" + tamanho + ",TEMPO:" + tempo);
		//System.out.println("\t\t\t !! Encontrado !!");	

	}



	public void gravarLogInsercao(String log) throws IOException {
		gravarArquivoLog("log_insercao.txt", log);
	}


	public void gravarLogBusca(String log) throws IOException{
		gravarArquivoLog("log_busca.txt", log);
	}


	private void gravarArquivoLog(String arquivoLog, String log)throws IOException{

		if(!new File(arquivoLog).exists()){
			RandomAccessFile arq = new RandomAccessFile(arquivoLog, "rw");
		}

		Writer csv = new BufferedWriter(new FileWriter(arquivoLog, true));
		csv.append(log + ",ORDEM:" + this.ordem + "\n");
		csv.close();


	}

}