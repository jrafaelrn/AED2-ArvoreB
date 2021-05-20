public class MedidorTempo {

	private long inicio;
	
	public void comeca(String metodo) {
		System.out.println("Medindo o tempo (" + metodo + ")");
		//Marca o inicio do tempo.
		inicio = System.currentTimeMillis();
	}
	
	public void termina(String metodo) {
		//Pega o instante que terminou menos oque comecou e divide por 1000(milisegundo)
		System.out.println("Tempo gasto ("+ metodo + "): " + ((System.currentTimeMillis() - inicio) / 1000) + " segundo(s).");
	}
}