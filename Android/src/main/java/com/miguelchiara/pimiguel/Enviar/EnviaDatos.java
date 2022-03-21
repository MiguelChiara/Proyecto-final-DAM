package com.miguelchiara.pimiguel.Enviar;


import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelchiara.pimiguel.Utils.Utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class EnviaDatos extends Thread {

    private String nombre;
    private InputStream inputStream;
    private String ip;
    private String espacio;
    private String ruta;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Dispositivos");
    Semaphore s;


    public EnviaDatos(String nombre, InputStream inputStream,String ip,String espacio,String ruta,Semaphore s) {
        this.nombre = nombre;
        this.inputStream = inputStream;
        this.ip=ip;
        this.espacio =espacio;
        this.ruta=ruta;
        this.s=s;
    }

    public void enviaDatos() {

        try {
            s.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Socket cliente = null;

        DataOutputStream fujoNombreTexto;

        try {
            ArrayList<byte[]> bts = new ArrayList<>();

            //InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            /*
            final byte[] BUFFER = new byte[2048];
            int readed = -1;
            while ((readed = inputStream.read(BUFFER)) != -1) {
                baos.write(BUFFER, 0, readed);

                if (baos.size() == 2048000 || readed < 2048) {
                    bts.add(baos.toByteArray());
                    baos.reset();
                }
            }
            */

            cliente = new Socket();
            Log.e("ip cone",ip);
            cliente.connect(new InetSocketAddress(ip, 6000));
            ObjectOutputStream enviar = new ObjectOutputStream(cliente.getOutputStream());
            fujoNombreTexto = new DataOutputStream(cliente.getOutputStream());
            fujoNombreTexto.writeBoolean(false);
            fujoNombreTexto.writeUTF(nombre);
            fujoNombreTexto.writeUTF(ruta);
            fujoNombreTexto.writeUTF(Utils.obtenerNombreDeDispositivo());

            final byte[] BUFFER = new byte[1048576];
            int readed = -1;

            while ((readed = inputStream.read(BUFFER)) != -1) {
                baos.write(BUFFER, 0, readed);
                if (baos.size() == 1048576000 || readed < 1048576) {
                    bts.add(baos.toByteArray());
                    enviar.writeObject(bts);
                    baos.reset();
                }
            }
            enviar.writeObject(bts);




            Map<String,String> mapa = new HashMap<>();
            mapa.put("Nombre",nombre);
            mapa.put("Espacio",humanReadableByteCountSI(Long.parseLong(espacio)));
            myRef.child(Utils.getMacAddress()).child("Archivos").push().setValue(mapa);

            inputStream.close();
            fujoNombreTexto.close();
            enviar.close();

        } catch (UnknownHostException e) {
            Log.e("NO FUNCIONA", "peto por host");
        } catch (IOException e) {
            Log.e("e",e.getMessage());
        } finally {
            if (cliente != null) {

                try {
                    cliente.close();
                    s.release();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        enviaDatos();
    }

    public static String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }
}
