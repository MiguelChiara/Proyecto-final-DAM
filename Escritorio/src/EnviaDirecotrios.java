
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileFilter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrador
 */
public class EnviaDirecotrios extends Thread {

    private VistaServidor vista;

    public EnviaDirecotrios(VistaServidor vista) throws IOException {
        this.vista = vista;
    }

    private String compruebaRuta() {
        String ruta = vista.getRuta().getText();
        String ficheros = "-:";

        File[] f = new File(ruta).listFiles(new java.io.FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        for (File fich : f) {
            System.out.println(fich.getName());
            ficheros = ficheros+fich.getName() + ":";
        }
        return ficheros;
    }

    @Override
    public void run() {
        
        final int PUERTO = 5000;
        byte[] buffer = new byte[1024];
 
        try {
            System.out.println("Iniciado el servidor UDP");
            //Creacion del socket
            DatagramSocket socketUDP = new DatagramSocket(PUERTO);
 
            //Siempre atendera peticiones
            while (true) {
                 
                //Preparo la respuesta
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
                 
                //Recibo el datagrama
                socketUDP.receive(peticion);
                System.out.println("Recibo la informacion del cliente");
                 
                //Convierto lo recibido y mostrar el mensaje
                String mensaje = new String(peticion.getData());
                System.out.println(mensaje);
 
                //Obtengo el puerto y la direccion de origen
                //Sino se quiere responder, no es necesario
                int puertoCliente = peticion.getPort();
                InetAddress direccion = peticion.getAddress();
 
                mensaje = compruebaRuta();
                buffer = mensaje.getBytes();
                System.out.println(new String(buffer));
 
                //creo el datagrama
                DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length, direccion, puertoCliente);
 
                //Envio la información
                System.out.println("Envio la informacion del cliente");
                socketUDP.send(respuesta);
                 
            }
 
        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
