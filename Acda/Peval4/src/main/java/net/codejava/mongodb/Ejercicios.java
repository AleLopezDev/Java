package net.codejava.mongodb;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.*;

/**
 * @author alexrose
 * @version 1.0 Clase Ejercicios - Clase que se encarga de realizar consultas,
 *          inserciones, eliminaciones y modificaciones sobre la base de datos
 */
public class Ejercicios {

	static MongoCollection coleccion;
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		Conexion cone = new Conexion();
		MongoDatabase db = cone.getDb();
		coleccion = db.getCollection("datosAlimentos");

		menu();
	}

	/**
	 * Menu que permite escoger una opcion, insertar, eliminar, modificar tiempo de
	 * elaboracion o consultar, o salir
	 */
	public static void menu() {

		int opcion = 0;

		while (opcion != 9) {
			System.out
					.println("1 - Insertar\n2 - Eliminar\n3 - Modificar tiempo elaboracion\n4 - Consultar\n9 - Salir");
			opcion = sc.nextInt();

			switch (opcion) {
			case 1:
				insertar();
				break;
			case 2:
				eliminar();
				break;
			case 3:
				modificarTiempoElaboracion();
				break;
			case 4:
				consultas();
				break;
			case 9:
				System.out.println("Saliendo...");
				opcion = 9;
			}
		}
	}

	/**
	 * Permite realizar distintas consultas sobre la base de datos
	 */
	public static void consultas() {
		System.out.println(
				"\tA - Visualizar todos los ingredientes de una receta introduciendo por teclado su nombre.\n\tB - Dar el nombre de todas las recetas que contengan huevos de ingredientes y tengan\n"
						+ "\tmás de 500 calorías.\n\tC - Dar el nombre de todas las recetas y el tiempo de elaboración, cuya elaboración sea\n"
						+ "\tinferior a 1 hora y que sean primer plato o segundo plato.\n\tD - Dar el nombre de las recetas y los pasos a seguir en su elaboración, que haya que\n"
						+ "\tdejarlas reposar y que sean platos únicos o primeros platos.\n\tE - Visualizar todos los datos de las recetas que se realicen en 5 o menos pasos que sean\n"
						+ "\tde una dificultad alta (difícil).");

		System.out.println("Teclee opción :");
		String opcion = sc.next();
		opcion = opcion.toUpperCase();

		switch (opcion) {
		case "A":

			FindIterable<Document> resultDocumentLista = coleccion.find();
			MongoCursor<Document> cursorLista = resultDocumentLista.cursor();

			System.out.println("\nLista de alimentos : ");
			while (cursorLista.hasNext()) {
				Document documento = (Document) cursorLista.next();
				System.out.println(documento.get("nombre"));
			}

			reiniciarBuffer();
			System.out.println("Introduce el nombre del alimento : ");
			String nombre = sc.nextLine();

			Document miDocumento = new Document("nombre", nombre);
			FindIterable<Document> resultDocument = coleccion.find(miDocumento);
			MongoCursor<Document> cursor = resultDocument.cursor();

			while (cursor.hasNext()) {
				Document documento = (Document) cursor.next();
				ArrayList<Document> ingredientes = (ArrayList<Document>) documento.get("ingredientes");

				System.out.println("\n------------------------\n");

				for (Document ingrediente : ingredientes) {
					System.out.println("Nombre: " + ingrediente.get("nombre"));
					System.out.println("Cantidad: " + ingrediente.get("cantidad"));
					System.out.println("Unidades: " + ingrediente.get("unidades"));
					System.out.println("\n------------------------\n");
				}
			}
			System.out.println();
			break;
		case "B":
			FindIterable<Document> resultDocumentB = coleccion.find();
			MongoCursor<Document> cursorB = resultDocumentB.cursor();

			System.out.println("Lista de alimentos : ");
			while (cursorB.hasNext()) {
				Document documento = (Document) cursorB.next();
				ArrayList<Document> ingredientes = (ArrayList<Document>) documento.get("ingredientes");

				for (Document ingrediente : ingredientes) {
					if (ingrediente.get("nombre").equals("huevos") && (int) documento.get("calorias") > 500) {
						System.out.println(documento.get("nombre"));
					}
				}
			}
			System.out.println();
			break;
		// Preguntar a enrique si pasa algo porque se muestre dos veces ya que si es
		// primer plato y segundo plato se repite
		case "C":
			BasicDBObject query = new BasicDBObject();
			BasicDBObject query2 = new BasicDBObject();

			BasicDBList obj = new BasicDBList();
			List<String> lista = new ArrayList<String>();
			lista.add("primer plato");
			lista.add("segundo plato");
			obj.add(new BasicDBObject("tipo", new BasicDBObject("$in", lista)));

			BasicDBList obj2 = new BasicDBList();
			obj2.add(
					new BasicDBObject("tiempo.unidad", "minutos").append("tiempo.valor", new BasicDBObject("$lt", 60)));
			obj2.add(new BasicDBObject("tiempo.unidad", "horas").append("tiempo.valor", new BasicDBObject("$lt", 1)));

			// Añadimos OR a las 2 consultas de arriba
			query.put("$and", obj);
			query2.put("$or", obj2);

			BasicDBObject finalquery = new BasicDBObject();
			finalquery.put("$and", Arrays.asList(query, query2));

			FindIterable resultDocumentC = coleccion.find(finalquery);
			MongoCursor<Document> cursorC = resultDocumentC.cursor();

			while (cursorC.hasNext()) {
				Document documento = (Document) cursorC.next();
				Document tiempo = (Document) documento.get("tiempo");
				System.out.println(documento.get("nombre") + " - " + tiempo.get("valor") + " " + tiempo.get("unidad"));
			}
			System.out.println();
			break;

		case "D":
			BasicDBObject queryD = new BasicDBObject();
			queryD.put("tipo", new BasicDBObject("$in", Arrays.asList("primer plato", "plato único")));
			queryD.put("pasos.elaboracion", new BasicDBObject("$regex", "reposar").append("$options", "i"));

			FindIterable<Document> resultDocumentD = coleccion.find(queryD);
			MongoCursor<Document> cursorD = resultDocumentD.cursor();

			while (cursorD.hasNext()) {
				Document documento = (Document) cursorD.next();
				System.out.println("\n" + documento.get("nombre"));

				System.out.println("Lista de pasos :");
				ArrayList<Document> pasos = (ArrayList<Document>) documento.get("pasos");
				for (Document paso : pasos) {
					System.out.println("\t" + paso.get("elaboracion"));
				}
			}
			System.out.println();
			break;
		case "E":
			BasicDBObject queryE = new BasicDBObject();
			queryE.put("dificultad", "Difícil");
			queryE.put("$expr", new BasicDBObject("$lt", Arrays.asList(new BasicDBObject("$size", "$pasos"), 6)));

			FindIterable resultDocumentE = coleccion.find(queryE);
			MongoCursor<Document> cursorE = resultDocumentE.cursor();

			while (cursorE.hasNext()) {
				Document documento = (Document) cursorE.next();
				System.out.println("\n------------------------\n");
				System.out.println(documento.get("nombre"));

				ArrayList<String> tipos = (ArrayList<String>) documento.get("tipo");
				for (String item : tipos) {
					System.out.println("Tipo - " + item);
				}

				System.out.println("Dificultad - " + documento.get("dificultad"));
				System.out.println("Lista Ingredientes: ");
				ArrayList<Document> ingredientes = (ArrayList<Document>) documento.get("ingredientes");
				for (Document ingrediente : ingredientes) {
					System.out.println(ingrediente.get("nombre") + " - " + ingrediente.get("cantidad") + " "
							+ ingrediente.get("unidades"));
				}

				System.out.println("Calorias - " + documento.get("calorias"));
				ArrayList<Document> pasos = (ArrayList<Document>) documento.get("pasos");
				for (Document paso : pasos) {
					System.out.println(paso.get("orden") + " - " + paso.get("elaboracion"));
				}

				Document tiempo = (Document) documento.get("tiempo");
				System.out.println("Valor: " + tiempo.get("valor"));
				System.out.println("Unidad: " + tiempo.get("unidad"));

				System.out.println("Electrodomestico - " + documento.get("electrodomestico"));
				System.out.println("\n------------------------\n");
			}

			break;
		}

	}

	/**
	 * Modificar el tiempo de elaboracion de un alimento
	 */
	public static void modificarTiempoElaboracion() {
		FindIterable<Document> resultDocument = coleccion.find();
		MongoCursor<Document> cursor = resultDocument.cursor();
		ArrayList<String> listaAlimentos = new ArrayList<String>();

		System.out.println("\nLista de alimentos : ");
		while (cursor.hasNext()) {
			Document documento = (Document) cursor.next();
			System.out.println(documento.get("nombre"));
			listaAlimentos.add((String) documento.get("nombre"));
		}
		cursor.close();

		reiniciarBuffer();

		System.out.println("Escribe el NOMBRE del alimento que quieres modificar : ");
		String nombre = sc.nextLine();

		// Comprobar que el nombre introducido esta en la lista de alimentos
		if (!listaAlimentos.contains(nombre)) {
			System.out.println("El nombre introducido no esta en la lista de alimentos");
			return;
		}

		Document documento = new Document("nombre", nombre);

		int tiempoElaboracion = 0;
		try {
			System.out.println("Escribe el nuevo tiempo de elaboracion : ");
			tiempoElaboracion = sc.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Error, no has introducido un numero");
			return;
		}
		// Se usa el next para evitar que el usuario ponga mas cosas a parte de minutos
		// o horas
		System.out.println("Escribe la nueva unidad: (Segundos / Minutos / Horas)");
		String unidad = sc.next();

		// Actualizar tiempoElaboracion y unidad en la misma linea
		coleccion.updateOne(documento,
				new Document("$set", new Document("tiempo.valor", tiempoElaboracion).append("tiempo.unidad", unidad)));
	}

	public static void eliminar() {
		FindIterable<Document> resultDocument = coleccion.find();
		MongoCursor<Document> cursor = resultDocument.cursor();
		ArrayList<String> listaAlimentos = new ArrayList<>();

		System.out.println("\nLista de alimentos : ");
		while (cursor.hasNext()) {
			Document documento = (Document) cursor.next();
			System.out.println(documento.get("nombre"));
			listaAlimentos.add((String) documento.get("nombre"));
		}

		reiniciarBuffer();
		System.out.println("Introduce el NOMBRE del alimento que quieres eliminar : ");
		String nombre = sc.nextLine();

		if (!listaAlimentos.contains(nombre)) {
			System.out.println("El alimento no existe");
		} else {
			Document miDocumento = new Document();
			miDocumento.append("nombre", nombre);
			coleccion.deleteOne(miDocumento);
			System.out.println("Alimento eliminado");
		}
	}

	/**
	 * Metodo para insertar un nuevo alimento en la base de datos
	 */
	public static void insertar() {
		List<String> listaTipos = new ArrayList<>();
		List<Document> ingredientes = new ArrayList<>();
		List<Document> listaPasos = new ArrayList<>();
		String tipo = "", ingrediente = "", pasos = "";
		boolean continuar = true;

		// Rellenar el array de tipos
		while (tipo == "") {
			reiniciarBuffer();
			try {
				System.out.println("Introduce el tipo de plato (Postre / Primer plato ...) : ");
				tipo = sc.nextLine();

				listaTipos.add(tipo);

				System.out.println("Quieres introducir otro tipo (S/N) : ");
				String respuesta = sc.next();

				if (respuesta.equalsIgnoreCase("N")) {
					break;
				} else {
					tipo = "";
				}
			} catch (InputMismatchException e) {
				System.out.println("Error, introduce un tipo de plato valido");
				tipo = "";
			}
		}

		// Dificultad
		System.out.println("Introduce la dificultad del alimento (Baja / Media / Alta) : ");
		String dificultad = sc.next();

		reiniciarBuffer();

		// Nombre
		System.out.println("Introduce el nombre del alimento : ");
		String nombre = sc.nextLine();

		// Ingredientes
		while (ingrediente == "") {
			int cantidad = -1;
			Document miDocumento = new Document();

			// Nombre
			System.out.println("Introduce el nombre del ingrediente :");
			String nombreAlimento = sc.nextLine();

			while (cantidad == -1) {
				try {
					// Cantidad
					System.out.println("Introduce la cantidad del ingrediente : ");
					cantidad = sc.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("Error, introduce un numero valido");
					cantidad = -1;
					reiniciarBuffer();
				}
			}

			reiniciarBuffer();

			// Unidades
			System.out.println("Introduce las unidades del ingrediente : ");
			String unidades = sc.nextLine();

			miDocumento.append("nombre", nombreAlimento);
			miDocumento.append("cantidad", cantidad);
			miDocumento.append("unidades", unidades);

			System.out.println("Quieres añadir otro ingrediente (S/N) : ");
			String respuesta = sc.next();

			if (respuesta.equalsIgnoreCase("N")) {
				ingredientes.add(miDocumento);
				break;
			} else {
				ingredientes.add(miDocumento);
				ingrediente = "";
				reiniciarBuffer();
			}
		}

		// Calorias
		double calorias = -1;
		while (calorias == -1) {
			try {
				System.out.println("Introduce las calorias del alimento : ");
				calorias = sc.nextDouble();
			} catch (InputMismatchException e) {
				System.out.println("Error, introduce un numero valido");
				calorias = -1;
				reiniciarBuffer();
			}
		}

		// Pasos
		while (pasos == "") {
			Document miDocumento = new Document();

			int orden = -1;
			while (orden == -1) {
				try {
					System.out.println("Introduce el orden del paso: (1 / 2 / 3 ...) :");
					orden = sc.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("Error, introduce un numero valido");
					orden = -1;
					reiniciarBuffer();
				}
			}

			reiniciarBuffer();

			System.out.println("Introduce la elaboracion");
			String elaboracion = sc.nextLine();

			miDocumento.append("orden", orden);
			miDocumento.append("elaboracion", elaboracion);

			System.out.println("Quieres añadir otro paso (S/N) : ");
			String respuesta = sc.next();

			if (respuesta.equalsIgnoreCase("N")) {
				listaPasos.add(miDocumento);
				break;
			} else {
				listaPasos.add(miDocumento);
				pasos = "";
			}
			reiniciarBuffer();
		}

		// Documento Tiempo
		Document tiempo = new Document();
		double tiempoPreparacion = -1;
		while (tiempoPreparacion == -1) {
			try {
				System.out.println("Introduce el tiempo de preparacion del alimento : ");
				tiempoPreparacion = sc.nextDouble();
			} catch (InputMismatchException e) {
				System.out.println("Error, introduce un numero valido");
				tiempoPreparacion = -1;
				reiniciarBuffer();
			}
		}

		System.out.println("Introduce la unidad (minutos / horas) :");
		String unidad = sc.next();

		tiempo.append("tiempoPreparacion", tiempoPreparacion);
		tiempo.append("unidad", unidad);

		reiniciarBuffer();

		// Electrodomestico
		System.out.println("Introduce el electrodomestico donde se prepara el alimento : ");
		String electrodomestico = sc.nextLine();

		Document miDocumento = new Document();
		miDocumento.append("tipo", listaTipos);
		miDocumento.append("dificultad", dificultad);
		miDocumento.append("nombre", nombre);
		miDocumento.append("ingredientes", ingredientes);
		miDocumento.append("calorias", calorias);
		miDocumento.append("pasos", listaPasos);
		miDocumento.append("tiempo", tiempo);
		miDocumento.append("electrodomestico", electrodomestico);
		coleccion.insertOne(miDocumento);
	}

	public static void reiniciarBuffer() {
		sc.nextLine();
	}

}
