import java.io.IOException;


class Main {

	public static void main(String[] args) throws IOException {

		MedidorTempo medidor = new MedidorTempo();
		DebugArvore debug = new DebugArvore();
		int ordem = 2;

		medidor.comeca("ArvoreB");

		ArvoreB arvB = new ArvoreB(ordem, "arv.txt");
		No no = arvB.getRaiz();

		debug.imprimeArvore(arvB);
   
   /*
    	arvB.insereChave(9);
		arvB.insereChave(10);
		arvB.insereChave(6);
		arvB.insereChave(3);
		arvB.insereChave(20);
		arvB.insereChave(2);
		arvB.insereChave(5);
		arvB.insereChave(8);

		System.out.println(" ----------------------- ");
		System.out.print("RAIZ DA ARVORE: ");
		arvB.mostrar();
		*/

  		
		
    

    
  /* 
    if (arvB.buscaB(11)) {
      System.out.println("\nCHAVE ESTÁ PRESENTE");
    } else {
      System.out.println("\nCHAVE NÃO ESTÁ");
    }
    
		medidor.termina("ArvoreB");		
	  arvB.obterMemoriaUsada();		
   */
  /*
		System.out.println("\n...... Imprimindo arvore da RAM ......");
		debug.imprimeArvore(arvB);
    
    
		System.out.println("\n...... Imprimindo arvore do DISCO ......");
		ArvoreB arvB2 = new ArvoreB(3, "arv.txt");
		debug.imprimeArvore(arvB2);
    
    arvB2.insereChave(2);
    
*/

/*
		for(int i = 0; i < 5; i++){
			no.chaves[i] = i;
			no.nChaves++;
		}
*/


/*
		//Convert c = new Convert();
		//c.converte("arv.txt");



/*
		try{
			Simulacao simulacao = new Simulacao();

			//	Testa diversos valores para T
			for(int i = 1; i <= 1000; i+=50){
				System.out.println("Simulando com T = " + i);
				simulacao.simula(i);
			}
	  	}
		catch (Exception e){
			System.out.println("ERRO NA SIMULAÇÃO");
	      	e.printStackTrace();

		}
*/
  }

}