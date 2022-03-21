package com.miguelchiara.pimiguel;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelchiara.pimiguel.ListView.ListViewBaseAdapter;
import com.miguelchiara.pimiguel.ListView.ListViewBean;
import com.miguelchiara.pimiguel.Utils.Utils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private LinkedList<String> listaIps = new LinkedList<>();
    private LinkedList<String> nombresHosts = new LinkedList<>();
    ProgressBar p;
    ListView lista;
    ListViewBaseAdapter adapter;
    ArrayList<ListViewBean> arr_bean;

    //FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_PIMiguel);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar(); actionBar.hide();


        Log.e("->", "Ejecuta");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Dispositivos");

        myRef.orderByChild("MAC").equalTo(Utils.getMacAddress()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("Nombre", Utils.obtenerNombreDeDispositivo());
                    map.put("MAC", Utils.getMacAddress());
                    myRef.child(Utils.getMacAddress()).setValue(map);
                    Log.e("->", "Ejecuta2");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        /*
        boolean ejecutado;

        SharedPreferences primeraVez = getSharedPreferences(Utils.FICHEROEJECUTADO, MODE_MULTI_PROCESS);
        ejecutado=primeraVez.getBoolean(Utils.PRIMERAVEZ,true);
        if(ejecutado){
            primeraVez.edit().putBoolean(Utils.PRIMERAVEZ,false).commit();
        }
        */


        try {
            Class activityClass;
            SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
            activityClass = Class.forName(
                    prefs.getString(Utils.ULTIMAACTIVIDAD, "null"));
            startActivity(new Intent(this, activityClass));
        } catch (ClassNotFoundException ex) {
        }

        p = findViewById(R.id.progressBar);
        p.setVisibility(View.INVISIBLE);
        lista = findViewById(R.id.listView);
        arr_bean=new ArrayList<ListViewBean>();


        WifiManager.MulticastLock lock = null;
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            lock = wifi.createMulticastLock("pseudo-ssdp");
            lock.acquire();
        }


        if (lock != null) {
            lock.release();
            lock = null;
        }


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), GestionaEnvio.class);

                SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(Utils.IDIP, listaIps.get(position));
                editor.putString(Utils.NOMBREHOST, nombresHosts.get(position));
                editor.commit();
                startActivity(i);
            }
        });

    }

    public void buscaDispositivos(View v) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                nombresHosts.clear();
                listaIps.clear();
                arr_bean.clear();
                SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
                prefs.edit().clear().commit();
                Toast.makeText(getApplicationContext(), "Buscando dispositivos", Toast.LENGTH_SHORT).show();
                p.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    byte[] b = new byte[100];
                    DatagramPacket dgram = new DatagramPacket(b, b.length);
                    MulticastSocket socket = new MulticastSocket(4000);
                    socket.joinGroup(InetAddress.getByName("235.1.1.1"));

                    long t = System.currentTimeMillis();
                    long end = t + 10 * 1000;
                    String ip;
                    socket.setSoTimeout(5000);
                    while (System.currentTimeMillis() < end) {
                        socket.receive(dgram); // Se bloquea hasta que llegue un datagrama
                        ip = dgram.getAddress().getHostAddress();
                        String host = dgram.getAddress().getHostName();
                        if (ip.equals(host)) {
                            host = new String(dgram.getData());
                        }
                        if (!listaIps.contains(ip) && !nombresHosts.contains(host)) {
                            listaIps.add(ip);
                            nombresHosts.add(host);
                            arr_bean.add(new ListViewBean(R.drawable.logopc,host));
                            Log.e("host", "->" + host);
                            Log.e("IP", "->" + ip);
                        } else {
                            Log.e("hola", "->" + "null");
                        }
                    }
                    socket.leaveGroup(InetAddress.getByName("235.1.1.1"));
                    socket.close();


                } catch (SocketException ex) {

                } catch (UnknownHostException ex) {
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                p.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Dispositivos encontrados " + listaIps.size(), Toast.LENGTH_SHORT).show();
                /*
                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, nombresHosts);
                lista.setAdapter(itemsAdapter);
                 */
                adapter=new ListViewBaseAdapter(arr_bean,getApplicationContext());
                lista.setAdapter(adapter);

            }
        }.execute();
        //BuscaDispositivos b = new BuscaDispositivos();
        //b.start();
    }

    public void leeTodaLaRed(View v) {
        new AsyncTask<Void, Void, Void>() {
            private static final String TAG = "NetworkSniffTask";
            private WeakReference<Context> mContextRef = new WeakReference<>(getApplicationContext());

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                nombresHosts.clear();
                listaIps.clear();
                arr_bean.clear();
                SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
                prefs.edit().clear().commit();
                Toast.makeText(getApplicationContext(), "Buscando dispositivos", Toast.LENGTH_SHORT).show();
                p.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Log.e(TAG, "Let's sniff the network");
                try {
                    Context context = mContextRef.get();
                    if (context != null) {
                        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo connectionInfo = wm.getConnectionInfo();
                        int ipAddress = connectionInfo.getIpAddress();
                        String ipString = Formatter.formatIpAddress(ipAddress);
                        Log.e(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                        Log.e(TAG, "ipString: " + String.valueOf(ipString));
                        String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                        Log.e(TAG, "prefix: " + prefix);
                        for (int i = 0; i < 255; i++) {
                            String testIp = prefix + String.valueOf(i);
                            InetAddress name = InetAddress.getByName(testIp);
                            String hostName = name.getCanonicalHostName();
                            if (!Character.isDigit(hostName.charAt(0))||name.isReachable(20)) {
                                Log.e(TAG, "Host:" + hostName);
                                nombresHosts.add(hostName);
                                arr_bean.add(new ListViewBean(R.drawable.logopc,hostName));
                                listaIps.add(name.getHostAddress());
                            }
                        }
                    }
                } catch (Throwable t) {
                    Log.e(TAG, "Well that's not good.", t);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                p.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Dispositivos encontrados " + listaIps.size(), Toast.LENGTH_SHORT).show();
                /*
                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, nombresHosts);
                lista.setAdapter(itemsAdapter);
                 */
                adapter=new ListViewBaseAdapter(arr_bean,getApplicationContext());
                lista.setAdapter(adapter);
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Utils.ULTIMAACTIVIDAD, "");
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Utils.CARPETASELECT, "-");
        editor.putInt(Utils.SELECTOR, 0);
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Utils.ULTIMAACTIVIDAD, "");
        editor.commit();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            Class activityClass;
            SharedPreferences prefs = getSharedPreferences(Utils.NOMBREFICHERO, MODE_MULTI_PROCESS);
            activityClass = Class.forName(
                    prefs.getString(Utils.ULTIMAACTIVIDAD, "null"));
            startActivity(new Intent(this, activityClass));
        } catch (ClassNotFoundException ex) {
        }
    }
}