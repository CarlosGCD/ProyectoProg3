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

import domain.AlquilerDuracion;
import domain.Moto;
import domain.MotoSegundaMano;

public class MetodosDB {
	
	private static Connection conn = null;
	
	private static String usuarioLogueado = null;
	
	//Metodo para conectarse a la base de datos
	public static void conectar() {
			try {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection("jdbc:sqlite:resources/db/Personas.db");
			} catch (ClassNotFoundException e) {
				System.out.println("Error cargando el driver de la BD");
			} catch (SQLException e) {
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
    	String sql = "CREATE TABLE IF NOT EXISTS Motos(marca String, modelo String, color String, matricula String, cilindrada int, potencia int, precio int, puntos int)";
    	try {
    		Statement stmt = conn.createStatement();
    		stmt.executeUpdate(sql);
    		sql = "CREATE TABLE IF NOT EXISTS MotosSegundaMano(marca String, modelo String, color String, matricula String, cilindrada int, potencia int, precio int, puntos int, anioFabricacion int, kilometraje int, estado String)";
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
    			ps.setInt(7, Integer.parseInt(datos[6].replaceAll("[,€]", "")));
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
    		String sql = "INSERT INTO MotosSegundaMano VALUES (?,?,?,?,?,?,?,?,?,?,?)";
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
    			ps.setInt(7, Integer.parseInt(datos[6].replaceAll("[,€]", "")));
    			ps.setInt(8, Integer.parseInt(datos[7]));
    			ps.setInt(9, Integer.parseInt(datos[8]));
    			ps.setInt(10, Integer.parseInt(datos[9].replaceAll("[, km]", "")));
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
	
	// Método para guardar el usuario que ha iniciado sesión
		public static void setUsuarioLogueado(String nombre) {
			usuarioLogueado = nombre;
		}
		
		// Método para obtener el usuario actual
		public static String getUsuarioLogueado() {
			return usuarioLogueado;
		}
		
		public static int obtenerUsuarioActual() {
		    
		    String sql = "SELECT cod FROM Personas WHERE nombre = ?";
		    int usuarioId = -1;

		    if (conn == null) {
		        conectar();
		    }
		    
		    if (usuarioLogueado == null) {
		    	return usuarioId; // Retornamos -1 si no hay usuario logueado
		    }

		    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
		        pstmt.setString(1, usuarioLogueado);

		        try (ResultSet rs = pstmt.executeQuery()) {
		            if (rs.next()) {
		                usuarioId = rs.getInt("cod");
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        desconectar();
		    }

		    return usuarioId;
		}
		
	public static boolean tieneCompras(int idUsuario) {
		String sql = "SELECT COUNT(*) as count FROM Comprar WHERE persona_id = ?";
		
		if (conn == null) {
			conectar();
		}
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, idUsuario);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("count") > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			desconectar();
		}
		return false;
	}
	
	public static class UltimaCompra {
	    private int id;
	    private String modelo;
	    private String fecha;
	    
	    public UltimaCompra(int id, String modelo, String fecha) {
	        this.id = id;
	        this.modelo = modelo;
	        this.fecha = fecha;
	    }
	    
	    public int getId() { 
	    	return id; 
	    }
	    public String getModelo() { 
	    	return modelo; 
	    }
	    public String getFecha() { 
	    	return fecha; 
	    }
	}
	
	public static UltimaCompra obtenerUltimaCompra(int idUsuario) {
	    String sql = "SELECT rowid, moto_modelo, fecha FROM Comprar " +
	                 "WHERE persona_id = ? ORDER BY rowid DESC LIMIT 1";
	    
	    if (conn == null) {
	        conectar();
	    }

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, idUsuario);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return new UltimaCompra(
	                    rs.getInt("id"),
	                    rs.getString("moto_modelo"),
	                    rs.getString("fecha")
	                );
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        desconectar();
	    }
	    return null;
	}
	
