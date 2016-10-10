package Test;

import static org.junit.Assert.*;

import java.util.HashMap;
import org.junit.Test;
import Auxiliar.Lector;
import Auxiliar.Matriz;
import ProcesadorPetri.RdP;

public class TestProcesador {

	Lector miLector = new Lector("carros.html");
	HashMap<String, Matriz> datos = miLector.LeerHTML();
	RdP oRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"));

	@Test
	public void testDispararNOSensibilizada() {

		int[][] iDisparo = { { 0, 0, 0, 1, 0, 0 } };
		int[][] iEsperada = { { 0, 0, 1, 0, 0, 1, 0, 0 } };

		Matriz mDisparo = new Matriz(iDisparo);
		Matriz mEsperada = new Matriz(iEsperada);

		oRed.ejecutar(mDisparo, true);

		Matriz mReal = oRed.getMarcadoInicial();

		comparar(mEsperada, mReal);

	}

	@Test
	public void testDispararSensibilizada() {

		int[][] iDisparo = { { 0, 0, 1, 0, 0, 0 } };
		int[][] iEsperada = { { 0, 0, 0, 1, 0, 1, 1, 0 } };

		Matriz mDisparo = new Matriz(iDisparo);
		Matriz mEsperada = new Matriz(iEsperada);

		oRed.ejecutar(mDisparo, true);

		Matriz mReal = oRed.getNuevoMarcado();

		comparar(mEsperada, mReal);
	}

	@Test
	public void testDispararSecuenciaCorrecta() {

		int[][] iDisparo = { { 0, 0, 1, 0, 0, 0 } };
		int[][] iDisparo2 = { { 0, 0, 0, 0, 0, 1 } };
		int[][] iDisparo3 = { { 1, 0, 0, 0, 0, 0 } };
		int[][] iEsperada = { { 0, 1, 0, 1, 0, 0, 0, 1 } };

		Matriz mDisparo = new Matriz(iDisparo);
		Matriz mDisparo2 = new Matriz(iDisparo2);
		Matriz mDisparo3 = new Matriz(iDisparo3);

		Matriz mEsperada = new Matriz(iEsperada);

		oRed.ejecutar(mDisparo, true);
		oRed.ejecutar(mDisparo2, true);
		oRed.ejecutar(mDisparo3, true);

		Matriz mReal = oRed.getNuevoMarcado();

		comparar(mEsperada, mReal);

	}

	@Test
	public void testDispararSecuenciaIncorrecta() {

		int[][] iDisparo = { { 0, 0, 0, 0, 0, 1 } };
		int[][] iDisparo2 = { { 1, 0, 0, 0, 0, 0 } };
		int[][] iEsperada = { { 1, 0, 1, 0, 0, 0, 0, 1 } };

		Matriz mDisparo = new Matriz(iDisparo);
		Matriz mDisparo2 = new Matriz(iDisparo2);

		Matriz mEsperada = new Matriz(iEsperada);

		oRed.ejecutar(mDisparo, true);

		Matriz mReal = oRed.getNuevoMarcado();

		oRed.ejecutar(mDisparo2, true);

		comparar(mEsperada, mReal);

	}

	@Test
	public void testTransicionesSensibilizadas() {

		int[][] iEsperada = { { 0, 0, 1, 0, 0, 1 } };

		Matriz mEsperada = oRed.getSensibilizadas();

		Matriz mReal = new Matriz(iEsperada);

		comparar(mEsperada, mReal);

	}

	private void comparar(Matriz mEsperada, Matriz mReal) {
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < mReal.getColCount(); j++) {
				assertEquals(mEsperada.getVal(i, j), mReal.getVal(i, j));
			}
		}
	}
}
