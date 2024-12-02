package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class MetodosDB {
	
	private static Connection conn = null;
	
	//Metodo para conectarse a la base de datos
	public static void conectar() {
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
	public static void registrarPersona(String usuario, String password, int trabajador) {
		sentSQL = "INSERT INTO Personas (cod,nombre, password, trabajador) VALUES (?, ?, ?, ?)";
		
		Integer codigo = generarCodigo();
		while(existeCodigo(codigo)) {
            codigo = generarCodigo();
        }
		
		try {
			ps = conn.prepareStatement(sentSQL);
			ps.setInt(1, codigo);
			ps.setString(2, usuario);
			ps.setString(3, password);
			ps.setInt(4, trabajador); 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	//comprueba si existe un usuario
	public static boolean existeUsuario(String usuario) {
		sentSQL = String.format("SELECT nombre FROM Personas WHERE nombre = '%s' ", usuario);
		
		try {
			ps = conn.prepareStatement(sentSQL);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("nombre").equals(usuario);
			}
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	//compruebar que la contraseña es correcta
	public static boolean comprobarPassword(String usuario, String password) {
        sentSQL = String.format("SELECT password FROM Personas WHERE nombre = '%s' ", usuario);
        
        try {
            ps = conn.prepareStatement(sentSQL);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) { // Verifica si hay resultados
                return rs.getString("password").equals(password);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        	e.printStackTrace();
        }
        return false;
    }

	//comprueba si un usuario es trabajador
	public static boolean esTrabajador(String usuario) {
		sentSQL = String.format("SELECT trabajador FROM Personas WHERE nombre = '%s' ", usuario);

		try {
			ps = conn.prepareStatement(sentSQL);
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) { // Verifica si hay resultados
	            return rs.getInt("trabajador") == 1;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	//genera un codigo aleatorio
	public static int generarCodigo() {
		Random random = new Random();
		int numero = random.nextInt(Integer.MAX_VALUE);
		return numero;
	}
	
	//comprueba si existe un codigo
	public static boolean existeCodigo(int codigo) {
		
		sentSQL = String.format("SELECT cod FROM Personas WHERE cod = '%d' ", codigo);
		
		try {
			ps = conn.prepareStatement(sentSQL);
	        ResultSet rs = ps.executeQuery();
	        
			if (rs.next()) { // Verifica si hay resultados
				return rs.getInt("cod") == codigo;
			}
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
		}
}






















