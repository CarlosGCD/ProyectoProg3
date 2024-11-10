package main;

import javax.swing.SwingUtilities;

import gui.VentanaInfoMoto;
import gui.VentanaInicioSesion;

public class VentanaPrincipal {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new VentanaInicioSesion();
				
			}
		});
		
	}
}
