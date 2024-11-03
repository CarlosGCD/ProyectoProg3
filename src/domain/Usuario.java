package domain;

public class Usuario {
	public int cod;
	public String nombre;
	public String contraseña;
	
	public Usuario(int cod, String nombre, String contraseña) {
		super();
		this.cod = cod;
		this.nombre = nombre;
		this.contraseña = contraseña;
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

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	@Override
	public String toString() {
		return "Usuario [cod=" + cod + ", nombre=" + nombre + ", contraseña=" + contraseña + "]";
	}
	
	
	
}
