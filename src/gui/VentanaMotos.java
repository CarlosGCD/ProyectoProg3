package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
		
		setBounds(300, 200, 600, 400);
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
