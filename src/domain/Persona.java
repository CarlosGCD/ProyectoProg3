package domain;

public class Persona {
	public int cod;
	public String nombre;
	public String password;
	public boolean trabajador;
	public Persona(int cod, String nombre, String apellidos, boolean trabajador) {
		super();
		this.cod = cod;
		this.nombre = nombre;
		this.password = apellidos;
		this.trabajador = trabajador;
	}
	public int getCod() {
		return cod;
	}
	public void setCod(int cod) {
		this.cod = cod;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return password;
	}
	public void setApellidos(String apellidos) {
		this.password = apellidos;
	}
	public boolean isTrabajador() {
		return trabajador;
	}
	public void setTrabajador(boolean trabajador) {
		this.trabajador = trabajador;
	}
	@Override
	public String toString() {
		return "Persona [cod=" + cod + ", nombre=" + nombre + ", apellidos=" + password + ", trabajador=" + trabajador
				+ "]";
	}
	
	
}
