package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class VentanaInfoMoto extends JFrame {

	private static final long serialVersionUID = 1L;
	private static VentanaInfoMoto ventanaActual;
	
	public VentanaInfoMoto(String marca, String modelo, String color, String matricula, String cilindrada, String potencia, String precio, String puntuacion) {
		
		if (ventanaActual != null) {
            ventanaActual.dispose();
        }
        ventanaActual = this;

        setLocation(100, 250);
        
		setTitle("Detalles de la Moto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 600);
        setLayout(new BorderLayout());
		
		JPanel panelTitulo = new JPanel(new GridLayout(2,1));
		
		JLabel labelModelo = new JLabel(marca + " " + modelo + " " + color);
		labelModelo.setFont(new Font("Times New Roman", Font.BOLD, 16));
		labelModelo.setHorizontalAlignment(JLabel.CENTER); 
		
		JPanel panelFoto = new JPanel();
		JLabel foto = new JLabel("FOTO DE LA MOTO");
		
		JPanel panelCaracteristaicas = new JPanel(new GridLayout(4,1));
		JLabel labelCilindrada = new JLabel("Cilindrada: " + cilindrada);
		JLabel labelPotencia = new JLabel("Potencia: " + potencia);
		JLabel labelColor = new JLabel("Color: " + color);
		JLabel labelPrecio = new JLabel("Precio: " + precio);
		JLabel labelPuntuacion = new JLabel("Puntuacion: " + puntuacion);
		
		JPanel panelDescripcion = new JPanel(new BorderLayout());
		JTextArea textoDescripcion = new JTextArea("Descripcion breve: " + marca + " " + modelo + "Lorem ipsum dolor sit amet consectetur adipiscing elit auctor habitant fusce cubilia eleifend neque justo quis, pretium sem enim gravida nec urna ornare nulla class cras potenti mollis montes bibendum.");
		textoDescripcion.setLineWrap(true);
		textoDescripcion.setEditable(false);
		
		JPanel panelCentral = new JPanel(new BorderLayout());
		
		
		panelDescripcion.add(textoDescripcion);
		
		panelCaracteristaicas.add(labelCilindrada);
		panelCaracteristaicas.add(labelPotencia);
		panelCaracteristaicas.add(labelColor);
		panelCaracteristaicas.add(labelPrecio);
		panelCaracteristaicas.add(labelPuntuacion);
		
		panelFoto.add(foto);
		
		panelTitulo.add(labelModelo);
		
		panelCentral.add(panelFoto, BorderLayout.NORTH);
		panelCentral.add(panelCaracteristaicas, BorderLayout.CENTER);
		panelCentral.add(panelDescripcion, BorderLayout.SOUTH);
		
		add(panelTitulo, BorderLayout.NORTH);
		add(panelCentral, BorderLayout.CENTER);
	
		
        
		
	}
}
