package gui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class VentanaMotosSegundaMano extends JFrame {

	private static final long serialVersionUID = 1L;
	protected JFrame vActual, vAnterior;
	protected JPanel pNorte, pSur, pCentro;
	protected JButton btnVolver, btnComprar;

	protected JTable tablaMotos;
	protected DefaultTableModel modeloTablaMotos;
	protected JScrollPane scrollTablaMotos;

	protected JTextField txtFiltro;
	protected JComboBox<String> cbTipo;
	
	protected int fila, columna;

	public VentanaMotosSegundaMano(JFrame vAnterior) {
		super();

		vActual = this;
		this.vAnterior = vAnterior;

		setSize(1600, 600);
		setLocationRelativeTo(null);
		setTitle("Inventario de Motos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Creación de los paneles y los añadimos a la ventana
		pCentro = new JPanel(new GridLayout(1, 1));
		pSur = new JPanel();
		pNorte = new JPanel();

		getContentPane().add(pCentro, BorderLayout.CENTER);
		getContentPane().add(pSur, BorderLayout.SOUTH);
		getContentPane().add(pNorte, BorderLayout.NORTH);

		// Creamos los botones del panel sur
		btnComprar = new JButton("Comprar");
		pSur.add(btnComprar);
		btnVolver = new JButton("Volver");
		pSur.add(btnVolver);

		// Creación de la tabla
		modeloTablaMotos = new DefaultTableModel();
		tablaMotos = new JTable(modeloTablaMotos);
		scrollTablaMotos = new JScrollPane(tablaMotos);

		// Añadimos los títulos de las columnas de la tabla
		String[] titulos = { "MARCA", "MODELO", "COLOR", "MATRÍCULA", "CILINDRADA", "POTENCIA", "PRECIO", "PUNTOS",
				"AÑO FABRICACIÓN", "KILOMETRAJE", "ESTADO" };
		modeloTablaMotos.setColumnIdentifiers(titulos);

		pCentro.add(scrollTablaMotos);

		// Creamos los botones del panel norte
		cbTipo = new JComboBox<>(titulos);
		pNorte.add(cbTipo);
		txtFiltro = new JTextField(20);
		pNorte.add(txtFiltro);

		// Cargar la información (motosSegundaMano) en la tabla
		cargarTabla();

		// Para impedir que se puedan cambiar las columnas de orden
		tablaMotos.getTableHeader().setReorderingAllowed(false);

		// Para impedir que se puedan redimensionar laas columnas
		tablaMotos.getTableHeader().setResizingAllowed(false);

		// Se definen criterios de ordenación por defecto para cada columna
		tablaMotos.setAutoCreateRowSorter(true);

		// Se modifica el modelo de selección de la tabla para que se pueda selecciona
		// únicamente una fila
		tablaMotos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Para hacer las celdas mas altas y se visualice mejor la informacion
		tablaMotos.setRowHeight(25);

		// Para hacer la cabecera mas alta
		JTableHeader header = tablaMotos.getTableHeader();

		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 28));

		// Habilitar las líneas de las celdas de la tabla
		tablaMotos.setShowGrid(true);
		tablaMotos.setGridColor(Color.DARK_GRAY);

		// Se establece el renderer para la cabecera
		tablaMotos.getTableHeader().setDefaultRenderer((table, value, isSelected, hasFocus, row, column) -> {
			JLabel lblTitulos = new JLabel(value.toString());
			lblTitulos.setHorizontalAlignment(JLabel.CENTER);

			lblTitulos.setFont(new Font("SansSerif", Font.BOLD, 13));

			switch (value.toString()) {
			case "MARCA":
			case "MODELO":
			case "COLOR":
			case "MATRÍCULA":
				lblTitulos.setHorizontalAlignment(JLabel.LEFT);
			}

			lblTitulos.setBackground(new Color(173, 216, 230)); // Azul claro
			lblTitulos.setForeground(Color.BLACK);

			lblTitulos.setOpaque(true);

			return lblTitulos;
		});
		;

		// Se establece el renderer para el contenido
		tablaMotos.setDefaultRenderer(Object.class, (table, value, isSelected, hasFocus, row, column) -> {
			JLabel lblContenido = new JLabel(value.toString());

			// Damos un fondo mas grisaceo a las filas pares para diferenciar mejor la
			// informacion de cada fila
			if (row % 2 == 0) {
				lblContenido.setBackground(Color.LIGHT_GRAY);
			} else {
				lblContenido.setBackground(Color.WHITE);
			}

			// Si la celda está seleccionada se renderiza con un amarillo suave
			if (isSelected) {
				lblContenido.setBackground(new Color(255, 255, 204));
				lblContenido.setForeground(tablaMotos.getSelectionForeground());
			}

			// Si el valor es numérico se renderiza centrado
			if (value instanceof Number) {
				lblContenido.setHorizontalAlignment(JLabel.CENTER);
			} else {
				// Si el valor es texto pero representa un número se renderiza centrado tambien
				String originalValue = value.toString();
				String cleanValue = originalValue.replaceAll("[^0-9.]", ""); // Mantener solo dígitos y el punto decimal

				// Comprobar si el valor limpio no está vacío y si el original tiene el formato
				// correcto
				if (!cleanValue.isEmpty() && 
					(originalValue.matches("\\d{1,3}(,\\d{3})*(\\.\\d+)?€") || // Formatonumérico con €
					 originalValue.matches("\\d{1,3}(,\\d{3})*\\s?km") || // Formato numérico con km
					 originalValue.matches("\\d+(\\.\\d+)?\\s?(cc|CV)"))) { // Formato numérico con cc o CV

					lblContenido.setHorizontalAlignment(JLabel.CENTER);
				} else {
					lblContenido.setText(value.toString());
				}

			}

			if (row==fila && column==columna) {
				lblContenido.setBackground(new Color(220, 240, 255)); //Cambiamos el fondo a un azul muy claro
				lblContenido.setForeground(tablaMotos.getSelectionForeground());
				lblContenido.setFont(lblContenido.getFont().deriveFont(Font.BOLD | Font.ITALIC, lblContenido.getFont().getSize2D() + 1)); 
				//Cambiamos el formato del texto para resaltar más la celda sobre la que esta el ratón pero sin sobrecargar la tabla, e incrementamos el tamaño del texto en 1
			}
			lblContenido.setOpaque(true);

			return lblContenido;

		});

		//El color de fondo de la celda cambiará a NO SE QUE COLOR cuando el puntero del ráton pase sobre ella
		tablaMotos.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				//Cada vez que se mueva el ratón queremos saber en qué fila y columna de la tabla está el puntero
				//Obtenemos las coordenadas en las que está el puntero del ratón
				Point p = e.getPoint();
				//Obtenemos la fila de la tabla situada en el punto p
				fila = tablaMotos.rowAtPoint(p);
				//Obtenemos la columna de la tabla situada en el punto p
				columna = tablaMotos.columnAtPoint(p);
				//Forzamos a que se vuelva a aplicar el Renderer
				tablaMotos.repaint();
			}
			
		});
		
		//Implementamos el mouseListener para saber cuando sale el raton de la tabla, para devolverle el fondo anterior a la ultima celda sobre la que ha estado el raton, sino se queda pintada
		tablaMotos.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
				fila = -1;
				columna = -1;
				tablaMotos.repaint();
			}
			
		});
		// Añadimos el listener al JTextField
		txtFiltro.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				String texto = txtFiltro.getText();
				String tipo = cbTipo.getSelectedItem().toString();

				if (tipo != null) { // Si ha seleccionado un tipo en el JComboBox
					if (tipo.equals("MARCA")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (marca.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("MODELO")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (modelo.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("COLOR")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (color.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("MATRÍCULA")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (matricula.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("CILINDRADA")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (cilindrada.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("POTENCIA")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (potencia.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("PRECIO")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (precio.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("PUNTOS")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							// Convertir puntos a cadena para la comparacion
							String puntosStr = String.valueOf(puntos);

							if (puntosStr.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("AÑO FABRICACIÓN")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							// Convertir fechaFabricacion a cadena para la comparacion
							String fechaFabricacionStr = String.valueOf(fechaFabricacion);

							if (fechaFabricacionStr.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("KILOMETRAJE")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (kilometraje.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (estado.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
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

				if (tipo != null) { // Si ha seleccionado un tipo en el JComboBox
					if (tipo.equals("MARCA")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (marca.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("MODELO")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (modelo.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("COLOR")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (color.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("MATRÍCULA")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (matricula.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("CILINDRADA")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (cilindrada.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("POTENCIA")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (potencia.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("PRECIO")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (precio.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("PUNTOS")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							// Convertir puntos a cadena para la comparacion
							String puntosStr = String.valueOf(puntos);

							if (texto.isEmpty() || puntosStr.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("AÑO FABRICACIÓN")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							// Convertir puntos a cadena para la comparacion
							String fechaFabricacionStr = String.valueOf(fechaFabricacion);

							if (texto.isEmpty() || fechaFabricacionStr.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else if (tipo.equals("KILOMETRAJE")) {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (kilometraje.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
					} else {
						// Obtenemos el modelo que contiene toda la información
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
							int fechaFabricacion = Integer.parseInt(modeloCompleto.getValueAt(i, 8).toString());
							String kilometraje = modeloCompleto.getValueAt(i, 9).toString();
							String estado = modeloCompleto.getValueAt(i, 10).toString();

							if (estado.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
										fechaFabricacion, kilometraje, estado };
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

		btnVolver.addActionListener((e) -> {
			vActual.dispose();
			vAnterior.setVisible(true);
		});

		setVisible(true);
	}

	private void cargarTabla() {
		File f = new File("resources/data/motoSegundaMano.txt");
		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNextLine()) {
				String linea = sc.nextLine();
				String[] datos = linea.split(";");

				String marca = datos[0];
				String modelo = datos[1];
				String color = datos[2];
				String matricula = datos[3];
				String cilindrada = datos[4];
				String potencia = datos[5];
				String precio = datos[6];
				int puntos = Integer.parseInt(datos[7]);
				int anioFabricacion = Integer.parseInt(datos[8]);
				String kilometraje = datos[9];
				String estado = datos[10];

				Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio, puntos,
						anioFabricacion, kilometraje, estado };
				modeloTablaMotos.addRow(fila);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
