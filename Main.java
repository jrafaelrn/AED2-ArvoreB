/*****************************************************************************************				
*																						 *
****************	TRABALHO DE AED2: BUSCA E INSERCAO EM ARVORE B 		******************
*											               								 *
*		NOME: 	JOSE RAFAEL RODRIGUES NASCIMENTO		    N. USP: 	11847801		 * 
*		NOME: 	LUIZ FERNANDO CONCEIÇÃO DOS SANTOS      N. USP:		11840300		     *
*																						 *
*		PROFESSOR:	ALEXANDRE DA SILVA FREIRE			TURMA: ACH2024-2021194 			 *
*																						 *
****************************************************************************************/

import java.io.IOException;
import java.io.File;


class Main {

	public static void main(String[] args) throws IOException {

		try{
			simula();
		}
		catch (Exception e){
			return;
		}

		/*
		if (args.length != 0){
			if (args[0].equals("simulacao")){
				simula();
				return;
			}
		}
		

		int ordem = 2;
		String nomeArvore = "arvore_b.txt";
		DebugArvore debug = new DebugArvore();

		File del = new File(nomeArvore);
		del.delete();

		ArvoreB arvB = new ArvoreB(ordem,nomeArvore);
    
    
		arvB.insereChave(10);
		arvB.insereChave(20);
		arvB.insereChave(30);
	  	arvB.insereChave(40);
		arvB.insereChave(2);
		arvB.insereChave(4);
		arvB.insereChave(6);
		arvB.insereChave(8);
		arvB.insereChave(10);
	  	arvB.insereChave(3);
		arvB.insereChave(5);
		arvB.insereChave(7);
		arvB.insereChave(9);
		arvB.insereChave(13);
		arvB.insereChave(25);
		arvB.insereChave(35);
	  	arvB.insereChave(45);
		arvB.insereChave(55);
		arvB.insereChave(65);
		arvB.insereChave(1);

		debug.imprimeArvore(arvB);
	
		arvB.remove(1);
		debug.imprimeArvore(arvB);
    	System.out.println("TERMINOU A PRIMEIRA");	
    	arvB.remove(4);	
		debug.imprimeArvore(arvB);
		arvB.remove(10);	
		debug.imprimeArvore(arvB);
		arvB.remove(13);	
		debug.imprimeArvore(arvB);
		arvB.remove(40);	
		debug.imprimeArvore(arvB);
		arvB.remove(20);	
		debug.imprimeArvore(arvB);
		arvB.remove(10);	
		debug.imprimeArvore(arvB);
		arvB.remove(9);	
		

		debug.imprimeArvore(arvB);

		
		if (arvB.buscaB(200))
			System.out.println("\nNúmero encontrado");
		else	
			System.out.println("\nNúmero não encontrado");

		*/
	}





	//******************************************************
	//		DESCOMENTAR ABAIXO PARA RODAR A SIMULACAO 
	//******************************************************

	public static void simula() throws IOException {


		MedidorTempo medidorTotal = new MedidorTempo();

		Simulacao simulacao; 
		int tMaximo = 10000;
		int i = 2;

		medidorTotal.comeca("");

		//	Testa diversos valores para T
		while(i <= tMaximo){
			
			System.out.println("\n*** Simulando com T = " + i + "/" + tMaximo);
			
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


}