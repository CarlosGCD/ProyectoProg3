package gui;

import java.awt.Component;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RendererIcono extends DefaultTableCellRenderer{
	
	private static final long serialVersionUID = 1L;
	
	private Map<String, ImageIcon> iconos;
	
	public RendererIcono() {
		iconos = new HashMap<>();
		
		iconos.put("Honda", tamanyoIcono(new ImageIcon("resources/images/Honda.png"), 20, 20));
        iconos.put("Yamaha", tamanyoIcono(new ImageIcon("resources/images/Yamaha.png"), 20, 20));
        iconos.put("BMW", tamanyoIcono(new ImageIcon("resources/images/BMW.png"), 20, 20));
        iconos.put("Aprilia", tamanyoIcono(new ImageIcon("resources/images/Aprilia.png"), 20, 20));
        iconos.put("Ducati", tamanyoIcono(new ImageIcon("resources/images/Ducati.png"), 20, 20));
        iconos.put("Harley", tamanyoIcono(new ImageIcon("resources/images/Harley.png"), 20, 20));
        iconos.put("Kawasaki", tamanyoIcono(new ImageIcon("resources/images/Kawasaki.png"), 20, 20));
        iconos.put("KTM", tamanyoIcono(new ImageIcon("resources/images/KTM.png"), 20, 20));
        iconos.put("Suzuki", tamanyoIcono(new ImageIcon("resources/images/Suzuki.png"), 20, 20));
        iconos.put("Triumph", tamanyoIcono(new ImageIcon("resources/images/Triumph.png"), 20, 20));
	}
	
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        ImageIcon icon = iconos.get(value.toString());
        label.setText("");
        label.setIcon(icon);
        label.setToolTipText(value.toString());
        label.setHorizontalAlignment(JLabel.CENTER);
        
        return label;
    }
	
	private ImageIcon tamanyoIcono(ImageIcon icono, int ancho, int alto) {
		Image imagen = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
		return new ImageIcon(imagen);
	}
}
// me he ayudado de chatgpt para hacer el mapa y JLabel que me daban problemas.