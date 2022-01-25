package com.miguelchiara.pimiguel.Enviar;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelchiara.pimiguel.Utils.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

// Autor: Miguel Chiara

public class EnviarUrl extends Thread{

    private String url;
    private String ip;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Dispositivos");

    public EnviarUrl(String url,String ip) {
        this.url = url;
        this.ip=ip;


    }

    public void enviaUrl() {
        Socket cliente = null;

        DataOutputStream fujoNombreTexto;

        try {
            cliente = new Socket();
            Log.e("ip cone",ip);
            cliente.connect(new InetSocketAddress(ip, 6000));
            fujoNombreTexto = new DataOutputStream(cliente.getOutputStream());
            ObjectOutputStream enviar = new ObjectOutputStream(cliente.getOutputStream());
            fujoNombreTexto.writeBoolean(true);
            fujoNombreTexto.writeUTF(url);
            fujoNombreTexto.close();

            Map<String,String> mapa = new HashMap<>();
            mapa.put("URL",url);
            myRef.child(Utils.getMacAddress()).child("URLS").push().setValue(mapa);


        } catch (UnknownHostException e) {
            Log.e("NO FUNCIONA", "peto por host");
        } catch (IOException e) {
            Log.e("NO FUNCIONA", "peto io " + e.getMessage());
        } finally {
            if (cliente != null) {
                try {
                    cliente.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        enviaUrl();
    }
}
