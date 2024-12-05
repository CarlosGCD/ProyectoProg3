package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class VentanaMenu extends JFrame{

private static final long serialVersionUID = 1L;
	
	protected JFrame vActual, vAnterior;
	protected JPanel pNorte,pCentro, pOferta;
	protected JButton btnMotos, btnMotoSegundaMano, btnVolver;
	protected JLabel lbienvenida, lblOferta; 
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
		pNorte = new JPanel();
		pOferta = new JPanel(null);
		
		pCentro.setOpaque(false);
		pNorte.setOpaque(false);
		pOferta.setOpaque(true);
		
		pCentro.setLayout(new GridLayout(3, 1, 20, 20));
		pCentro.setBorder(new EmptyBorder(150, 100, 150, 100)); //Arriba, izq, abajo, drch
		
		pNorte.setBorder(new EmptyBorder(15, 0, 0, 0));
		
		pOferta.setBackground(new Color(255, 220, 220));
		pOferta.setPreferredSize(new Dimension(pFondo.getWidth(), 50));
		
		//Creamos los botones 
		btnMotos = new JButton("Visualizar Motos");
		btnMotos.setPreferredSize(new Dimension(250, 30));
		pCentro.add(btnMotos);
		
		btnMotoSegundaMano = new JButton("Visualizar Motos de Segunda Mano");
		btnMotoSegundaMano.setPreferredSize(new Dimension(250, 30));
		pCentro.add(btnMotoSegundaMano);
		
		btnVolver = new JButton("Volver");
		btnVolver.setPreferredSize(new Dimension(250, 30));
		pCentro.add(btnVolver);
        
		//Creamos el mensaje de bienvenida
		lbienvenida = new JLabel("¡Bienvenid@ a nuestro concesionario de motos!");
		Font fuente = new Font("Times New Roman", Font.BOLD, 24); 
		lbienvenida.setFont(fuente);
		pNorte.add(lbienvenida);
		
		//Creamos el JLabel para el hilo
		lblOferta = new JLabel("¡¡¡OFERTAS DE NAVIDAD!!! ¡¡¡TODO AL 30% NO TE LO PIERDAS!!!");
		lblOferta.setFont(new Font("Times New Roman", Font.BOLD, 30));
		
		lblOferta.setSize(lblOferta.getPreferredSize());
		
		pOferta.add(lblOferta);
		
		// Creamos paneles para cada botón
		JPanel pBoton1 = new JPanel();
		JPanel pBoton2 = new JPanel();
		JPanel pBoton3 = new JPanel();

		// Los paneles deben ser transparentes para mantener el fondo visible
		pBoton1.setOpaque(false);
		pBoton2.setOpaque(false);
		pBoton3.setOpaque(false);

		// Añadimos cada botón a su panel
		pBoton1.add(btnMotos);
		pBoton2.add(btnMotoSegundaMano);
		pBoton3.add(btnVolver);

		// Añadimos los paneles al panel central en lugar de los botones directamente
		pCentro.add(pBoton1);
		pCentro.add(pBoton2);
		pCentro.add(pBoton3);
		
		//Añadir los paneles al panel principal
		pFondo.setLayout(new BorderLayout());
        pFondo.add(pNorte, BorderLayout.NORTH);
        pFondo.add(pCentro, BorderLayout.CENTER);
        pFondo.add(pOferta, BorderLayout.SOUTH);
		
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
		
		//Creamos un Thread que desplaza todo el rato la etiqueta lblOferta
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				lblOferta.setBounds(-lblOferta.getPreferredSize().width, 10, lblOferta.getPreferredSize().width, lblOferta.getPreferredSize().height);
				
				while(true) {
					int x = lblOferta.getX();
					x = x + 40;
					lblOferta.setLocation(x, 10);
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(x >= pOferta.getWidth()) {
						x = -lblOferta.getWidth();
						lblOferta.setLocation(x, 10);
						
					}
					
					pOferta.repaint();
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
		
		add(pFondo);
		
		setVisible(true);
	}
	
}
