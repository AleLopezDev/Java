package examen1acda2223;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

public class Tareas {

	// Variables de configuracion

	static String rutaConfiguracion = "C:\\examen1acda2223\\"; // Cambiar solo esto, ya configurado para que no haga
																// falta poner el archivo

	private Statement sentencia;
	private static Scanner sc = new Scanner(System.in);

	public Tareas(Statement sentencia) {
		this.sentencia = sentencia;
	}

	public void ejercicio1() throws IOException {

		// Revisarlo

		System.out.println("Introduce el nombre del producto");
		String nombreProducto = sc.next();

		String ruta = rutaConfiguracion + File.separator + nombreProducto + ".odb";

		File f = new File(ruta);

		ObjectOutputStream escribir = new ObjectOutputStream(new FileOutputStream(f));

		try {
			// Importe Ventas = Kilos * precio

			ResultSet nomProducto = sentencia.executeQuery(
					"SELECT NomProducto,NombreGrupo,Kilos * Precio,Kilos,productos.Precio,vendedores.NombreVendedor,vendedores.NIF,vendedores.Poblacion from productos,grupos,ventas,vendedores where grupos.IdGrupo = productos.IdGrupo and productos.IdProducto = ventas.CodProducto and ventas.CodVendedor = vendedores.IdVendedor and NomProducto like '"
							+ nombreProducto + "' GROUP BY vendedores.NombreVendedor; ");

			System.out.println(
					("SELECT NomProducto,NombreGrupo,Kilos * Precio,Kilos,productos.Precio,vendedores.NombreVendedor,vendedores.NIF,vendedores.Poblacion from productos,grupos,ventas,vendedores where grupos.IdGrupo = productos.IdGrupo and productos.IdProducto = ventas.CodProducto and ventas.CodVendedor = vendedores.IdVendedor and NomProducto like '"
							+ nombreProducto + "' GROUP BY vendedores.NombreVendedor; "));

			while (nomProducto.next()) {

				escribir.writeObject(nomProducto.getString(1) + nomProducto.getString(2) + nomProducto.getInt(3)
						+ nomProducto.getInt(4) + nomProducto.getDouble(5) + nomProducto.getString(6)
						+ nomProducto.getString(7) + nomProducto.getString(8));

			}

			System.out.println("Registros insertados");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ejercicio2() throws SQLException {

		System.out.println("Introduce la poblacion");
		String poblacion = sc.next();

		System.out.println("Introduce la cantidad minima");
		int cantidad = sc.nextInt();

		// Realizar consulta vendedor y nif
		ResultSet vendedor = sentencia
				.executeQuery("SELECT NombreVendedor,NIF from vendedores,ventas where Poblacion like'" + poblacion
						+ "' and kilos > " + cantidad + " GROUP by NombreVendedor ");

		System.out.println("SELECT NombreVendedor,NIF from vendedores,ventas where Poblacion like'" + poblacion
				+ "' and kilos > " + cantidad + " GROUP by NombreVendedor ");

		while (vendedor.next()) {

			System.out.println("Vendedor: " + vendedor.getString(1) + " - " + "DNI: " + vendedor.getString(2));
			System.out.println("----------------");
		}

		vendedor.close();

		ResultSet resultados = sentencia.executeQuery(
				"select IdProducto,NomProducto,Precio,count(Kilos),Fecha from productos,ventas,vendedores WHERE productos.IdProducto = ventas.CodProducto and ventas.CodVendedor = vendedores.IdVendedor and Poblacion like '"
						+ poblacion + "'  GROUP by NomProducto");

		while (resultados.next()) {
			System.out.println("ID Producto:" + resultados.getInt(1) + " Nombre Producto:" + resultados.getString(2)
					+ " Precio:" + resultados.getDouble(3) + " Kilos:" + resultados.getInt(4) + " Fecha:"
					+ resultados.getString(5));
		}

		resultados.close();

	}

	public void ejercicio3() throws SQLException {

		System.out.println("Introduce el nombre de grupo: ");
		String nombre = sc.next();

		// Actualizamos en un 2%
		sentencia.executeUpdate(
				"UPDATE productos INNER JOIN grupos ON productos.IdGrupo = grupos.IdGrupo and grupos.NombreGrupo like'"
						+ nombre + "' set productos.Precio = productos.Precio + productos.Precio *0.2 ");

		System.out.println("Actualizacion realizada con exito");

	}

	public void ejercicio4() throws SQLException {
		Objects<Vendedores> ven;
		Vendedores vend = null;
		Productos pr = null;

		ODB odb = ODBFactory.open(rutaConfiguracion + File.separator + "frutasyverduras.neo");

		System.out.println("Introduce la poblacion");
		String poblacion = sc.next();

		System.out.println("Introduce la cantidad minima");
		int cantidad = sc.nextInt();

		// Realizar consulta vendedor y nif
		ResultSet vendedores = sentencia
				.executeQuery("SELECT NombreVendedor,NIF from vendedores,ventas where Poblacion like'" + poblacion
						+ "' and kilos > " + cantidad + " GROUP by NombreVendedor ");

		while (vendedores.next()) {
			vend = new Vendedores(vendedores.getString(1), vendedores.getString(2));
			odb.store(new Vendedores(vendedores.getString(1), vendedores.getString(2)));
		}

		ResultSet rs = sentencia.executeQuery(
				"select IdProducto,NomProducto,Precio from productos,ventas,vendedores WHERE productos.IdProducto = ventas.CodProducto and ventas.CodVendedor = vendedores.IdVendedor and Poblacion like '"
						+ poblacion + "'  GROUP by NomProducto");

		while (rs.next()) {
			odb.store(new Productos(rs.getInt(1), rs.getString(2), rs.getDouble(3)));
		}

		ResultSet rsV = sentencia.executeQuery("select Fecha,Kilos,CodProducto from ventas");

		while (rsV.next()) {

			CriteriaQuery productos = new CriteriaQuery(Productos.class, Where.equal("idProducto", rsV.getInt(3)));
			Objects<Productos> pro = odb.getObjects(productos);
			if (pro.hasNext()) {
				pr = (Productos) pro.getFirst();
			}

			odb.store(new Ventas(rsV.getString(1), pr, rsV.getInt(2), vend));

			vendedores.close();
			rs.close();
		}

		System.out.println("Datos introducidos con exito");
		odb.close();
		rs.close();

	}

	public void ejercicio5() {
		ODB odb = ODBFactory.open(rutaConfiguracion + File.separator + "frutasyverduras.neo");

		System.out.println("Introduce el nombre del vendedor");
		String nombre = sc.next();

		CriteriaQuery vendedoresQuery = new CriteriaQuery(Productos.class,
				Where.equal("Vendedores.nombreVendedor", nombre));

		Objects<Productos> productos = odb.getObjects(vendedoresQuery);
		while (productos.hasNext()) {
			Productos p = productos.next();
			System.out.println(p.getIdProducto() + p.getNombreProducto() + p.getPrecioProducto());
		}

	}

}
