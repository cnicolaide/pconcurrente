package minacarbon;

import ProcesadorPetri.Matriz;
import ProcesadorPetri.Lector;
import java.util.HashMap;

public class MinaCarbon {

    public static void main(String[] args) {

        Lector miLector = new Lector("PrioridadSensores.xls","AutomaticasSensores.xls","Sensores.html","TiemposSensores.xls");

        HashMap<String, int[][]> datos = miLector.LeerHTML();

        Matriz Marcado = new Matriz(datos.get("marcado"));
        Matriz Incidencia = new Matriz(datos.get("incidencia"));
        Matriz Inhibicion = new Matriz(datos.get("inhibicion"));

        
        System.out.println ("" + Marcado.toString());
        System.out.println ("" + Incidencia.toString());
    }

}
