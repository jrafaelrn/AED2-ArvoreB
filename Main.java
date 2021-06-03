/****************************************************************************************					
*																						 *
****************	TRABALHO DE AED2: BUSCA E INSERCAO EM ARVORE B 		*****************
*																						                                            *
*		NOME: 	JOSE RAFAEL RODRIGUES NASCIMENTO		    N. USP: 	11847801			            * 
*		NOME: 	LUIZ FERNANDO CONCEIÇÃO DOS SANTOS      N. USP:		11840300		            	*
*																						                                            *
*		PROFESSOR:	ALEXANDRE DA SILVA FREIRE			TURMA: ACH2024-2021194 			              *
*																						                                            *
****************************************************************************************/

import java.io.IOException;
import java.io.File;


class Main {

	public static void main(String[] args) throws IOException {
		
		int ordem = 4;
		String nomeArvore = "arvore_b.txt";
		DebugArvore debug = new DebugArvore();
		ArvoreB arvB = new ArvoreB(ordem,nomeArvore);
    
    
		arvB.insereChave(1);
		arvB.insereChave(2);
		arvB.insereChave(3);
	    arvB.insereChave(4);
		arvB.insereChave(5);
		arvB.insereChave(6);
		arvB.insereChave(7);
		arvB.insereChave(8);
		arvB.insereChave(9);
    
	

	
	    debug.imprimeArvore(arvB);

		arvB.remove(4);
		
		if (arvB.buscaB(2))
			System.out.println("Número encontrado");
		else	
			System.out.println("Número não encontrado");

	}




/*
	//******************************************************
	//		DESCOMENTAR ABAIXO PARA RODAR A SIMULACAO 
	//******************************************************

	public static void main(String[] args) throws IOException {

		MedidorTempo medidorTotal = new MedidorTempo();

		Simulacao simulacao; 
		int tMaximo = 10000;
		int i = 2;

		medidorTotal.comeca("");

		//	Testa diversos valores para T
		while(i <= tMaximo){
			
			System.out.println("*** Simulando com T = " + i + "/" + tMaximo);
			
			simulacao = new Simulacao(i);
			simulacao.simulaTudo();

			//Padronizacao dos valores
			if (i < 10)
				i++;
			else
				if (i < 50)
					i+=10;
				else
					if (i < 100)
						i+=50;
					else
						if (i < 1000)
							i+=100;
						else
							i+=1000;

		}
		
	long tempoTotal = medidorTotal.termina();
	System.out.println("\n\nTempo TOTAL\t" + tempoTotal + "\n");
	System.out.println("FIIIM");

  }

*/
}