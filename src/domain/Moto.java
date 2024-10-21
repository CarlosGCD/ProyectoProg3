package domain;

public class Moto {
	public String marca;
	public String modelo;
	public String color;
	public String matricula;
	public int cilindrada;
	public int potencia;
	public int precio;
	public int puntos;
	
	public Moto() {
		super();
	}
	public Moto(String marca, String modelo, String color, String matricula, int cilindrada, int potencia, int precio,
			int puntos) {
		super();
		this.marca = marca;
		this.modelo = modelo;
		this.color = color;
		this.matricula = matricula;
		this.cilindrada = cilindrada;
		this.potencia = potencia;
		this.precio = precio;
		this.puntos = puntos;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public int getCilindrada() {
		return cilindrada;
	}
	public void setCilindrada(int cilindrada) {
		this.cilindrada = cilindrada;
	}
	public int getPotencia() {
		return potencia;
	}
	public void setPotencia(int potencia) {
		this.potencia = potencia;
	}
	public int getPrecio() {
		return precio;
	}
	public void setPrecio(int precio) {
		this.precio = precio;
	}
	public int getPuntos() {
		return puntos;
	}
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}
	@Override
	public String toString() {
		return "Moto [marca=" + marca + ", modelo=" + modelo + ", color=" + color + ", matricula=" + matricula
				+ ", cilindrada=" + cilindrada + ", potencia=" + potencia + ", precio=" + precio + ", puntos=" + puntos
				+ "]";
	}
	
	

	
}
