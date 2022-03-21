
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrador
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ControladorTabla control = null;
        try {
            // TODO code application logic here

            Socket cliente;

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            ServerSocket server = new ServerSocket(6000);
            new PublicaIp().start();
            VistaServidor vista = new VistaServidor();
            vista.getRuta().setText(File.listRoots()[0].toString());
            control = new ControladorTabla(vista);
            new EnviaDirecotrios(vista).start();
            server.setSoTimeout(1000);
            while (vista.isVivo()) {
                try {
                    cliente = server.accept();
                    if(cliente!=null){
                        new Cliente(cliente, control, vista.getRuta().getText(),vista).start();
                    }
                    
                } catch (SocketTimeoutException e) {
                }
            }
            //out.close();

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //JOptionPane.showMessageDialog(null, "Ero");
            System.exit(0);
        }

        /* catch (ClassNotFoundException ex) {
         Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
         }*/
    }
}
