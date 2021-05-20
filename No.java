import java.util.Arrays;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.IOException;

public class No {

	int folha, t, nChaves;
	int[] chaves, filhos;
	private static final int TRUE = 1;
	private static final int FALSE = 0;
	private static final int NULL = -1;
  private String nomeArq;
  

	//Construtor
	public No(boolean ehFolha,int ordem) {
		this.t = ordem;
		this.chaves =  new int[2 * t - 1];
		this.nChaves = 0;
		this.folha = ehFolha ? TRUE : FALSE;
		
		if(!ehFolha()) {
			this.filhos = new int[2 * t];
			//Arrays.fill(filhos, NULL);
		}

	}

  	//Construtor
	public No(boolean ehFolha,int ordem, int chave) throws IOException {
		this.t = ordem;
		this.chaves =  new int[2 * t - 1];
		this.nChaves = 0;
		this.folha = ehFolha ? TRUE : FALSE;
		
		if(!ehFolha()) {
			this.filhos = new int[2 * t];
			//Arrays.fill(filhos, NULL);
		}
  	RandomAccessFile arq = new RandomAccessFile(nomeArq, "rw");
    if((int) arq.length() == 0){
    int i = (int) arq.length();
    this.chaves[i] = chave; 
   }
    arq.close();
	}
  


	public void imprime() {

		System.out.println("\nEh folha: " + (folha == TRUE ? true : false));
		System.out.println("nChaves: " + nChaves);
		
		System.out.print("Chaves:");		
		for(int i = 0; i < nChaves; i++) {
			System.out.print(" " + chaves[i]);
		}
		
		System.out.println();
		
		if(!ehFolha()) {
			System.out.print("Filhos:");
			
			for(int i = 0; i < nChaves + 1; i++) {
				System.out.print(" " + filhos[i]);
			}
			
			System.out.println("\n");
		}
	}

	//*****************************
	//		    GETs e SETs
	//*****************************
  
  public boolean ehFolha() {
			return folha == TRUE;
		}
  
	public boolean isFull(){
		return this.nChaves == this.chaves.length - 1;
	}
}
