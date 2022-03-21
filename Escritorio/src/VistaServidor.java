
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrador
 */
public class VistaServidor extends javax.swing.JFrame {

    /**
     * Creates new form VistaServidor
     */
    DefaultTableModel modelo;
    private boolean vivo = true;
    private MenuItem botonMostrar;
    private MenuItem botonOcultar;

    public VistaServidor() {
        Image icon = new ImageIcon(getClass().getResource("logoRedi.png")).getImage();
        setIconImage(icon);
        setTitle("Mercurio");
        modelo = new DefaultTableModel();
        initComponents();
        modelo = (DefaultTableModel) jTable1.getModel();
        jTable1.setRowSorter(new TableRowSorter(modelo));
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(100);
        jTable1.setRowHeight(25);

        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                vivo = false;
                //System.exit(1);
            }
        });


        //Icono

        final TrayIcon trayIcon;

        /*Se verifica si el sistema soporta los try icons*/
        if (SystemTray.isSupported()) {

            SystemTray tray = SystemTray.getSystemTray();

            //Se asigna la imagen que del tray icon
            ImageIcon im = new ImageIcon(VistaServidor.class.getResource("logoRedi.png"));
            Image image = Toolkit.getDefaultToolkit().getImage("logoRedi.png");

            //Aquí se crea un popup menu
            PopupMenu popup = new PopupMenu();

            //Se agrega la opción
            botonMostrar = new MenuItem("Mostrar");
            botonMostrar.setEnabled(false);
            botonOcultar = new MenuItem("Ocultar");

            MenuItem botonSalida = new MenuItem("Exit");


            //Este listener permite salir de la aplicacion
            ActionListener exitListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVivo(false);
                }
            };

            ActionListener ocultarListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //System.out.println("Exiting...");
                    botonMostrar.setEnabled(true);
                    botonOcultar.setEnabled(false);
                    setVisible(false);
                }
            };

            ActionListener mostrarListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //System.out.println("Exiting...");
                    botonMostrar.setEnabled(false);
                    botonOcultar.setEnabled(true);
                    setVisible(true);
                }
            };

            //Se le asigna al item del popup el listener para salir de la app
            botonMostrar.addActionListener(mostrarListener);
            botonOcultar.addActionListener(ocultarListener);
            botonSalida.addActionListener(exitListener);

            popup.add(botonMostrar);
            popup.add(botonOcultar);
            popup.add(botonSalida);


            /*Aqui creamos una instancia del tray icon y asignamos
             La imagen, el nombre del tray icon y el popup*/
            trayIcon = new TrayIcon(im.getImage(), "Mercurio", popup);

            /*Creamos un acction listener que se ejecuta cuando le damos
             doble click al try icon*/
            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    trayIcon.displayMessage("Action Event",
                            "An Action Event Has Been Peformed!",
                            TrayIcon.MessageType.INFO);
                }
            };


            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);

            try {

                tray.add(trayIcon);

            } catch (AWTException ex) {
                ex.printStackTrace();
            }

        } else {
            System.err.println("System tray is currently not supported.");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        ruta = new javax.swing.JTextField();
        jProgressBar1 = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        borrarTodo = new javax.swing.JMenuItem();
        borrarRegistro = new javax.swing.JMenuItem();
        limpiarTabla = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Archivo", "Dispositivo", "Fecha", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.PAGE_START);

        jButton1.setText("Examinar");
        jButton1.setPreferredSize(new java.awt.Dimension(50, 23));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ruta, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ruta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        getContentPane().add(jProgressBar1, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Opciones");

        borrarTodo.setText("Borrar todo");
        borrarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarTodoActionPerformed(evt);
            }
        });
        jMenu1.add(borrarTodo);

        borrarRegistro.setText("Borrar elemento seleccionado");
        borrarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarRegistroActionPerformed(evt);
            }
        });
        jMenu1.add(borrarRegistro);

        limpiarTabla.setText("Limpiar tabla");
        limpiarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarTablaActionPerformed(evt);
            }
        });
        jMenu1.add(limpiarTabla);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JFileChooser fch = new JFileChooser();
        fch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fch.showOpenDialog(this);
        File archivo = fch.getSelectedFile();
        if (archivo != null) {
            ruta.setText(archivo.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void borrarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarTodoActionPerformed

            for (int i = jTable1.getRowCount(); i > 0; i--) {
                modelo.removeRow(0);
            }
            
            if (jTable1.getRowCount()==0) {
                JOptionPane.showMessageDialog(this, "Registro borrado");
            } else {
                JOptionPane.showMessageDialog(this, "Borrar registro");
            }
    }//GEN-LAST:event_borrarTodoActionPerformed

    private void borrarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarRegistroActionPerformed

            int[] rows = jTable1.getSelectedRows(); // En plural el rows.
            Arrays.sort(rows);

            for (int i = rows.length - 1; i >= 0; i--) {
                modelo.removeRow(rows[i]);
            }
    }//GEN-LAST:event_borrarRegistroActionPerformed

    private void limpiarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarTablaActionPerformed
        // TODO add your handling code here:
        for (int i = jTable1.getRowCount(); i > 0; i--) {
            modelo.removeRow(0);
        }
    }//GEN-LAST:event_limpiarTablaActionPerformed

    /**
     * @param args the command line arguments
     */
    public JTable getjTable1() {
        return jTable1;
    }

    public void setjTable1(JTable jTable1) {
        this.jTable1 = jTable1;
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }

    public void setModelo(DefaultTableModel modelo) {
        this.modelo = modelo;
    }

    public JTextField getRuta() {
        return ruta;
    }

    public void setRuta(JTextField ruta) {
        this.ruta = ruta;
    }

    public JMenuItem getBorrarRegistro() {
        return borrarRegistro;
    }

    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    public JProgressBar getjProgressBar1() {
        return jProgressBar1;
    }

    public void setjProgressBar1(JProgressBar jProgressBar1) {
        this.jProgressBar1 = jProgressBar1;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem borrarRegistro;
    private javax.swing.JMenuItem borrarTodo;
    private javax.swing.JButton jButton1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JMenuItem limpiarTabla;
    private javax.swing.JTextField ruta;
    // End of variables declaration//GEN-END:variables
}
