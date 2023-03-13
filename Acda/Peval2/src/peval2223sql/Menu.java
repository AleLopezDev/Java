package peval2223sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 
 * @author alex Clase que contiene un menu interactivo en el cual el usuario
 *         puede decidir que opcion usar
 */
public class Menu {

	private static int opcion = 0;
	private static Scanner sc = new Scanner(System.in);
	private static Tareas t;
	private static Connection conexion;
	private static Statement sentencia;

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		realizarConexion();

		while (opcion != 99) {
			System.out.println(
					"1 - Actualizar tabla jugadores con nuevos fichajes\n2 - Insertar un partido\n3 - Mostrar datos de los jugadores de un equipo\n4 - Visualizar numero de partidos jugados por cada jugador\n5 - Actualizar la posicion de todos los jugadores\n6 - Eliminar datos de un equipo\n99 - Cerrar");

			try {
				opcion = sc.nextInt();

				switch (opcion) {

				case 1:
					t.insertarNuevosFichajes();
					break;
				case 2:
					t.insertarPartidos();
					break;
				case 3:
					t.mostrarDatosJugadores();
					break;
				case 4:
					t.visualizarNumeroPartidos();
					break;
				case 5:
					t.actualizarPosicion();
					break;
				case 6:
					t.eliminarDatosEquipo();
					break;
				case 99:
					cerraConexion();
					opcion = 99;
					break;

				}
			} catch (InputMismatchException e) {
				System.out.println("Introduce una opción correcta");
				break;
			}

		}
	}

	public static void realizarConexion() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		conexion = DriverManager.getConnection("jdbc:mysql://localhost/peval2223", "root", "");
		sentencia = conexion.createStatement();

		t = new Tareas(sentencia);
	}

	public static void cerraConexion() {

		try {
			conexion.close();
			sentencia.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
