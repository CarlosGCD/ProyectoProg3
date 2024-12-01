package gui;

import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import db.MetodosDB;

public class VentanaInicioSesion extends JFrame{

	private static final long serialVersionUID = 1L;
	
	protected JTextField usuario;
	protected JPasswordField contrasena;
	protected JLabel textoUsuario;
	protected JLabel textoContrasena;
	protected JLabel titulo;
	protected JLabel textoTrabajador;
	protected JButton iniciarSesion;
	protected JButton registrarseUsuario;
	protected JButton registrarseTrabajador;
	protected JRadioButton siTrabajador;
	protected JRadioButton noTrabajador;
	
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
		textoUsuario.setBounds(180, 0, 200, 30);
		panelSesion.add(textoUsuario);

		textoContrasena = new JLabel("Contraseña: ");
		textoContrasena.setBounds(160, 40, 200, 30);
		panelSesion.add(textoContrasena);

		textoTrabajador = new JLabel("¿Deseas entrar como trabajador? ");
		textoTrabajador.setBounds(180, 80, 200, 30);
		panelSesion.add(textoTrabajador);
		
		usuario = new JTextField();
		usuario.setBounds(235, 5, 150, 20);
		panelSesion.add(usuario);

		contrasena = new JPasswordField();
		contrasena.setBounds(235, 45, 150, 20);
		panelSesion.add(contrasena);
		
		siTrabajador = new JRadioButton("si");
		siTrabajador.setBounds(200, 110, 20, 20);
		panelSesion.add(siTrabajador);
		
		noTrabajador = new JRadioButton("no");
		noTrabajador.setBounds(200, 130, 20, 20);
		panelSesion.add(noTrabajador);
		
		if (siTrabajador.isSelected()) {
			noTrabajador.setSelected(false);
		}
		if (noTrabajador.isSelected()) {
            siTrabajador.setSelected(false);
        };
         
		
		
		
	
		iniciarSesion = new JButton("Iniciar sesion");
		registrarseUsuario = new JButton("Registrar usuario");
		registrarseTrabajador = new JButton("Registrar trabajador");
		panelBotones.add(iniciarSesion);
		panelBotones.add(registrarseUsuario);
		panelBotones.add(registrarseTrabajador);
		
		
		
		iniciarSesion.addActionListener((e)-> {
			MetodosDB.conectar();
			if (siTrabajador.isSelected()) {
				if(usuario.getText().isEmpty() || contrasena.getPassword().length == 0) {
					JOptionPane.showMessageDialog(null, "Debes rellenar todos los campos");
                    return;
				}else if (MetodosDB.existeUsuario(usuario.getText())) {
					if (MetodosDB.comprobarPassword(usuario.getText(), contrasena.getPassword().toString())) {
						if (MetodosDB.esTrabajador(usuario.getText())) {
							JOptionPane.showMessageDialog(null, "Has iniciado sesión correctamente");
							vaciarCampos();
							vActual.dispose();
							new VentanaMenu(vActual);
						} else {
							JOptionPane.showMessageDialog(null, "No eres trabajador", "ERROR",
									JOptionPane.ERROR_MESSAGE);
							vaciarCampos();
						}
					} else {
						JOptionPane.showMessageDialog(null, "Contraseña incorrecta", "ERROR",
								JOptionPane.ERROR_MESSAGE);
						vaciarCampos();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Nombre de usuario incorrecto", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					vaciarCampos();
				}
			} else {
				if(usuario.getText().isEmpty() || contrasena.getPassword().length == 0) {
					JOptionPane.showMessageDialog(null, "Debes rellenar todos los campos");
					return;
					
				}
				else if (MetodosDB.existeUsuario(usuario.getText())) {
					if (MetodosDB.comprobarPassword(usuario.getText(), contrasena.getPassword().toString())) {
						JOptionPane.showMessageDialog(null, "Has iniciado sesión correctamente");
						vaciarCampos();
						vActual.dispose();
						new VentanaMenu(vActual);
					} else {
						JOptionPane.showMessageDialog(null, "Contraseña incorrecta", "ERROR",
								JOptionPane.ERROR_MESSAGE);
						vaciarCampos();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Nombre de usuario incorrecto", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					vaciarCampos();
				}
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
