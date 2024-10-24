package main;

import javax.swing.SwingUtilities;
import gui.VentanaInicioSesion;

public class VentanaPrincipal {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			//hola
			@Override
			public void run() {
				new VentanaInicioSesion();
				
			}
		});
		
	}
}
