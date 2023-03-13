package peval3;
/**
 * Clase Usuario - Contiene sus atributos y métodos correspondientes
 * 
 * @author Alejandro Lopez - 2ºDAM
 * @date 16/11/2022
 * 
 */
public class Usuario {

	private int codigoUsuario;
	private String nombre, apellidos, dni, domicilio, poblacion, provincia, fechaNacimiento;

	/**
	 * 
	 * @param codigoUsuario - tipo int - codigo usuario
	 * @param nombre - tipo string - nombre usuario
	 * @param apellidos - tipo string - apellidos usuario
	 * @param dni - tipo string - dni usuario
	 * @param domicilio - tipo string - domicilio usuario
	 * @param poblacion - tipo string - poblacion usuario
	 * @param provincia - tipo string - provincia usuario
	 * @param fechaNacimiento - tipo string - fecha nacimiento usuario
	 */
	public Usuario(int codigoUsuario, String nombre, String apellidos, String dni, String domicilio, String poblacion,
			String provincia, String fechaNacimiento) {
		this.codigoUsuario = codigoUsuario;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.domicilio = domicilio;
		this.poblacion = poblacion;
		this.provincia = provincia;
		this.fechaNacimiento = fechaNacimiento;
	}

	@Override
	public String toString() {
		return "Cod: " + codigoUsuario + " - Nombre: " + nombre;
	}

	public int getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(int codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}