package gui;

import java.awt.BorderLayout;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
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
	private BufferedImage fondo;
	
	public VentanaMenu(JFrame vAnterior) {
		super();
		
		vActual = this;
		this.vAnterior = vAnterior;
		
		setSize(800, 600);
		setLocationRelativeTo(null);
		setTitle("Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Cargar la imagen para el fondo
		try {
            fondo = ImageIO.read(new File("resources/images/concesionarioFondo.png")); 
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		//Crear un JPanel personalizado
		JPanel pFondo = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (fondo != null) {
                    g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                	System.out.println("Error: La imagen de fondo no se ha cargado.");
                }
            }
        };
        
		//Creación de los paneles y los añadimos a la ventana
		pCentro = new JPanel();
		pSur = new JPanel();
		pNorte = new JPanel();
		
		pCentro.setOpaque(false);
		pSur.setOpaque(false);
		pNorte.setOpaque(false);
		
		pCentro.setLayout(new GridLayout(3, 1, 20, 20));
		pCentro.setBorder(new EmptyBorder(150, 100, 100, 100)); //Arriba, izq, abajo, drch
		
		pNorte.setBorder(new EmptyBorder(15, 0, 0, 0));
		
		//Creamos los botones 
		btnMotos = new JButton("Visualizar Motos");
		pCentro.add(btnMotos);
		
		btnMotoSegundaMano = new JButton("Visualizar Motos de Segunda Mano");
		pCentro.add(btnMotoSegundaMano);
		
		btnVolver = new JButton("Volver");
		pCentro.add(btnVolver);
        
		//Creamos el mensaje de bienvenida
		lbienvenida = new JLabel("¡Bienvenid@ a nuestro concesionario de motos!");
		Font fuente = new Font("Times New Roman", Font.BOLD, 24); 
		lbienvenida.setFont(fuente);
		pNorte.add(lbienvenida);
		
		//Añadir los paneles al panel principal
        pFondo.add(pNorte, BorderLayout.NORTH);
        pFondo.add(pCentro, BorderLayout.CENTER);
        pFondo.add(pSur, BorderLayout.SOUTH);
		
		//ActionListeners:
		btnMotos.addActionListener((e)-> {
			vActual.dispose();
			VentanaMotos ventanaMotos = new VentanaMotos(vActual);
			ventanaMotos.setVisible(true);
		});
		
		btnMotoSegundaMano.addActionListener((e)-> {
			vActual.dispose();
			VentanaMotosSegundaMano ventanaMotosSegundaMano = new VentanaMotosSegundaMano(vActual);
			ventanaMotosSegundaMano.setVisible(true);
		});
		
		btnVolver.addActionListener((e)-> {
			vActual.dispose();
			new VentanaInicioSesion();
		});
		add(pFondo);
		
		setVisible(true);
	}
	
}
