
import java.awt.List;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrador
 */
public class Cliente extends Thread {

    private Socket cliente;
    private ControladorTabla control;
    private String ruta;
    private VistaServidor v;

    public Cliente(Socket cliente, ControladorTabla control, String ruta, VistaServidor v) {
        this.cliente = cliente;
        this.control = control;
        this.v = v;
        if (ruta != "") {
            this.ruta = ruta;
        } else {
            this.ruta = File.listRoots()[0].getAbsolutePath();
        }

    }

    @Override
    public void run() {
        String nombreArchivo;
        String nombreDispositivo;
        String direccion;
        ArrayList<byte[]> bts;
        try {


            DataInputStream fujoTexto = new DataInputStream(cliente.getInputStream());
            ObjectInputStream lectura = new ObjectInputStream(cliente.getInputStream());

            if (fujoTexto.readBoolean()) {
                abreUrl(fujoTexto.readUTF());
            } else {
                nombreArchivo = fujoTexto.readUTF();
                String carpeta=fujoTexto.readUTF().trim();
                if(carpeta.equals("-")){
                    carpeta="";
                }
                direccion = ruta + separador() + carpeta;
                nombreDispositivo = fujoTexto.readUTF();
                System.out.println("aqui " + ruta);
                System.out.println(nombreArchivo);




                File auxChivo = new File(direccion);
                System.out.println(auxChivo.getAbsolutePath());
                auxChivo.mkdirs();

                OutputStream out = new FileOutputStream(direccion + separador() + nombreArchivo);
                    bts = (ArrayList<byte[]>) lectura.readObject();
                    v.getjProgressBar1().setMinimum(0);
                    v.getjProgressBar1().setMaximum(bts.size());
                    for (int i = 0; i < bts.size(); i++) {
                        out.write(bts.get(i));
                        v.getjProgressBar1().setValue(i);
                    }
                v.getjProgressBar1().setValue(0);
                out.close();

                control.insertaFila(nombreArchivo, nombreDispositivo, direccion);
            }
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (NullPointerException e) {
            //e.printStackTrace();
        } finally {
            try {
                if (cliente != null) {
                    cliente.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private void abreUrl(String URL) {
        System.out.println("Entra " + URL);
        URL = extractUrls(URL).get(0);
        System.out.println("Sale " + URL);
        if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                try {
                    java.net.URI uri = new java.net.URI(URL);
                    desktop.browse(uri);
                } catch (URISyntaxException | IOException ex) {
                }
            }
        }
    }

    private String separador(){
        String barra;
        if(System.getProperty("os.name").contains("Windows")){
            barra="\\";
        }else{
            barra="/";
        }
        return barra;
    }
    private LinkedList<String> extractUrls(String text) {
        LinkedList<String> containedUrls = new LinkedList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }
}
