/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bersu
 */

import ProcesadorPetri.Matriz;
import ProcesadorPetri.Lector;
import java.util.HashMap;


public class TestArcoInibidor {
    
        public static void main(String[] args) {

        Lector miLector = new Lector("PrioridadSensores.xls","AutomaticasSensores.xls","arcosInibidores.html","TiemposSensores.xls");

        HashMap<String, int[][]> datos = miLector.LeerHTML();

        Matriz Marcado = new Matriz(datos.get("marcado"));
        Matriz Incidencia = new Matriz(datos.get("incidencia"));
        Matriz Inhibicion = new Matriz(datos.get("inhibicion"));

        
        System.out.println ("Marcado inicial: " + Marcado.toString());
        System.out.println ("Matriz de incidencia: " + Incidencia.toString());
    }
        
    @Test
    public void testDispararSensibilizada() 
    {
        Ficheros.Instance().Escribir("TEST","DISPARAR_SENSIBILIZADA");
        int[][] iDisparo  = {{0, 0, 1, 0, 0}};
        int[][] iEsperada = {{0, 1, 1, 0}};
        
        Redes oRed = new Redes(iMarcadoInicial, iIncidencia, iH,null);
        oRed.ejecutar(iDisparo,true);        
        
        Matriz oEsperada = new Matriz(iEsperada);
        Matriz oReal =  new Matriz(oRed.getNuevoMarcado());
        
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < oReal.getColCount(); j++) {
                assertEquals(oEsperada.getVal(i, j), oReal.getVal(i, j));                
            }            
        }        
    }
    
}
