package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import db.MetodosDB;
import db.MetodosDB.UltimaCompra;
import db.MetodosDB.UltimoAlquiler;
import domain.AlquilerDuracion;
import domain.Moto;

public class VentanaMotos extends JFrame {

	private static final long serialVersionUID = 1L;

	protected JFrame vActual, vAnterior;
	protected JPanel pNorte, pSur, pCentro;
	protected JButton btnVolver, btnComprar, btnAlquilar;

	protected JTable tablaMotos;
	protected DefaultTableModel modeloTablaMotos;
	protected JScrollPane scrollTablaMotos;

	protected JTextField txtFiltro;
	protected JComboBox<String> cbTipo;

	protected int fila, columna; // Los atributos para saber la fila y columna del raton para el cambio de fondo
									// de las celdas cuando este esté sobre ellas

	public VentanaMotos(JFrame vAnterior) {
		super();

		vActual = this;
		this.vAnterior = vAnterior;

		setSize(1200, 600);
		setLocation(500, 250);
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
		btnAlquilar = new JButton("Alquilar");
		pSur.add(btnAlquilar);
		btnComprar = new JButton("Comprar");
		pSur.add(btnComprar);
		btnVolver = new JButton("Volver");
		pSur.add(btnVolver);

		// Creación de la tabla
		modeloTablaMotos = new DefaultTableModel();
		tablaMotos = new JTable(modeloTablaMotos) {

			private static final long serialVersionUID = 1L;

			// Bloquea la edición de todas las celdas
			@Override
			public boolean isCellEditable(int row, int column) {

				return false;
			}
		};

		scrollTablaMotos = new JScrollPane(tablaMotos);
		scrollTablaMotos.setBorder(new TitledBorder("Motos"));
		tablaMotos.setFillsViewportHeight(true);

		// Añadimos los títulos de las columnas de la tabla
		String[] titulos = { "MARCA", "MODELO", "COLOR", "MATRÍCULA", "CILINDRADA", "POTENCIA", "PRECIO", "PUNTOS"};
		modeloTablaMotos.setColumnIdentifiers(titulos);

		pCentro.add(scrollTablaMotos);

		// Creamos los botones del panel norte
		cbTipo = new JComboBox<>(titulos);
		pNorte.add(cbTipo);
		txtFiltro = new JTextField(20);
		pNorte.add(txtFiltro);

		// Cargar la información (motos) en la tabla
		cargarTabla();

		// Para impedir que se puedan cambiar las columnas de orden
		tablaMotos.getTableHeader().setReorderingAllowed(false);

		// Para impedir que se puedan redimensionar laas columnas
		tablaMotos.getTableHeader().setResizingAllowed(false);

		// Se definen criterios de ordenación por defecto para cada columna
		tablaMotos.setAutoCreateRowSorter(true);

		// Configurar el RowSorter para personalizar el orden de cada columna
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTablaMotos);

