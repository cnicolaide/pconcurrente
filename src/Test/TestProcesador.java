package Test;

import static org.junit.Assert.*;

import java.util.HashMap;
import org.junit.Test;
import Auxiliar.Lector;
import Auxiliar.Matriz;
import ProcesadorPetri.RdP;

public class TestProcesador {

	Lector miLector = new Lector("PrioridadSensores.xls", "AutomaticasSensores.xls", "arcosInibidores.html",
			"TiemposSensores.xls");
	HashMap<String, Matriz> datos = miLector.LeerHTML();

	@Test
	public void testDispararNOSensibilizada() {

		int[][] iDisparo = { { 1, 0, 0, 0 } };
		int[][] iEsperada = { { 1, 0, 1, 1, 0 } };

		Matriz Disparo = new Matriz(iDisparo);
		Matriz Esperada = new Matriz(iEsperada);

		RdP oRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"));
		oRed.ejecutar(Disparo, true);

		Matriz oReal = new Matriz(oRed.getNuevoMarcado());

		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < oReal.getColCount(); j++) {
				assertEquals(Esperada.getVal(i, j), oReal.getVal(i, j));
			}
		}

	}

	@Test
	public void testDispararSensibilizada() {

		int[][] iDisparo = { { 0, 1, 0, 0 } };
		int[][] iEsperada = { { 1, 0, 0, 0, 1 } };

		Matriz Disparo = new Matriz(iDisparo);
		Matriz Esperada = new Matriz(iEsperada);

		RdP oRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"));
		
		oRed.getSensibilizadas();
		oRed.ejecutar(Disparo, true);
		oRed.getSensibilizadas();

		Matriz mReal = new Matriz(oRed.getNuevoMarcado());

		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < mReal.getColCount(); j++) {
				assertEquals(Esperada.getVal(i, j), mReal.getVal(i, j));
			}
		}

		int[][] iDisparo2 = { { 0, 0, 0, 1 } };
		Matriz Disparo2 = new Matriz(iDisparo2);
		oRed.ejecutar(Disparo2, true);
		
		oRed.getSensibilizadas();
	}

}
