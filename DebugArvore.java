public class DebugArvore{

	public void imprimeArvore(ArvoreB arv){

		System.out.println("Iniciando impressao de Arvore B ...\n");

		System.out.println("Ordem: " + arv.getOrdemArvore());
		System.out.println("End raiz: " + arv.getEnderecoRaiz());

		System.out.println("\n ** NO RAIZ **");
		No no = arv.getRaiz();
		no.imprime();
		System.out.println("\nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

	}

}