package peval2223;

/**
 * @author Alejandro López Aguilar
 * @Date 26/10/2022 Clase Tiempo la cual extiende de la clase Thread junto a sus
 *       métodos Pasamos por parametro la tubería (Colegio) al constructor, para
 *       posteriormente llamar a sus métodos, cada 10 segundos llamara al método
 *       de mostrar resultados parciales
 */
public class Tiempo extends Thread {

	Colegio t;

	public Tiempo(Colegio t) {
		this.t = t;
	}

	/*
	 * Método run heredado de la Clase Thread que ejecuta los métodos de la
	 * Tuberia(Colegio)
	 */
	@Override
	public void run() {

		while (t.isHaAcabado() == false) {

			try {
				sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (t.isHaAcabado() == false) {

				t.mostrarResultadosParciales();
			}

		}

	}

}
