package peval2223;

/**
 * @author Alejandro López Aguilar Clase que crea los hilos (30) y los inicia
 *
 */
public class Main {

	private static Colegio t = new Colegio();
	static Votador v;

	public static void main(String[] args) {

		for (int i = 1; i <= 30; i++) {
			v = new Votador(t);
			v.setName(Integer.toString(i));
			v.start();
		}

		Tiempo tiempo = new Tiempo(t);
		tiempo.start();

		while (tiempo.isAlive() || v.isAlive()) {
			//Esta en espera
		}
		
		t.recuentosTotales();
	}

}