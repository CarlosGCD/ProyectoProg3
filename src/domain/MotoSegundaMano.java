package domain;

public class MotoSegundaMano extends Moto {
    public int anioFabricacion;       
    public int kilometraje;           
    public String estado;
    
	public MotoSegundaMano() {
		super();
	}
	
	public MotoSegundaMano(String marca, String modelo, String color, String matricula, int cilindrada, int potencia, int precio, int puntos) {
		super(marca, modelo, color, matricula, cilindrada, potencia, precio, puntos);
	}

	public MotoSegundaMano(int anioFabricacion, int kilometraje, String estado) {
		super();
		this.anioFabricacion = anioFabricacion;
		this.kilometraje = kilometraje;
		this.estado = estado;
	}

	public int getAnioFabricacion() {
		return anioFabricacion;
	}

	public void setAnioFabricacion(int anioFabricacion) {
		this.anioFabricacion = anioFabricacion;
	}

	public int getKilometraje() {
		return kilometraje;
	}

	public void setKilometraje(int kilometraje) {
		this.kilometraje = kilometraje;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
	    return "MotoSegundaMano [" + super.toString() + 
	           ", anioFabricacion=" + anioFabricacion + 
	           ", kilometraje=" + kilometraje + 
	           ", estado=" + estado + "]";
	}

}
