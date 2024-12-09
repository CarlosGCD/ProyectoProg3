package db;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import domain.Moto;
import domain.MotoSegundaMano;

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
	
	// Nuevo método para cerrar la conexión
    public static void desconectar() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión con la base de datos");
        }
    }
	
	
	//METODOS DE LA BASE DE DATOS

    //Método para crear la tabla de motos y la de motosSegundaMano
    public static void crearTablas() {
    	String sql = "CREATE TABLE IF NOT EXISTS Motos(marca String, modelo String, color String, matricula String, cilindrada String, potencia String, precio String, puntos int)";
    	try {
    		Statement stmt = conn.createStatement();
    		stmt.executeUpdate(sql);
    		sql = "CREATE TABLE IF NOT EXISTS MotosSegundaMano(marca String, modelo String, color String, matricula String, cilindrada String, potencia String, precio String, puntos int, anioFabricacion int, kilometraje int, estado String)";
    		stmt.executeUpdate(sql);
    		stmt.close();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
    //Métodos para cargar las motos a la base de datos desde los ficheros
    public static void cargarMotos(String nomfich) {
    	try {
    		String sql = "INSERT INTO Motos VALUES (?,?,?,?,?,?,?,?)";
    		PreparedStatement ps = conn.prepareStatement(sql);
    		Scanner sc = new Scanner(new File(nomfich)); 
    		while(sc.hasNextLine()) {
    			String linea = sc.nextLine();
    			String [] datos = linea.split(";");
    			
    			ps.setString(1, datos[0]);
    			ps.setString(2, datos[1]);
    			ps.setString(3, datos[2]);
    			ps.setString(4, datos[3]);
    			ps.setString(5, datos[4]);
    			ps.setString(6, datos[5]);
    			ps.setString(7, datos[6]);
    			ps.setInt(8, Integer.parseInt(datos[7]));
    			ps.execute();
    		}
    		sc.close();
    		ps.close();
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    public static void cargarMotosSegundaMano(String nomfich) {
    	try {
    		String sql = "INSERT INTO Motos VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    		PreparedStatement ps = conn.prepareStatement(sql);
    		Scanner sc = new Scanner(new File(nomfich)); 
    		while(sc.hasNextLine()) {
    			String linea = sc.nextLine();
    			String [] datos = linea.split(";");
    			
    			ps.setString(1, datos[0]);
    			ps.setString(2, datos[1]);
    			ps.setString(3, datos[2]);
    			ps.setString(4, datos[3]);
    			ps.setString(5, datos[4]);
    			ps.setString(6, datos[5]);
    			ps.setString(7, datos[6]);
    			ps.setInt(8, Integer.parseInt(datos[7]));
    			ps.setInt(9, Integer.parseInt(datos[8]));
    			ps.setInt(10, Integer.parseInt(datos[9]));
    			ps.setString(11, datos[10]);
    			ps.execute();
    		}
    		sc.close();
    		ps.close();
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    public static List<Moto> obtenerMotos() {
    	List<Moto> lm = new ArrayList<>();
    	String sql = "SELECT * FROM Motos";
    	try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String marca = rs.getString("marca");
				String modelo = rs.getString("modelo");
				String color = rs.getString("color");
				String matricula = rs.getString("matricula");
				int cilindrada = rs.getInt("cilindrada");
				int potencia = rs.getInt("potencia");
				int precio = rs.getInt("precio");
				int puntos = rs.getInt("puntos");
				Moto m = new Moto(marca, modelo, color, matricula, cilindrada, potencia, precio, puntos);
				lm.add(m);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return lm;
    }
    
    public static List<MotoSegundaMano> obtenerMotosSegundaMano() {
    	List<MotoSegundaMano> lmsm = new ArrayList<>();
    	String sql = "SELECT * FROM MotosSegundaMano";
    	try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String marca = rs.getString("marca");
				String modelo = rs.getString("modelo");
				String color = rs.getString("color");
				String matricula = rs.getString("matricula");
				int cilindrada = rs.getInt("cilindrada");
				int potencia = rs.getInt("potencia");
				int precio = rs.getInt("precio");
				int puntos = rs.getInt("puntos");
				int anioFabricacion = rs.getInt("anioFabricacion");
				int kilometraje = rs.getInt("kilometraje");
				String estado = rs.getString("estado");
				MotoSegundaMano msm = new MotoSegundaMano(marca, modelo, color, matricula, cilindrada, potencia, precio, puntos, anioFabricacion, kilometraje, estado);
				lmsm.add(msm);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return lmsm;
    }

	//registrar un usuario
	public static void registrarPersona(String usuario, String password, int trabajador) {
		String sentSQL;
		PreparedStatement ps;
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
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//borrar un usuario
	public static void borrarPersona(String usuario) {
		String sentSQL;
		PreparedStatement ps;
		sentSQL = String.format("DELETE FROM Personas WHERE nombre = '%s' ", usuario);

		try {
			ps = conn.prepareStatement(sentSQL);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//comprueba si existe un usuario
	public static boolean existeUsuario(String usuario) {
		String sentSQL;
		PreparedStatement ps;
		sentSQL = String.format("SELECT nombre FROM Personas WHERE nombre = '%s' ", usuario);
		
		try {
			ps = conn.prepareStatement(sentSQL);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("nombre").equals(usuario);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	//compruebar que la contraseña es correcta
	public static boolean comprobarPassword(String usuario, String password) {
		String sentSQL;
		PreparedStatement ps;
        sentSQL = String.format("SELECT password FROM Personas WHERE nombre = '%s' ", usuario);
        
        try {
            ps = conn.prepareStatement(sentSQL);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) { // Verifica si hay resultados
                return rs.getString("password").equals(password);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        	e.printStackTrace();
        }
        return false;
    }

	//comprueba si un usuario es trabajador
	public static boolean esTrabajador(String usuario) {
		String sentSQL;
		PreparedStatement ps;
		sentSQL = String.format("SELECT trabajador FROM Personas WHERE nombre = '%s' ", usuario);

		try {
			ps = conn.prepareStatement(sentSQL);
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) { // Verifica si hay resultados
	            return rs.getInt("trabajador") == 1;
	        }
	        rs.close();
	        ps.close();
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
		String sentSQL;
		PreparedStatement ps;
		sentSQL = String.format("SELECT cod FROM Personas WHERE cod = '%d' ", codigo);
		
		try {
			ps = conn.prepareStatement(sentSQL);
	        ResultSet rs = ps.executeQuery();
	        
			if (rs.next()) { // Verifica si hay resultados
				return rs.getInt("cod") == codigo;
			}
			rs.close();
			ps.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
		}
}






















