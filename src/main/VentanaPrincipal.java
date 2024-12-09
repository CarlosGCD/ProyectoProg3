package main;

import javax.swing.SwingUtilities;

import db.MetodosDB;
import gui.VentanaInicioSesion;

public class VentanaPrincipal {
	public static void main(String[] args) {
		
		MetodosDB.conectar();
		MetodosDB.crearTablas();
		MetodosDB.cargarMotos("resources/data/motos.txt");
		MetodosDB.cargarMotosSegundaMano("resources/data/motoSegundaMano.txt");
		MetodosDB.desconectar();
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new VentanaInicioSesion();
				
			}
		});
		
	}
}
