package examen1acda2223;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Menu {

	private static Connection conexion;
	private static Statement sentencia;
	private static int opcion = 0;
	private static Tareas tareas;
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		realizarConexion();

		while (opcion != 9) {
			System.out.println(
					"1 - Ejercicio 1\n2 - Ejercicio 2\n3 - Ejercicio 3\n4 - Ejercicio 4\n5 - Ejercicio 5\n6 - Ejercicio 6\nTeclee opcion:");
			opcion = sc.nextInt();

			switch (opcion) {
			case 1:
				tareas.ejercicio1();
				break;
			case 2:
				tareas.ejercicio2();
				break;
			case 3:
				tareas.ejercicio3();
				break;
			case 4:
				tareas.ejercicio4();
				break;
			case 5:
				break;
			case 6:
				break;
			}

		}

	}

	public static void realizarConexion() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		conexion = DriverManager.getConnection("jdbc:mysql://localhost/frutasyverduras", "root", "");
		sentencia = conexion.createStatement();

		tareas = new Tareas(sentencia);
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
