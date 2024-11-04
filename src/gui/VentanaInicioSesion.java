package gui;

import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class VentanaInicioSesion extends JFrame{

	private static final long serialVersionUID = 1L;
	
	protected JTextField usuario;
	protected JPasswordField contrasena;
	protected JLabel textoUsuario;
	protected JLabel textoContrasena;
	protected JLabel titulo;
	protected JButton iniciarSesion;
	protected JButton registrarseUsuario;
	protected JButton registrarseTrabajador;
	
	protected JFrame vActual;
	
	public VentanaInicioSesion() {
		super();
		
		vActual = this;
		
		JPanel panelTitulo = new JPanel();
		panelTitulo.setLayout(new GridLayout(1, 1));
		
		ImageIcon tituloImagen = new ImageIcon("resources/images/logo moto.png");
		//imagen generada por una IA
		ImageIcon minImagen = new ImageIcon(tituloImagen.getImage().getScaledInstance(400, 250, Image.SCALE_SMOOTH));
		JLabel labelTitulo = new JLabel(minImagen);
		panelTitulo.add(labelTitulo, BorderLayout.NORTH);
		
		
		JPanel panelSesion = new JPanel();
		panelSesion.setLayout(null);
		
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new GridLayout(1, 3));

		textoUsuario = new JLabel("Usuario: ");
		textoUsuario.setBounds(180, 60, 200, 30);
		panelSesion.add(textoUsuario);

		textoContrasena = new JLabel("Contraseña: ");
		textoContrasena.setBounds(160, 100, 200, 30);
		panelSesion.add(textoContrasena);

		usuario = new JTextField();
		usuario.setBounds(235, 65, 150, 20);
		panelSesion.add(usuario);

		contrasena = new JPasswordField();
		contrasena.setBounds(235, 105, 150, 20);
		panelSesion.add(contrasena);
		
	
		iniciarSesion = new JButton("Iniciar sesion");
		registrarseUsuario = new JButton("Registrar usuario");
		registrarseTrabajador = new JButton("Registrar trabajador");
		panelBotones.add(iniciarSesion);
		panelBotones.add(registrarseUsuario);
		panelBotones.add(registrarseTrabajador);
		
		iniciarSesion.addActionListener((e)-> {
			if (usuario.getText().equals("") || contrasena.getPassword().equals("")) {
				JOptionPane.showMessageDialog(null, "Has iniciado sesión correctamente");
				vaciarCampos();
				vActual.dispose();
				new VentanaMenu(vActual);
			} else {
				JOptionPane.showMessageDialog(null, "Nombre de usuario y/o contraseña incorrectos", "ERROR", JOptionPane.ERROR_MESSAGE);
				vaciarCampos();
			}
		});
		
		registrarseUsuario.addActionListener((e) -> {
			vActual.dispose();
			new VentanaRegistroUsuario();
		});
		
		registrarseTrabajador.addActionListener((e) -> {
			vActual.dispose();
			new VentanaRegistroTrabajador();
		});
		
		
		this.add(panelTitulo, BorderLayout.NORTH);
		this.add(panelSesion, BorderLayout.CENTER);
		this.add(panelBotones, BorderLayout.SOUTH);
		
		this.setTitle("Inicio de sesion");
		this.setSize(600, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void vaciarCampos() {
		usuario.setText("");
		contrasena.setText("");
	}
	
}
