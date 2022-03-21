
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrador
 */
//https://sethlopez.me/article/writing-a-upnp-control-point-in-javascript-part-two/
public class PublicaIp extends Thread{

    public PublicaIp() {
    }

    
    
    @Override
    public void run() {
        try {

            MulticastSocket socket = new MulticastSocket();


            byte[] b = InetAddress.getLocalHost().getHostName().getBytes();
            DatagramPacket dgram;

            dgram = new DatagramPacket(b, b.length, InetAddress.getByName("235.1.1.1"), 4000);

            System.err.println("Enviando " + b.length + " bytes a "
                    + dgram.getAddress() + ':' + dgram.getPort());
            while (true) {
                System.err.print(".");
                socket.send(dgram);
                Thread.sleep(5000);
            }

        } catch (SocketException ex) {
            
        } catch (UnknownHostException ex) {
            
        } catch (IOException ex) {
            Logger.getLogger(PublicaIp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(PublicaIp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}