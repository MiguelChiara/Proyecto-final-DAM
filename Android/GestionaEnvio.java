package com.miguelchiara.pimiguel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelchiara.pimiguel.Enviar.EnviaDatos;
import com.miguelchiara.pimiguel.Enviar.EnviarUrl;
import com.miguelchiara.pimiguel.Spinner.CustomItem;
import com.miguelchiara.pimiguel.Spinner.CustomSpinnerAdapter;
import com.miguelchiara.pimiguel.Utils.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

// Autor: Miguel Chiara

public class GestionaEnvio extends AppCompatActivity {


    private String nombre;
    private Uri uri;
    private String ip;
    private String ruta;
    private EditText editor;
    private Spinner directoriosSpinner;
    private String[] directorios;
    private String host;
    private int posSpinner = 0;
    private boolean activado = true;
    private String espacio;
    private ArrayList<CustomItem> listaItems;
    private ProgressBar p;
    Semaphore available = new Semaphore(10);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestio_envio);
        //TextView texto = findViewById(R.id.textoIP);
        editor = ((EditText) findViewById(R.id.ruta));
        directoriosSpinner = findViewById(R.id.spinner);
        listaItems=new ArrayList<CustomItem>();
        p=findViewById(R.id.bar);
        p.setVisibility(View.INVISIBLE);
        ActionBar actionBar = getSupportActionBar();


        SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
        ip = prefs.getString(Utils.IDIP, "No conectado");

/*
       new AsyncTask<Void,Void,Void>(){
            boolean conectado=true;
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Socket s=new Socket(ip, 6000);
                    s.close();
                } catch (Exception e){
                    exception.set(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if(exception.get()!=null){
                    Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
                    getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS).edit().clear().commit();
                    onBackPressed();
                }else{
                    Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
*/



        host = prefs.getString(Utils.NOMBREHOST, "NULL");
        //texto.setText("conectado a " + host);
        actionBar.setTitle(host);
        CheckBox checkbox = findViewById(R.id.checkBox);
        activado = prefs.getBoolean(Utils.CHECK, true);
        checkbox.setChecked(activado);


        if (checkbox.isChecked()) {
            directoriosSpinner.setEnabled(false);
            editor.setEnabled(true);

            ruta = editor.getText().toString();
            Log.e("Ruta->", ruta);
            if (ruta.matches("")) {
                ruta = "";
                prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
                ruta = prefs.getString(Utils.EDITTEXT, "");
                editor.setText(ruta);
                if(ruta.matches("")){
                    ruta=Utils.CARPETAPORFECTO;
                }
            }

        } else {
            directoriosSpinner.setEnabled(true);
            editor.setEnabled(false);

            try {

                Log.e("pos2->", "" + posSpinner);
                ruta = prefs.getString(Utils.CARPETASELECT, Utils.CARPETAPORFECTO);
            } catch (NullPointerException ex) {
                Log.e("Ruta->", ruta);
            }
        }

