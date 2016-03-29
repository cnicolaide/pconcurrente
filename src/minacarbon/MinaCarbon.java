package minacarbon;

import ProcesadorPetri.Matriz;
import Auxiliar.Lector;
import ProcesadorPetri.Redes;
import java.util.HashMap;

public class MinaCarbon {

    public static void main(String[] args) {

        Lector miLector = new Lector("PrioridadSensores.xls", "AutomaticasSensores.xls", "arcosInibidores.html", "TiemposSensores.xls");

        HashMap<String, int[][]> datos = miLector.LeerHTML();

        Matriz Marcado = new Matriz(datos.get("marcado"));
        Matriz Incidencia = new Matriz(datos.get("incidencia"));
        Matriz Inhibicion = new Matriz(datos.get("inhibicion"));

        System.out.println("Marcado inicial: \n" + Marcado.toString());
        System.out.println("Matriz de incidencia: \n" + Incidencia.toString());
        System.out.println("Matriz de inhibicion: \n" + Inhibicion.toString());

        int[][] iDisparo = {{1, 0, 0, 0}};
        int[][] iEsperada = {{1, 0, 1, 1, 0}};

        Matriz Disparo = new Matriz(iDisparo);
        Matriz Esperada = new Matriz(iEsperada);

        Redes oRed = new Redes(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"), null);
        oRed.ejecutar(iDisparo, true);

        Matriz oEsperada = new Matriz(iEsperada);
        Matriz oReal = new Matriz(oRed.getNuevoMarcado());


    }

}
