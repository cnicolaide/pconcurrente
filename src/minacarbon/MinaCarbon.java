package minacarbon;

import ProcesadorPetri.Matriz;
import ProcesadorPetri.Lector;
import java.util.HashMap;

public class MinaCarbon {

    public static void main(String[] args) {

        Lector miLector = new Lector("PrioridadSensores.xls","AutomaticasSensores.xls","arcosInibidores.html","TiemposSensores.xls");

        HashMap<String, int[][]> datos = miLector.LeerHTML();

        Matriz Marcado = new Matriz(datos.get("marcado"));
        Matriz Incidencia = new Matriz(datos.get("incidencia"));
        Matriz Inhibicion = new Matriz(datos.get("inhibicion"));
        
        int[][] iDisparo  = {{0, 0, 1, 0, 0}};
        int[][] iEsperada = {{0, 1, 1, 0}};
        
        Matriz Disparo = new Matriz(iDisparo);
        Matriz Esperada = new Matriz(iEsperada);
        
        System.out.println ("Marcado inicial: \n" + Marcado.toString());
        System.out.println ("Matriz de incidencia: \n" + Incidencia.toString());
        System.out.println("Matriz de inhibicion: \n"+ Inhibicion.toString());
        
    
    }

}