/*
        new AsyncTask<Void, Void, Void>() {
            SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);

            @Override
            protected Void doInBackground(Void... voids) {

                //puerto del servidor
                final int PUERTO_SERVIDOR = 5000;
                //buffer donde se almacenara los mensajes
                byte[] buffer = new byte[1024];

                try {
                    //Obtengo la localizacion de localhost
                    InetAddress direccionServidor = InetAddress.getByName(ip);

                    //Creo el socket de UDP
                    DatagramSocket socketUDP = new DatagramSocket();

                    String mensaje = "";

                    //Convierto el mensaje a bytes
                    //buffer = mensaje.getBytes();

                    //Creo un datagrama
                    DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, direccionServidor, PUERTO_SERVIDOR);

                    //Lo envio con send
                    socketUDP.send(pregunta);

                    //Preparo la respuesta
                    DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

                    //Recibo la respuesta
                    socketUDP.receive(peticion);

                    //Cojo los datos y lo muestro
                    mensaje = new String(peticion.getData()).trim();
                    Log.e("Que paso", mensaje);
                    directorios = mensaje.split(":");

                    //cierro el socket
                    socketUDP.close();

                } catch (SocketException ex) {

                } catch (UnknownHostException ex) {

                } catch (IOException ex) {

                }

                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try {
                    ArrayAdapter<String> itemsAdapter =
                            new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, directorios);
                    directoriosSpinner.setAdapter(itemsAdapter);
                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Error en la conexion", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
*/
        Thread carpetas = new Thread(new Runnable() {
            @Override
            public void run() {

                //puerto del servidor
                final int PUERTO_SERVIDOR = 5000;
                //buffer donde se almacenara los mensajes
                byte[] buffer = new byte[1024];

                try {
                    //Obtengo la localizacion de localhost
                    InetAddress direccionServidor = InetAddress.getByName(ip);

                    //Creo el socket de UDP
                    DatagramSocket socketUDP = new DatagramSocket();

                    String mensaje = "";

                    //Convierto el mensaje a bytes
                    //buffer = mensaje.getBytes();

                    //Creo un datagrama
                    DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, direccionServidor, PUERTO_SERVIDOR);

                    //Lo envio con send
                    socketUDP.send(pregunta);

                    //Preparo la respuesta
                    DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

                    //Recibo la respuesta
                    socketUDP.receive(peticion);

                    //Cojo los datos y lo muestro
                    mensaje = new String(peticion.getData()).trim();
                    Log.e("Que paso", mensaje);
                    directorios = mensaje.split(":");

                    //cierro el socket
                    socketUDP.close();

                } catch (SocketException ex) {

                } catch (UnknownHostException ex) {

                } catch (IOException ex) {

                }

            }
        });

        carpetas.start();
        try {
            carpetas.join(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            int a=directorios.length;
            Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_SHORT).show();
        }catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        try {
            for (String directorio:directorios){
                listaItems.add(new CustomItem(directorio));
            }
            listaItems.set(0,new CustomItem("Selecciona carpeta"));
            CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this,listaItems);
            directoriosSpinner.setAdapter(adapter);
            /*
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, directorios);
            directoriosSpinner.setAdapter(itemsAdapter);
            */
        } catch (NullPointerException e) {
        }




        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkbox.isChecked()) {
                    directoriosSpinner.setEnabled(false);
                    editor.setEnabled(true);
                } else {
                    directoriosSpinner.setEnabled(true);
                    editor.setEnabled(false);
                }
                activado = !activado;
                SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(Utils.CHECK, activado).commit();
                Log.e("check", "" + activado);
            }
        });


        directoriosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    position = prefs.getInt(Utils.SELECTOR, 0);
                    directoriosSpinner.setSelection(position);
                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(Utils.CARPETASELECT,((CustomItem)directoriosSpinner.getItemAtPosition(position)).getSpinnerItemName());
                    editor.putInt(Utils.SELECTOR, position);
                    editor.commit();
                }


                Log.e("pos->", "" + position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        try {
            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if (type.startsWith("text/plain")) {
                    Log.e("Entro?","???");
                    handleSendText(intent); // Handle text being sent
                } else if (type.startsWith("image/")) {
                    handleSend(intent); // Handle single image being sent
                } else if (type.startsWith("video/")) {
                    handleSend(intent); // Handle single image being sent
                }else if (type.startsWith("application/")){
                    Log.e("Entro?2","???");
                    handleSend(intent);
                }
            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
                handleSendMultiple(intent); // Handle multiple images being sent
            } else {
                // Handle other intents, such as being started from the home screen
            }
        } catch (FileNotFoundException ex) {

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        editor.clearFocus();
        ruta = editor.getText().toString();
        SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Utils.EDITTEXT, ruta);
        editor.commit();

        hideKeyboard(this);

        return super.onTouchEvent(event);
    }


    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            enviaUrl(sharedText);
        }
    }

    void handleSend(Intent intent) throws FileNotFoundException {
        Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        String nombre = "";
        if (uri != null) {
            // Update UI to reflect image being shared
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int indexName = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int indexSize = cursor.getColumnIndex(OpenableColumns.SIZE);
            this.nombre = cursor.getString(indexName);
            this.espacio = cursor.getString(indexSize);
            this.uri = uri;
            Log.e("URI", uri.toString());
            Log.e("nombre a", cursor.getString(indexName));
            enviaDatos(uri, cursor.getString(indexName),cursor.getString(indexSize));
        }
    }

    void handleSendMultiple(Intent intent) {
        ArrayList<Uri> uris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        for (Uri uri : uris) {
            String nombre = "";
            if (uri != null) {
                // Update UI to reflect image being shared
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int indexName = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int indexSize = cursor.getColumnIndex(OpenableColumns.SIZE);
                enviaDatos(uri, cursor.getString(indexName),cursor.getString(indexSize));
            }
        }
    }

    public void enviaUrl(String url){
        new EnviarUrl(url,ip).start();
    }

    public void enviaDatos(Uri uri, String nombre,String espacio) {


        Log.e("Espacio",espacio);
        if(Long.parseLong(espacio)<1048576000) {

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                p.setVisibility(View.VISIBLE);
                EnviaDatos e = new EnviaDatos(nombre, inputStream, ip, espacio, ruta,available);
                e.start();
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        while (e.isAlive()) ;
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        //Toast.makeText(getApplicationContext(), "Archivo "+nombre+" pasado", Toast.LENGTH_SHORT).show();
                        super.onPostExecute(aVoid);
                        p.setVisibility(View.INVISIBLE);
                    }
                }.execute();
            } catch (IOException e) {
                Log.e("NO FUNCIONA", "peto io " + e.getMessage());
            }
        }else {
            Toast.makeText(this, "Solo se pueden pasar archivos con peso menor a 1GB", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Utils.ULTIMAACTIVIDAD, "");
        editor.commit();
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Utils.ULTIMAACTIVIDAD, getClass().getName());
        editor.putString(Utils.IDIP, ip);
        editor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
        ip = prefs.getString(Utils.IDIP, "No conectado");
        host = prefs.getString(Utils.NOMBREHOST, "NULL");
        CheckBox checkbox = findViewById(R.id.checkBox);
        activado = prefs.getBoolean(Utils.CHECK, true);
        Log.e("->", "" + prefs.getBoolean(Utils.CHECK, true));
        checkbox.setChecked(activado);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Utils.ULTIMAACTIVIDAD, "");
        editor.clear().commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Utils.ULTIMAACTIVIDAD, getClass().getName());
        editor.commit();
    }

}