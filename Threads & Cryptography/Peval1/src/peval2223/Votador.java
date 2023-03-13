package peval2223;

import java.util.Arrays;

/**
 * @author Alejandro López Aguilar
 * @Date 26/10/2022 Clase Votador la cual extiende de la clase Thread junto a
 *       sus métodos Pasamos por parametro la tubería (Colegio) al constructor,
 *       para posteriormente llamar a sus métodos
 */
public class Votador extends Thread {

	Colegio t;

	public Votador(Colegio t) {
		this.t = t;
	}

	/**
	 * Método run heredado de la Clase Thread que ejecuta los métodos de la
	 * Tuberia(Colegio)
	 */
	@Override
	public void run() {

		t.checkearCenso(getName());
		
		if (Arrays.asList(t.getListaDnis()).contains(Integer.parseInt(getName()))) {
		t.votar(getName());
		}
	}

}