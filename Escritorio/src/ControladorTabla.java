
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrador
 */
public class ControladorTabla {

    private VistaServidor vista;
    private ArrayList<String> rutas = new ArrayList<>();
    //private DataOutputStream fichero;
    //private DataInputStream ficheroLecutra;

    public ControladorTabla(final VistaServidor vista) {
        this.vista = vista;
        //fichero = new DataOutputStream(new FileOutputStream(registro,true));
        //ficheroLecutra = new DataInputStream(new FileInputStream(registro));

    }

    public void insertaFila(String nombreArchivo, String dispositivo, String ruta) {
        try {
            //fichero = new DataOutputStream(new FileOutputStream(registro,true));
            String fechaActual = fechaActual();
            Object[] insertar = new Object[4];
            insertar[0] = nombreArchivo;
            insertar[1] = dispositivo;
            insertar[2] = fechaActual;
            rutas.add(0, ruta);
            vista.getjTable1().getColumn("").setCellRenderer(new ButtonRenderer());
            JButton boton = new JButton();
            boton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    try {
                        //Codigo abrir carpeta
                        //ProcessBuilder p = new ProcessBuilder("cmd","/C","start",rutas.get(vista.getjTable1().getSelectedRow()));
                        if (new File(rutas.get(vista.getjTable1().getSelectedRow()) + File.separator + vista.getModelo().getValueAt(vista.getjTable1().getSelectedRow(), 0)).exists()) {
                            Desktop.getDesktop().open(new File(rutas.get(vista.getjTable1().getSelectedRow())));
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encuentra el archivo seleccionado");
                            vista.getModelo().removeRow(vista.getjTable1().getSelectedRow());
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ControladorTabla.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            vista.getjTable1().getColumn("").setCellEditor(new ButtonEditor(new JCheckBox(), boton));
            Thread.sleep(50);
            vista.getModelo().addRow(insertar);
            vista.getModelo().moveRow(vista.getjTable1().getRowCount() - 1, vista.getjTable1().getRowCount() - 1, 0);
        } catch (InterruptedException ex) {
            Logger.getLogger(ControladorTabla.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String fechaActual() {
        GregorianCalendar actual = new GregorianCalendar();
        String minutos, segundos;
        if (actual.get(GregorianCalendar.MINUTE) < 10) {
            minutos = "0" + actual.get(GregorianCalendar.MINUTE);
        } else {
            minutos = "" + actual.get(GregorianCalendar.MINUTE);
        }
        if (actual.get(GregorianCalendar.SECOND) < 10) {
            segundos = "0" + actual.get(GregorianCalendar.SECOND);
        } else {
            segundos = "" + actual.get(GregorianCalendar.SECOND);
        }
        return actual.get(GregorianCalendar.DATE) + "/" + (actual.get(GregorianCalendar.MONDAY) + 1) + "/" + actual.get(GregorianCalendar.YEAR) + " " + (actual.get(GregorianCalendar.HOUR_OF_DAY)) + ":" + minutos + ":" + segundos;
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {

        setOpaque(true);

    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(Color.lightGray);
        } else {
            setForeground(table.getForeground());
            setBackground(Color.lightGray);
        }
        setText((value == null)
                ? "Abrir Carperta" : "Abrir Carperta");

        return this;

    }
}

class ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private String label;

    public ButtonEditor(JCheckBox checkBox, JButton b) {
        super(checkBox);
        button = b;
        button.setOpaque(true);
        


    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(Color.lightGray);
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(Color.lightGray);
        }
        label = (value == null) ? "" : value.toString();
        button.setText("Abrir carpeta");
        return button;
    }

    @Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }
}
