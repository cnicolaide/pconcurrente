/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auxiliar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 * Con esta clase uno puede excribir en un archivo .txt cualquier tipo de
 * información que necesita ser almacenada. Para usarla, simplemente se
 * instancia y se utiliza el método Escribir al cual se le entra un título
 * (escrito por delante) y el texto que lo acompaña. Los títulos y los textos
 * proporcionados serán escritos en el tiempo en que fueron enviados tanto en la
 * consola como en el .txt
 */
public class Ficheros {

    private String sNombre;
    private String sArchivo;
    private static Ficheros oFicheros = null;
    private SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd HH:ss SSS");

    private Ficheros() {
        this.sNombre = "Redes de Petri Temporales";
        this.sArchivo = "LogPetriNet.txt";
        File fichero = new File(System.getProperty("user.dir") + "\\" + sArchivo);
        if (fichero.exists()) {
            fichero.delete();
        }
    }

    public static Ficheros Instance() {
        if (oFicheros == null) {
            oFicheros = new Ficheros();
        }
        return oFicheros;
    }

    public void Escribir(String sTitulo, String sTexto) {
//        JOptionPane.showMessageDialog(null, sTexto,sTitulo, JOptionPane.ERROR_MESSAGE);
        sTitulo = sTitulo + ": " + sTexto;
        String sTextoFinal = "\r\n" + sd.format(new Date()) + " - " + sTitulo;
        System.out.println(sTextoFinal);
        File fichero = new File(System.getProperty("user.dir") + "\\" + sArchivo);
        PrintWriter pw = null;
        BufferedWriter bw = null;
        try {
            if (fichero.exists()) {
                bw = new BufferedWriter(new FileWriter(fichero, true));
                //pw = new PrintWriter(fichero,true);                                    
            } else {
                bw = new BufferedWriter(new FileWriter(fichero, true));
                //pw = new PrintWriter(fichero);
            }
            bw.write(sTextoFinal);
            bw.close();
            //pw.close();
        } catch (IOException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex, "Escribir", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getsNombre() {
        return sNombre;
    }

    public String getsArchivo() {
        return sArchivo;
    }
}
