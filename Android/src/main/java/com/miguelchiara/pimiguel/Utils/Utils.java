package com.miguelchiara.pimiguel.Utils;

import android.os.Build;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class Utils {
    public static String IDIP="Clave";
    public static String ULTIMAACTIVIDAD="ultimaActividad";
    public static String NOMBREFICHERO="X";
    public static String EDITTEXT="texto";
    public static String NOMBREHOST="host";
    public static String SELECTOR="seleccionado";
    public static String CHECK="check";
    public static String CARPETASELECT="carpeta";
    public static String CARPETAPORFECTO="";
    public static String FICHEROEJECUTADO="ejecutado";
    public static String PRIMERAVEZ="primeravez";


    public static String obtenerNombreDeDispositivo() {
        String fabricante = Build.MANUFACTURER;
        String modelo = Build.MODEL;
        if (modelo.startsWith(fabricante)) {
            return primeraLetraMayuscula(modelo);
        } else {
            return primeraLetraMayuscula(fabricante) + " " + modelo;
        }
    }

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {

        }
        return "";
    }


    private static String primeraLetraMayuscula(String cadena) {
        if (cadena == null || cadena.length() == 0) {
            return "";
        }
        char primeraLetra = cadena.charAt(0);
        if (Character.isUpperCase(primeraLetra)) {
            return cadena;
        } else {
            return Character.toUpperCase(primeraLetra) + cadena.substring(1);
        }
    }
}
