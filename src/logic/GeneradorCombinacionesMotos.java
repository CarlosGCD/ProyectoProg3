package logic;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import db.MetodosDB;
import domain.Moto;
import domain.MotoSegundaMano;

public class GeneradorCombinacionesMotos {

    // Método para generar combinaciones de motos dentro del presupuesto
	public static void generarCombinacionesMotos(boolean esMotoSegundaMano) {
	    String input = JOptionPane.showInputDialog("Introduce tu presupuesto:");
	    if (input != null && !input.isEmpty()) {
	        try {
	            int presupuesto = Integer.parseInt(input);

	            // Obtener la lista de motos desde la base de datos según el tipo de moto
	            MetodosDB.conectar();
	            List<Moto> motos = new ArrayList<>(); 
	            
	            if (esMotoSegundaMano) {
	                // Cargar solo motos de segunda mano
	                List<MotoSegundaMano> motosSegundaMano = MetodosDB.obtenerMotosSegundaMano(); 
	                motos.addAll(motosSegundaMano);  // Agregar todas las motos de segunda mano a la lista de motos
	            } else {
	                // Cargar solo motos nuevas
	                motos = MetodosDB.obtenerMotos(); 
	            }
	            MetodosDB.desconectar();

	            // Generar las combinaciones
	            List<List<Moto>> combinaciones = generarCombinaciones(motos, presupuesto);

	            // Verificar si hay combinaciones
	            if (combinaciones.isEmpty()) {
	                // Mostrar mensaje de error
	                JOptionPane.showMessageDialog(null, "No hay combinaciones posibles con ese presupuesto.", "Error", JOptionPane.ERROR_MESSAGE);
	            } else {
	                // Construir el mensaje de salida con las combinaciones
	                StringBuilder resultado = new StringBuilder("Combinaciones posibles:\n");
	                // Crear el mensaje con las combinaciones y mostrar propiedades específicas (Año) de las motos de segunda mano
	                for (List<Moto> combinacion : combinaciones) {
	                    resultado.append("[");
	                    for (Moto moto : combinacion) {
	                        resultado.append(moto.getMarca()).append(" ").append(moto.getModelo()).append(", ");

	                        // Si es moto de segunda mano, añadir el año
	                        if (moto instanceof MotoSegundaMano) {
	                            MotoSegundaMano motoSegundaMano = (MotoSegundaMano) moto;
	                            resultado.setLength(resultado.length() - 2); // Eliminar la última coma y espacio
	                            resultado.append(" ")
	                                    .append("Año: ").append(motoSegundaMano.getAnioFabricacion())
	                                    .append(", ");
	                        }
	                    }
	                    resultado.setLength(resultado.length() - 2); // Eliminar la última coma y espacio
	                    resultado.append("]\n");
	                }

	                // Crear un JTextArea para mostrar las combinaciones con desplazamiento
	                JTextArea textArea = new JTextArea(20, 40); // 20 filas y 40 columnas
	                textArea.setText(resultado.toString());
	                textArea.setCaretPosition(0); // Desplazar el área de texto al inicio
	                textArea.setEditable(false);

	                // Crear un JScrollPane para que el contenido sea desplazable
	                JScrollPane scrollPane = new JScrollPane(textArea);

	                // Mostrar el resultado en un cuadro de diálogo
	                JOptionPane.showMessageDialog(null, scrollPane, "Combinaciones posibles a partir del presupuesto introducido", JOptionPane.INFORMATION_MESSAGE);
	            }
	        } catch (NumberFormatException e) {
	            JOptionPane.showMessageDialog(null, "Por favor, introduce un número válido.");
	        }
	    }
	}

    // Método recursivo para generar combinaciones de motos dentro del presupuesto
    private static List<List<Moto>> generarCombinaciones(List<? extends Moto> motos, int presupuesto) {
        List<List<Moto>> resultado = new ArrayList<>();
        generarCombinacionesR(motos, presupuesto, 0, new ArrayList<>(), resultado);
        return resultado;
    }

    // Método recursivo para explorar las combinaciones
    private static void generarCombinacionesR(List<? extends Moto> motos, int presupuesto, int index, List<Moto> combinacionActual, List<List<Moto>> resultado) {
        if (presupuesto < 0) {
            return; // Si excedemos el presupuesto, detener esta rama
        }

        // Solo agregar combinaciones de al menos dos motos
        if (combinacionActual.size() >= 2) {
            resultado.add(new ArrayList<>(combinacionActual));
        }

        // Explorar todas las combinaciones posibles desde el índice actual
        for (int i = index; i < motos.size(); i++) {
            Moto moto = motos.get(i);

            // Añadir la moto actual a la combinación
            combinacionActual.add(moto);

            // Llamada recursiva con el presupuesto reducido y el siguiente índice
            generarCombinacionesR(motos, presupuesto - moto.getPrecio(), i + 1, combinacionActual, resultado);

            // Retirar la moto para probar otras combinaciones
            combinacionActual.remove(combinacionActual.size() - 1);
        }
    }
}