	public static boolean eliminarCompra(int idCompra) {
	    String sql = "DELETE FROM Comprar WHERE id = ?";
	    
	    if (conn == null) {
	        conectar();
	    }

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, idCompra);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        desconectar();
	    }
	}
	
	public static boolean insertarCompra(int usuarioId, String modeloMoto) {
	    String sql = "INSERT INTO Comprar (persona_id, moto_modelo, fecha) VALUES (?, ?, date('now'));";
	    
	    if (conn == null) {
	        conectar();
	    }

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setInt(1, usuarioId);
	        pstmt.setString(2, modeloMoto);

	        pstmt.executeUpdate();
	        return true;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        desconectar();
	    }
	}
	
	public static String obtenerCompraRealizada(int usuarioId, String modeloMoto) {
	    String sql = "SELECT persona_id, moto_modelo, fecha FROM Comprar WHERE persona_id = ? AND moto_modelo = ? ORDER BY fecha DESC LIMIT 1;";
	    StringBuilder resultado = new StringBuilder();

	    if (conn == null) {
	        conectar();
	    }

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, usuarioId);
	        pstmt.setString(2, modeloMoto);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                int personaId = rs.getInt("persona_id");
	                String motoModelo = rs.getString("moto_modelo");
	                String fecha = rs.getString("fecha");

	                resultado.append("Compra realizada: ");
	                resultado.append("Usuario ID: ").append(personaId).append(", ");
	                resultado.append("Modelo Moto: ").append(motoModelo).append(", ");
	                resultado.append("Fecha: ").append(fecha);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        desconectar();
	    }

	    return resultado.toString();
	}
	
	public static boolean tieneAlquileres(int idUsuario) {
	    String sql = "SELECT COUNT(*) as count FROM Alquilar WHERE persona_id = ?";
	    
	    if (conn == null) {
	        conectar();
	    }

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, idUsuario);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("count") > 0;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        desconectar();
	    }
	    return false;
	}
	
	public static class UltimoAlquiler {
	    private int id;
	    private String modelo;
	    private String fecha;
	    private int duracion;
	    
	    public UltimoAlquiler(int id, String modelo, String fecha, int duracion) {
	        this.id = id;
	        this.modelo = modelo;
	        this.fecha = fecha;
	        this.duracion = duracion;
	    }
	    
	    public int getId() { 
	    	return id; 
	    }
	    public String getModelo() { 
	    	return modelo;
	    }
	    public String getFecha() { 
	    	return fecha; 
	    }
	    public int getDuracion() { 
	    	return duracion; 
	    }
	}
	
	public static UltimoAlquiler obtenerUltimoAlquiler(int idUsuario) {
	    String sql = "SELECT rowid, moto_modelo, fecha, duracion FROM Alquilar " +
	                 "WHERE persona_id = ? ORDER BY rowid DESC LIMIT 1";
	    
	    if (conn == null) {
	        conectar();
	    }

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, idUsuario);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return new UltimoAlquiler(
	                    rs.getInt("id"),
	                    rs.getString("moto_modelo"),
	                    rs.getString("fecha"),
	                    rs.getInt("duracion")
	                );
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        desconectar();
	    }
	    return null;
	}
	
	public static boolean eliminarAlquiler(int idAlquiler) {
	    String sql = "DELETE FROM Alquilar WHERE id = ?";
	    
	    if (conn == null) {
	        conectar();
	    }

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, idAlquiler);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        desconectar();
	    }
	}
	
	public static boolean insertarAlquiler(int usuarioId, String modeloMoto, AlquilerDuracion duracion) {
	    String sql = "INSERT INTO Alquilar (persona_id, moto_modelo, fecha, duracion) VALUES (?, ?, date('now'), ?);";
	    
	    if (conn == null) {
	        conectar();
	    }

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, usuarioId);
	        pstmt.setString(2, modeloMoto);
	        pstmt.setInt(3, duracion.getMeses());

	        pstmt.executeUpdate();
	        return true;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        desconectar();
	    }
	}
	
	public static String obtenerAlquilerRealizado(int usuarioId, String modeloMoto) {
	    String sql = "SELECT persona_id, moto_modelo, fecha, duracion FROM Alquilar WHERE persona_id = ? AND moto_modelo = ? ORDER BY fecha DESC LIMIT 1;";
	    StringBuilder resultado = new StringBuilder();

	    if (conn == null) {
	        conectar();
	    }

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, usuarioId);
	        pstmt.setString(2, modeloMoto);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                int personaId = rs.getInt("persona_id");
	                String motoModelo = rs.getString("moto_modelo");
	                String fecha = rs.getString("fecha");
	                int duracion = rs.getInt("duracion");

	                resultado.append("Alquiler realizado: ");
	                resultado.append("Usuario ID: ").append(personaId).append(", ");
	                resultado.append("Modelo Moto: ").append(motoModelo).append(", ");
	                resultado.append("Fecha: ").append(fecha).append(", ");
	                resultado.append("Duración: ").append(duracion).append(" mes/meses");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        desconectar();
	    }

	    return resultado.toString();
	}


}






















