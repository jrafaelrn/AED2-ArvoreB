import java.io.IOException;

public class DebugArvore{

	public void imprimeArvore(ArvoreB arv) throws IOException{

		System.out.println("\n\nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n");
		System.out.println("Iniciando impressao de Arvore B ...\n");

		System.out.println("Ordem: " + arv.getOrdemArvore());
		No no = arv.getRaiz();
		System.out.println("End do Nó RAIZ: " + no.endereco + " -- " + no);

		System.out.println("\n-----------------------------------");
		System.out.println("\n ** NO RAIZ **");		
		no.imprime();
		System.out.println("\n-----------------------------------");

		System.out.println("\n ** FILHOS **");

		imprimeFilhosRecursivos(arv, no);

		System.out.println("\nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

	}


	private void imprimeFilhosRecursivos(ArvoreB arv, No no) throws IOException{

		if(!no.ehFolha()){

			System.out.println("\n\t*** Imprimindo filho do " + no + " ***");
			
			for(int f : no.getFilhos()){

				if (f == -1) break;
				
				System.out.println("\n\n-- Lendo filho na posicao " + f);
				No filho = arv.leNo(f);
				filho.imprime();
				imprimeFilhosRecursivos(arv, filho);

			}

		}


	}

}