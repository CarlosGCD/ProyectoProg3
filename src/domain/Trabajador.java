package domain;

public class Trabajador {
	public int cod;
	public String nombre;
	public String apellidos;
	
	public Trabajador(int cod, String nombre, String apellidos) {
		super();
		this.cod = cod;
		this.nombre = nombre;
		this.apellidos = apellidos;
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
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	@Override
	public String toString() {
		return "Trabajador [cod=" + cod + ", nombre=" + nombre + ", apellidos=" + apellidos + "]";
	}
	
	
}
