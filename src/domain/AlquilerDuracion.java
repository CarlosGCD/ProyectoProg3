package domain;

public enum AlquilerDuracion {
	unMes(1),
    tresMeses(3);

    private final int meses;

    AlquilerDuracion(int meses) {
        this.meses = meses;
    }

    public int getMeses() {
        return meses;
    }
	
}