		// Configurar comparadores personalizados para las columnas de texto y numéricas
		for (int i = 0; i < modeloTablaMotos.getColumnCount(); i++) {
			if (i < 4 || i == 10) {
				// Comparador para columnas de texto (orden alfabético ascendente)
				sorter.setComparator(i, Comparator.comparing(String::valueOf, String.CASE_INSENSITIVE_ORDER));
			} else if (i >= 4 && i <= 9) {
				// Comparador para columnas numéricas con unidades (orden descendente)
				sorter.setComparator(i, (o1, o2) -> {
					try {
						// Extraer la parte numérica del texto eliminando cualquier carácter no numérico
						// (excepto comas y puntos)
						String numStr1 = o1.toString().replaceAll("[^\\d,]", "").replace(",", "");
						String numStr2 = o2.toString().replaceAll("[^\\d,]", "").replace(",", "");

						// Convertir los valores extraídos a Integer
						Integer num1 = numStr1.isEmpty() ? 0 : Integer.parseInt(numStr1);
						Integer num2 = numStr2.isEmpty() ? 0 : Integer.parseInt(numStr2);

						// Orden descendente
						return num2.compareTo(num1);
					} catch (NumberFormatException e) {
						// Si ocurre un error al parsear, considera los valores como iguales
						return 0;
					}
				});
			}
		}
		// Asignar el RowSorter personalizado a la tabla
		tablaMotos.setRowSorter(sorter);

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
				if (!cleanValue.isEmpty() && (originalValue.matches("\\d{1,3}(\\d{3})*(\\.\\d+)?€") || // Formato
																										// numérico con
																										// €
						originalValue.matches("\\d+(\\.\\d+)?\\s?(cc|CV)"))) { // Formato numérico con cc o CV

					lblContenido.setHorizontalAlignment(JLabel.CENTER);
				} else {
					lblContenido.setText(value.toString());
				}

			}

			if (row == fila && column == columna) {
				lblContenido.setBackground(new Color(220, 240, 255)); // Cambiamos el fondo a un azul muy claro
				lblContenido.setForeground(tablaMotos.getSelectionForeground());
				lblContenido.setFont(lblContenido.getFont().deriveFont(Font.BOLD | Font.ITALIC,
						lblContenido.getFont().getSize2D() + 1));
				// Cambiamos el formato del texto para resaltar más la celda sobre la que esta
				// el ratón pero sin sobrecargar la tabla, e incrementamos el tamaño del texto
				// en 1
			}

			lblContenido.setOpaque(true);

			return lblContenido;

		});

		// El color de fondo de la celda cambiará a NO SE QUE COLOR cuando el puntero
		// del ráton pase sobre ella
		tablaMotos.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// Cada vez que se mueva el ratón queremos saber en qué fila y columna de la
				// tabla está el puntero
				// Obtenemos las coordenadas en las que está el puntero del ratón
				Point p = e.getPoint();
				// Obtenemos la fila de la tabla situada en el punto p
				fila = tablaMotos.rowAtPoint(p);
				// Obtenemos la columna de la tabla situada en el punto p
				columna = tablaMotos.columnAtPoint(p);
				// Forzamos a que se vuelva a aplicar el Renderer
				tablaMotos.repaint();
			}

		});

		// Implementamos el mouseListener para saber cuando sale el raton de la tabla,
		// para devolverle el fondo anterior a la ultima celda sobre la que ha estado el
		// raton, sino se queda pintada
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

							if (marca.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							if (modelo.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							if (color.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							if (matricula.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							if (cilindrada.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							if (potencia.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							if (precio.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							// Convertir puntos a cadena para la comparacion
							String puntosStr = String.valueOf(puntos);

							if (puntosStr.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							if (marca.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							if (modelo.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							if (color.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							if (matricula.toUpperCase().startsWith(texto.toUpperCase())) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							if (cilindrada.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							if (potencia.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							if (precio.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
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

							// Convertir puntos a cadena para la comparacion
							String puntosStr = String.valueOf(puntos);

							if (texto.isEmpty() || puntosStr.startsWith(texto)) {
								Object[] fila = { marca, modelo, color, matricula, cilindrada, potencia, precio,
										puntos };
								modeloFiltrado.addRow(fila);
							}
						}
						tablaMotos.setModel(modeloFiltrado);
						tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
					}
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}

		});
		
		
		tablaMotos.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tablaMotos.rowAtPoint(e.getPoint());		
				
				if (row >= 0) {
                    
                    String marca = (String) modeloTablaMotos.getValueAt(row, 0);
                    String modelo = (String) modeloTablaMotos.getValueAt(row, 1);
                    String color = (String) modeloTablaMotos.getValueAt(row, 2);
                    String matricula = (String) modeloTablaMotos.getValueAt(row, 3);
                    String cilindrada = (String) modeloTablaMotos.getValueAt(row, 4);
                    String potencia = (String) modeloTablaMotos.getValueAt(row, 5);
                    String precio = (String) modeloTablaMotos.getValueAt(row, 6);
                    String puntuacion = String.valueOf(modeloTablaMotos.getValueAt(row, 7));
                    
                    SwingUtilities.invokeLater(() ->  {
                    	VentanaInfoMoto ventana = new VentanaInfoMoto(marca, modelo, color, matricula, cilindrada, potencia, precio, puntuacion);
                    	ventana.setVisible(true);
                    });
				}
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}
		});

		btnVolver.addActionListener((e) -> {
			vActual.dispose();
			vAnterior.setVisible(true);
		});
		
		btnComprar.addActionListener((e) -> {
		    int selectedRow = tablaMotos.getSelectedRow();
		    
		    if (selectedRow >= 0) {
		        
		        String modeloMoto = (String) modeloTablaMotos.getValueAt(selectedRow, 1);

		        
		        int usuarioId = MetodosDB.obtenerUsuarioActual(); 

		        
		        boolean exito = MetodosDB.insertarCompra(usuarioId, modeloMoto);

		        if (exito) {
		            
		            String compraRealizada = MetodosDB.obtenerCompraRealizada(usuarioId, modeloMoto);
		            System.out.println(compraRealizada);

		            JOptionPane.showMessageDialog(this, "Compra realizada exitosamente.");
		        } else {
		            JOptionPane.showMessageDialog(this, "Error al realizar la compra.", "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    } else {
		        JOptionPane.showMessageDialog(this, "Debe seleccionar una moto.", "Aviso", JOptionPane.WARNING_MESSAGE);
		    }
		});
		
		btnAlquilar.addActionListener((e) -> {
		    int selectedRow = tablaMotos.getSelectedRow();

		    if (selectedRow >= 0) {
		        
		        String modeloMoto = (String) modeloTablaMotos.getValueAt(selectedRow, 1); 
        
		        int usuarioId = MetodosDB.obtenerUsuarioActual();
	     
		        AlquilerDuracion[] opciones = AlquilerDuracion.values();
		        AlquilerDuracion seleccion = (AlquilerDuracion) JOptionPane.showInputDialog(
		                this,
		                "Seleccione duración de alquiler:",
		                "Opciones de Alquiler",
		                JOptionPane.QUESTION_MESSAGE,
		                null,
		                opciones,
		                opciones[0]
		        );

		        if (seleccion != null) {

		            boolean exito = MetodosDB.insertarAlquiler(usuarioId, modeloMoto, seleccion);

		            if (exito) {

		                String alquilerRealizado = MetodosDB.obtenerAlquilerRealizado(usuarioId, modeloMoto);
		                System.out.println(alquilerRealizado);

		                JOptionPane.showMessageDialog(this, "Alquiler registrado exitosamente.");
		            } else {
		                JOptionPane.showMessageDialog(this, "Error al registrar el alquiler.", "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        }
		    } else {
		        JOptionPane.showMessageDialog(this, "Debe seleccionar una moto.", "Aviso", JOptionPane.WARNING_MESSAGE);
		    }
		});
		
		tablaMotos.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {	
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
					// Obtener el ID del usuario actual
					int idUsuario = MetodosDB.obtenerUsuarioActual();
					
					// Verificar si hay compras
					if (!MetodosDB.tieneCompras(idUsuario)) {
						JOptionPane.showMessageDialog(null, 
								"No has realizado ninguna compra anteriormente.", 
								"Sin compras", 
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						UltimaCompra ultimaCompra = MetodosDB.obtenerUltimaCompra(idUsuario);
						
						if (ultimaCompra != null) {
							int opcion = JOptionPane.showConfirmDialog(null,
									"Estás seguro de que deseas cancelar la siguiente compra?\n\n" +
									"Modelo: " + ultimaCompra.getModelo() + "\n" +
									"Fecha: " + ultimaCompra.getFecha() + "\n" +
									"ID Compra: " + ultimaCompra.getId(),
									"Confirmar cancelación",
									JOptionPane.YES_NO_OPTION);
						
							if (opcion == JOptionPane.YES_OPTION) {
								// Eliminamos la compra de la base de datos
								if (MetodosDB.eliminarCompra(ultimaCompra.getId())) {
									JOptionPane.showMessageDialog(null, 
											"La compra ha sido cancelada exitosamente.", 
											"Compra cancelada", 
											JOptionPane.INFORMATION_MESSAGE);
								} else {
									JOptionPane.showMessageDialog(null, 
											"Hubo un error al cancelar la compra.", 
											"Error",
											JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
				} else if(e.getKeyCode() == KeyEvent.VK_Y && e.isControlDown()) {
					// Obtener el ID del usuario actual
					int idUsuario = MetodosDB.obtenerUsuarioActual();
					
					// Verificar si hay alquileres
					if (!MetodosDB.tieneAlquileres(idUsuario)) {
						JOptionPane.showMessageDialog(null, 
								"No has realizado ningún alquiler anteriormente.", 
								"Sin alquileres", 
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						UltimoAlquiler ultimoAlquiler = MetodosDB.obtenerUltimoAlquiler(idUsuario);
						
						if (ultimoAlquiler != null) {
							int opcion = JOptionPane.showConfirmDialog(null,
									"Estás seguro de que deseas cancelar el siguiente alquiler?\n\n" +
									"Modelo: " + ultimoAlquiler.getModelo() + "\n" +
									"Fecha: " + ultimoAlquiler.getFecha() + "\n" +
									"Duración: " + ultimoAlquiler.getDuracion() + "meses" + "\n" +
									"ID Alquiler: " + ultimoAlquiler.getId(),
									"Confirmar cancelación",
									JOptionPane.YES_NO_OPTION);
						
							if (opcion == JOptionPane.YES_OPTION) {
								// Eliminamos el alquiler de la base de datos
								if (MetodosDB.eliminarAlquiler(ultimoAlquiler.getId())) {
									JOptionPane.showMessageDialog(null, 
											"El alquiler ha sido cancelado exitosamente.", 
											"Alquiler cancelado", 
											JOptionPane.INFORMATION_MESSAGE);
								} else {
									JOptionPane.showMessageDialog(null, 
											"Hubo un error al cancelar el alquiler.", 
											"Error",
											JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
				}
			}
		});


		setVisible(true);
	}
	


	private void cargarTabla() {
		// Limpiar la tabla antes de cargar nuevos datos
	    modeloTablaMotos.setRowCount(0);
	    MetodosDB.conectar();
	    List<Moto> motos = MetodosDB.obtenerMotos(); 
	    
	    for (Moto moto : motos) {
	        Object[] fila = { 
	            moto.getMarca(),
	            moto.getModelo(),
	            moto.getColor(),
	            moto.getMatricula(),
	            String.valueOf(moto.getCilindrada()) + " cc",  
	            String.valueOf(moto.getPotencia()) + " CV",    
	            String.valueOf(moto.getPrecio()) + "€",       
	            moto.getPuntos()
	        };
	        modeloTablaMotos.addRow(fila);
	    }
	    MetodosDB.desconectar();
	    tablaMotos.getColumnModel().getColumn(0).setCellRenderer(new RendererIcono());
	}
	
	

}
