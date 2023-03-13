package peval2223;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Alejandro López Aguilar
 * @Date 26/10/2022 Clase Colegio que actúa como tubería y contienen todos los
 *       metodos que van a ser usado por los hilos
 */
public class Colegio {

	int y = 0;
	private Integer[] listaDnis = { 1, 2, 3, 4, 6, 8, 10, 11, 13, 14, 15, 18, 19, 20, 22, 23, 24, 28, 29, 30 };
	int votosPartidoA = 0, votosPartidoB = 0;
	private ArrayList<Integer> colaVotar = new ArrayList<>();
	private boolean haAcabado = false;

	public Colegio() {

	}

	/**
	 * Método el cual checkea el censo y va añadiendo a las personas a una cola para
	 * votar según el orden de llegada
	 * 
	 * @param String name - Nombre del Hilo
	 */
	public void checkearCenso(String nombre) {

		System.out.println("La persona con DNI: " + nombre + " ha llegado y esta mirando el censo");

		// Comprueba que este en el censo, si se encuentra en el censo se le añade a la
		// cola de votar
		if (Arrays.asList(listaDnis).contains(Integer.parseInt(nombre))) {

			synchronized (colaVotar) {
				System.out.println(nombre + " ha comprobado y efectivamente está en el censo");
				colaVotar.add(Integer.parseInt(nombre));
				System.out.println("La persona con DNI " + nombre
						+ " se ha puesto en la cola de votos, su posición es: " + (colaVotar.size()) + "\n");
			}

		} else {
			System.out.println("\n" + nombre + " ha confirmado que NO está en el censo\n");
		}

	}

	/**
	 * Método el cual si el hilo entrante esta en la lista de dnis, espera o vota
	 * segun el orden de llegada anterior
	 * 
	 * @param String name - Nombre del Hilo
	 */
	public synchronized void votar(String name) {

		// Si la primera persona en llegar no es la que esta en la cola

		while (colaVotar.get(0) != Integer.parseInt(name)) {

			try {
				System.out.println("La persona con DNI " + name + " se ha intentado colar");
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("\nLa persona con DNI: " + name + "  esta votando");

		try {
			Thread.sleep((int) Math.random() * 3000 + 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int partidoVotado = (int) (Math.random() * 2 + 1);

		// Si sale 1 voto para el partido A - Si sale 2 voto para el partido B
		switch (partidoVotado) {

		case 1:
			votosPartidoA++;
			// System.out.println("Votos partido A : " + votosPartidoA);
			break;
		case 2:
			votosPartidoB++;
			// System.out.println("Votos partido B : " + votosPartidoB);
			break;
		}

		colaVotar.remove(0);
		System.out.println(name + " se ha marchado del colegio\n");
		notifyAll();

		if (y == colaVotar.size()) {
			haAcabado = true;
		}

	}

	/**
	 * Método el cual realiza un recuento Total de todos los votos una vez que se
	 * haya acabado la ejecución de todos los Hilos
	 * 
	 * @param String nanme - Nombre del Hilo
	 */

	public void recuentosTotales() {

		System.out.println("\nRESULTADOS TOTALES\nVotos TOTALES partido A: " + getVotosPartidoA()
				+ "\nVotos TOTALES Partido B: " + getVotosPartidoB() + "\n");

	}

	/**
	 * Método el cual cada 10 segundos va mostrando por pantalla el resultado de los
	 * votos
	 */
	public void mostrarResultadosParciales() {
		System.out.println("\nRESULTADOS ELECTORALES PARCIALES\nVotos partido A: " + votosPartidoA
				+ "\nVotos Partido B: " + votosPartidoB + "\n");
	}

	public int getVotosPartidoA() {
		return votosPartidoA;
	}

	public int getVotosPartidoB() {
		return votosPartidoB;
	}

	public Integer[] getListaDnis() {
		return listaDnis;
	}

	public boolean isHaAcabado() {
		return haAcabado;
	}

	public ArrayList<Integer> getColaVotar() {
		return colaVotar;
	}


	
	

}