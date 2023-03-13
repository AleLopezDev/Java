package peval3;
/**
 * Clase Libros - Contiene sus atributos y métodos correspondientes
 * 
 * @author Alejandro Lopez - 2ºDAM
 * @date 16/11/2022
 */
public class Libros {

	private int codigo, numeroPaginas, year;
	private String nombre, editorial, autor, genero, paisAutor, precioLibro;

	/**
	 * 
	 * @param codigo        - tipo int - codigo libro
	 * @param nombre        - tipo string - nombre libro
	 * @param editorial     - tipo string - nombre editorial
	 * @param autor         - tipo string - nombre autor
	 * @param genero        - tipo string - genero libro
	 * @param paisAutor     - tipo string - pais autor
	 * @param numeroPaginas - tipo int - numero paginas libro
	 * @param year          - tipo int - año salida libro
	 * @param precioLibro   - tipo string - precio libro
	 */
	public Libros(int codigo, String nombre, String editorial, String autor, String genero, String paisAutor,
			int numeroPaginas, int year, String precioLibro) {

		this.codigo = codigo;
		this.nombre = nombre;
		this.editorial = editorial;
		this.autor = autor;
		this.genero = genero;
		this.paisAutor = paisAutor;
		this.numeroPaginas = numeroPaginas;
		this.year = year;
		this.precioLibro = precioLibro;

	}

	@Override
	public String toString() {
		return "Codigo Libro: " + codigo;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getPrecioLibro() {
		return precioLibro;
	}

	public void setPrecioLibro(String precioLibro) {
		this.precioLibro = precioLibro;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}