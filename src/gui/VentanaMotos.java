package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class VentanaMotos extends JFrame{

	private static final long serialVersionUID = 1L;
	
	protected JFrame vActual, vAnterior;
	protected JPanel pNorte,pSur,pCentro,pEste,pOeste;
	protected JButton btnVolver;
	
	protected JTable tablaMotos;
	protected DefaultTableModel modeloTablaMotos;
	protected JScrollPane scrollTablaMotos;
	
	public VentanaMotos(JFrame vAnterior) {
		super();
		
		vActual = this;
		this.vAnterior = vAnterior;
		
		setSize(800, 600);
		setLocationRelativeTo(null);
		setTitle("Inventario de Motos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Creación de los paneles y los añadimos a la ventana
		pCentro = new JPanel(new GridLayout(1,1));
		pSur = new JPanel();
		
		getContentPane().add(pCentro, BorderLayout.CENTER);
		getContentPane().add(pSur, BorderLayout.SOUTH);
		
		//Creamos el boton y lo añadimos al panel sur
		btnVolver = new JButton("VOLVER");
		pSur.add(btnVolver);
		
		//Creación de la tabla
		modeloTablaMotos = new DefaultTableModel();
		tablaMotos = new JTable(modeloTablaMotos);
		scrollTablaMotos = new JScrollPane(tablaMotos);
		
		//Añadimos los títulos de las columnas de la tabla
		String [] titulos = {"MARCA", "MODELO", "COLOR", "MATRÍCULA", "CILINDRADA", "POTENCIA", "PRECIO", "PUNTOS"};
		modeloTablaMotos.setColumnIdentifiers(titulos);
		
		pCentro.add(scrollTablaMotos);
		
		//Cargar la información (motos) en la tabla
		cargarTabla();
		
		//Para impedir que se puedan cambiar las columnas de orden
		tablaMotos.getTableHeader().setReorderingAllowed(false);
		
		//Para hacer las celdas mas altas y se visualice mejor la informacion
		tablaMotos.setRowHeight(25);
		
		tablaMotos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
				//Damos un fondo mas grisaceo a las filas pares para diferenciar mejor la informacion de cada fila
				if (row%2 == 0) {
					c.setBackground(Color.LIGHT_GRAY);
				} else {
					c.setBackground(Color.WHITE);
				}
				return c;
				
			}
			
			
			
		});
		
		btnVolver.addActionListener((e)-> {
			vActual.dispose();
			vAnterior.setVisible(true);
		});
		
		setVisible(true);
	}
	
	private void cargarTabla() {
		File f = new File("resources/data/motos.txt");
		try {
			Scanner sc = new Scanner(f);
			while(sc.hasNextLine()) {
				String linea = sc.nextLine();
				String [] datos = linea.split(";");
				
				String marca = datos[0];
				String modelo = datos[1];
				String color = datos[2];
				String matricula = datos[3];
				String cilindrada = datos[4];
				String potencia = datos[5];
				String precio = datos[6];
				int puntos = Integer.parseInt(datos[7]);
				
				Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
				modeloTablaMotos.addRow(fila);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
