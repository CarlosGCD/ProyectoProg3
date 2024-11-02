package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class VentanaMenu extends JFrame{

private static final long serialVersionUID = 1L;
	
	protected JFrame vActual, vAnterior;
	protected JPanel pNorte,pSur,pCentro;
	protected JButton btnMotos, btnMotoSegundaMano, btnVolver;
	protected JLabel lbienvenida; 
	
	
	public VentanaMenu(JFrame vAnterior) {
		super();
		
		vActual = this;
		this.vAnterior = vAnterior;
		
		setSize(800, 600);
		setLocationRelativeTo(null);
		setTitle("Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Creación de los paneles y los añadimos a la ventana
		pCentro = new JPanel();
		pSur = new JPanel();
		pNorte = new JPanel();
		
		pCentro.setLayout(new GridLayout(2, 1, 20, 20));
		pCentro.setBorder(new EmptyBorder(170, 250, 170, 250)); //Arriba, izq, abajo, drch
		
		pNorte.setBorder(new EmptyBorder(20, 0, 0, 0));
		
		getContentPane().add(pCentro, BorderLayout.CENTER);
		getContentPane().add(pSur, BorderLayout.SOUTH);
		getContentPane().add(pNorte, BorderLayout.NORTH);
		
		//Creamos los botones 
		btnMotos = new JButton("Visualizar Motos");
		pCentro.add(btnMotos);
		
		btnMotoSegundaMano = new JButton("Visualizar Motos de Segunda Mano");
		pCentro.add(btnMotoSegundaMano);
		btnVolver = new JButton("Volver");
		pSur.add(btnVolver);
        
		//Creamos el mensaje de bienvenida
		lbienvenida = new JLabel("¡Bienvenid@ a nuestro concesionario de motos!");
		Font fuente = new Font("Times New Roman", Font.BOLD, 20); 
		lbienvenida.setFont(fuente);
		pNorte.add(lbienvenida);
		
		//ActionListeners:
		btnMotos.addActionListener((e)-> {
			vActual.dispose();
			VentanaMotos ventanaMotos = new VentanaMotos(vActual);
			ventanaMotos.setVisible(true);
		});
		
		btnVolver.addActionListener((e)-> {
			vActual.dispose();
			vAnterior.setVisible(true);
		});
		
		
		setVisible(true);
	}
}
