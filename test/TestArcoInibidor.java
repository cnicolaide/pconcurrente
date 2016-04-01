
import ProcesadorPetri.Matriz;
import Auxiliar.Lector;
import ProcesadorPetri.Red;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestArcoInibidor {

    Lector miLector = new Lector("PrioridadSensores.xls", "AutomaticasSensores.xls", "arcosInibidores.html", "TiemposSensores.xls");

    HashMap<String, int[][]> datos = miLector.LeerHTML();

    @Test
    public void testDispararNOSensibilizada() {

        int[][] iDisparo = {{1, 0, 0, 0}};
        int[][] iEsperada = {{1, 0, 1, 1, 0}};

        Matriz Disparo = new Matriz(iDisparo);
        Matriz Esperada = new Matriz(iEsperada);

        Red oRed = new Red(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"), null);
        oRed.ejecutar(iDisparo, true);

        Matriz oEsperada = new Matriz(iEsperada);
        Matriz oReal = new Matriz(oRed.getNuevoMarcado());

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < oReal.getColCount(); j++) {
                assertEquals(oEsperada.getVal(i, j), oReal.getVal(i, j));
            }
        }

    }

    @Test
    public void testDisparaSensibilizada() {

        int[][] iDisparo = {{0, 1, 0, 0}};
        int[][] iEsperada = {{1, 0, 0, 0, 1}};

        Matriz Disparo = new Matriz(iDisparo);
        Matriz Esperada = new Matriz(iEsperada);

        Red oRed = new Red(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"), null);
        oRed.ejecutar(iDisparo, true);

        Matriz oEsperada = new Matriz(iEsperada);
        Matriz oReal = new Matriz(oRed.getNuevoMarcado());

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < oReal.getColCount(); j++) {
                assertEquals(oEsperada.getVal(i, j), oReal.getVal(i, j));
            }
        }

    }

}
