package gui;

import javax.swing.JFrame;

public class VentanaMotos extends JFrame{

	private static final long serialVersionUID = 1L;
	
	protected JFrame vActual, vAnterior;
	
	public VentanaMotos(JFrame vAnterior) {
		super();
		
		vActual = this;
		this.vAnterior = vAnterior;
		
		
		
		
		
		
		
		
		
		
		this.setTitle("Inventario de Motos");
		this.setSize(400, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
}
