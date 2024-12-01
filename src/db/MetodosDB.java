package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetodosDB {
	
	private static Connection conn = null;
	
	//Metodo para conectarse a la base de datos
	public void conectar() {
			try {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection("jdbc:sqlite:resources/db/Personas.db");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Error cargando el driver de la BD");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error conectando con la base de datos");
			}
			
	}
	
	
	
	//METODOS DE LA BASE DE DATOS
	
	public static String sentSQL;
	public static PreparedStatement ps;
	
	
	//registrar un usuario
	public static void registrarUsuario(String usuario, String password) {
		sentSQL = "INSERT INTO Usuario (cod,nombre, password, trabajador) VALUES (?, ?, ?, ?)";
		
		Integer codigo = generarCodigo();
		while(existeCodigo(codigo)) {
            codigo = generarCodigo();
        }
		
		try {
			ps = conn.prepareStatement(sentSQL);
			ps.setInt(1, codigo);
			ps.setString(2, usuario);
			ps.setString(3, password);
			ps.setBoolean(4, false);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//registrar un trabajador
	public static void registrarTrabajador(String usuario, String password) {
		sentSQL = "INSERT INTO Usuario (cod,nombre, password, trabajador) VALUES (?, ?, ?, ?)";

		Integer codigo = generarCodigo();
		while (existeCodigo(codigo)) {
			codigo = generarCodigo();
		}

		try {
			ps = conn.prepareStatement(sentSQL);
			ps.setInt(1, codigo);
			ps.setString(2, usuario);
			ps.setString(3, password);
			ps.setBoolean(4, true);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//comprueba si existe un usuario
	public static boolean existeUsuario(String usuario) {
		sentSQL = String.format("SELECT nombre FROM Usuario WHERE nombre = '%s' ", usuario);

		try {
			ps = conn.prepareStatement(sentSQL);
			ps.executeQuery(sentSQL);
			ps.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	//compruebar que la contraseña es correcta
	public static boolean comprobarPassword(String usuario, String password) {
        sentSQL = String.format("SELECT password FROM Usuario WHERE nombre = '%s' ", usuario);
        
        try {
            ps = conn.prepareStatement(sentSQL);
            ResultSet rs = ps.executeQuery(sentSQL);
            if (rs.getString("password").equals(password)) {
                return true;
            }
            ps.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            return false;
        }
        return false;
    }

	
	//genera un codigo aleatorio
	public static int generarCodigo() {
		double numero = Math.random() * 9000;
		return (int) numero;
	}
	
	//comprueba si existe un codigo
	public static boolean existeCodigo(int codigo) {
		
		sentSQL = String.format("SELECT cod FROM Usuario WHERE cod = '%d' ", codigo);
		
		try {
			ps = conn.prepareStatement(sentSQL);
			ps.executeQuery(sentSQL);
			ps.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

}




















