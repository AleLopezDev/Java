package peval3;
/**
 * Clase Prestamos - Contiene sus atributos y métodos correspondientes
 * 
 * @author Alejandro Lopez - 2ºDAM
 * @date 16/11/2022
 */
public class Prestamos {

	int numeroPedido;
	Libros Libro;
	Usuario Usuario;
	String fechaSalida, fechaMaxDevolucion, fechaDevolucion;

	/**
	 * 
	 * @param numeroPedido       - tipo int - numero pedido de prestamo
	 * @param Libro              - tipo Libro
	 * @param Usuario            - tipo Usuario
	 * @param fechaSalida        - tipo String - fecha salida
	 * @param fechaMaxDevolucion - tipo String - fecha maxima devolucion
	 * @param fechaDevolucion    - tipo String - fecha devolucion
	 */
	public Prestamos(int numeroPedido, Libros Libro, Usuario Usuario, String fechaSalida, String fechaMaxDevolucion,
			String fechaDevolucion) {
		this.numeroPedido = numeroPedido;
		this.Libro = Libro;
		this.Usuario = Usuario;
		this.fechaSalida = fechaSalida;
		this.fechaMaxDevolucion = fechaMaxDevolucion;
		this.fechaDevolucion = fechaDevolucion;
	}

	public int getNumeroPedido() {
		return numeroPedido;
	}

	public String getFechaDevolucion() {
		return fechaDevolucion;
	}

	public Libros getLibro() {
		return Libro;
	}

	public void setLibro(Libros libro) {
		Libro = libro;
	}

	public Usuario getUsuario() {
		return Usuario;
	}

	public void setUsuario(Usuario usuario) {
		Usuario = usuario;
	}

	public String getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(String fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public String getFechaMaxDevolucion() {
		return fechaMaxDevolucion;
	}

	public void setFechaMaxDevolucion(String fechaMaxDevolucion) {
		this.fechaMaxDevolucion = fechaMaxDevolucion;
	}

	public void setFechaDevolucion(String fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}

	@Override
	public String toString() {
		return "Numero pedido: " + numeroPedido + " - " + Libro.toString() + " - " + Usuario.toString() + " - "
				+ fechaSalida + " - " + fechaDevolucion + " - " + fechaMaxDevolucion;
	}

}