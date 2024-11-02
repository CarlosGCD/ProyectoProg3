package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class VentanaMotos extends JFrame{

	private static final long serialVersionUID = 1L;
	
	protected JFrame vActual, vAnterior;
	protected JPanel pNorte,pSur,pCentro;
	protected JButton btnVolver, btnComprar;
	
	protected JTable tablaMotos;
	protected DefaultTableModel modeloTablaMotos;
	protected JScrollPane scrollTablaMotos;
	
	protected JTextField txtFiltro;
	protected JComboBox<String> cbTipo;
	
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
		pNorte = new JPanel();
		
		getContentPane().add(pCentro, BorderLayout.CENTER);
		getContentPane().add(pSur, BorderLayout.SOUTH);
		getContentPane().add(pNorte, BorderLayout.NORTH);
		
		//Creamos los botones del panel sur
		btnComprar = new JButton("Comprar");
		pSur.add(btnComprar);
		btnVolver = new JButton("Volver");
		pSur.add(btnVolver);
		
		//Creación de la tabla
		modeloTablaMotos = new DefaultTableModel();
		tablaMotos = new JTable(modeloTablaMotos);
		scrollTablaMotos = new JScrollPane(tablaMotos);
		
		//Añadimos los títulos de las columnas de la tabla
		String [] titulos = {"MARCA", "MODELO", "COLOR", "MATRÍCULA", "CILINDRADA", "POTENCIA", "PRECIO", "PUNTOS"};
		modeloTablaMotos.setColumnIdentifiers(titulos);
		
		pCentro.add(scrollTablaMotos);
		
		//Creamos los botones del panel norte
		cbTipo = new JComboBox<>(titulos);
		pNorte.add(cbTipo);
		txtFiltro = new JTextField(20);
		pNorte.add(txtFiltro);
		
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
		
		//Añadimos el listener al JTextField
		txtFiltro.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				String texto = txtFiltro.getText();
				String tipo = cbTipo.getSelectedItem().toString();
				
				if (tipo!=null) { //Si ha seleccionado un tipo en el JComboBox
					if (tipo.equals("MARCA")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (marca.toUpperCase().contains(texto.toUpperCase())) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if(tipo.equals("MODELO")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (modelo.toUpperCase().contains(texto.toUpperCase())) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if(tipo.equals("COLOR")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (color.toUpperCase().contains(texto.toUpperCase())) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if(tipo.equals("MATRÍCULA")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (matricula.toUpperCase().contains(texto.toUpperCase())) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if(tipo.equals("CILINDRADA")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (cilindrada.contains(texto)) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if(tipo.equals("POTENCIA")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (potencia.contains(texto)) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if(tipo.equals("PRECIO")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (precio.contains(texto)) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					}else {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							//Convertir puntos a cadena para la comparacion
							String puntosStr = String.valueOf(puntos);
							
							if (puntosStr.contains(texto)) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					}
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String texto = txtFiltro.getText();
				String tipo = cbTipo.getSelectedItem().toString();
				
				if (tipo!=null) { //Si ha seleccionado un tipo en el JComboBox
					if (tipo.equals("MARCA")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (marca.toUpperCase().contains(texto.toUpperCase())) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if(tipo.equals("MODELO")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (modelo.toUpperCase().contains(texto.toUpperCase())) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if(tipo.equals("COLOR")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (color.toUpperCase().contains(texto.toUpperCase())) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if(tipo.equals("MATRÍCULA")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (matricula.toUpperCase().contains(texto.toUpperCase())) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if(tipo.equals("CILINDRADA")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (cilindrada.contains(texto)) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if(tipo.equals("POTENCIA")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (potencia.contains(texto)) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if(tipo.equals("PRECIO")) {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							if (precio.contains(texto)) {
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					}else {
						//Obtenemos el modelo que contiene toda la información
						DefaultTableModel modeloCompleto = modeloTablaMotos;
						DefaultTableModel modeloFiltrado = new DefaultTableModel();
						modeloFiltrado.setColumnIdentifiers(titulos);
						for (int i = 0; i < modeloCompleto.getRowCount(); i++) {
							String marca = modeloCompleto.getValueAt(i, 0).toString();
							String modelo = modeloCompleto.getValueAt(i, 1).toString();
							String color = modeloCompleto.getValueAt(i, 2).toString();
							String matricula = modeloCompleto.getValueAt(i, 3).toString();
							String cilindrada = modeloCompleto.getValueAt(i, 4).toString();
							String potencia = modeloCompleto.getValueAt(i, 5).toString();
							String precio = modeloCompleto.getValueAt(i, 6).toString();
							int puntos = Integer.parseInt(modeloCompleto.getValueAt(i, 7).toString());
							
							//Convertir puntos a cadena para la comparacion
							String puntosStr = String.valueOf(puntos);
							
							if (texto.isEmpty() || puntosStr.contains(texto)){
								Object [] fila = {marca, modelo, color, matricula, cilindrada, potencia, precio, puntos};
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					}
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				
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
