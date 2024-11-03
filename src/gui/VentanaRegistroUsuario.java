package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class VentanaRegistroUsuario extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JLabel titulo;
	private JLabel usuario;
	private JLabel contrasenia;
	private JTextField textoUsuario;
	private JPasswordField textoContrasenia;
	private JButton botonRegistrar;
	private JButton botonVolver;
	
	
	public VentanaRegistroUsuario() {
		super();
		
		JPanel panelTitulo = new JPanel();
		panelTitulo.setLayout(new GridLayout(1,1));
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(1,2));
		
		titulo = new JLabel("Registro de usuario");
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		panelTitulo.add(titulo);

		usuario = new JLabel("Usuario: ");
		usuario.setBounds(90, 60, 200, 30);
		panel1.add(usuario);
		
		textoUsuario = new JTextField();
		textoUsuario.setBounds(141, 67, 150, 20);
		panel1.add(textoUsuario);

		contrasenia = new JLabel("Contraseña: ");
		contrasenia.setBounds(69, 100, 200, 30);
		panel1.add(contrasenia);
		

		
		textoContrasenia = new JPasswordField();
		textoContrasenia.setBounds(141, 105, 150, 20); 
        panel1.add(textoContrasenia);

        
      
        botonRegistrar = new JButton("Registrarse");
        botonRegistrar.addActionListener((e) -> {
			if (textoUsuario.getText().isEmpty() || textoContrasenia.getPassword().length == 0) {
				JOptionPane.showMessageDialog(null, "Debes rellenar todos los campos");
				return;
			}else {
				JOptionPane.showMessageDialog(null, "Te has registrado correctamente");
	        	new VentanaInicioSesion();
				this.dispose();
			}
        
		});
        
        botonVolver = new JButton("Volver");
        
        botonVolver.addActionListener((e)-> {
        	new VentanaInicioSesion();
        	this.dispose();
        });
			

        panel2.add(botonRegistrar);
        panel2.add(botonVolver);
        
     
        
        add(panelTitulo, BorderLayout.NORTH);
		add(panel1, BorderLayout.CENTER);
		add(panel2, BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Registro de usuario");
		this.setBounds(0,0,400,500);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}
}
