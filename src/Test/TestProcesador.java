package Test;

import static org.junit.Assert.*;

import java.util.HashMap;
import org.junit.Test;
import Auxiliar.Lector;
import Auxiliar.Log;
import Auxiliar.Matriz;
import ProcesadorPetri.RdP;

public class TestProcesador {

	// Lee las matrices de marcado, inicidencia e inhibicion desde el archivo
	// HTML exportado en PIPE
	private Lector miLector = new Lector("carros.html");
	private HashMap<String, Matriz> datos = miLector.LeerHTML();
	private RdP oRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"));

	// PRUEBA DEL METODO GETSENSIBILIZADAS
	@Test
	public void testTransicionesSensibilizadas() {

		Log.Instance().Escribir("TestProcesador", " ***** EJECUTANDO TEST: GETSENSIBILIZADAS *****");
		int[][] iEsperada = { { 0, 0, 1, 0, 0, 1 } };
		Matriz mEsperada = oRed.getSesibilizadas();
		Matriz mReal = new Matriz(iEsperada);
		comparar(mEsperada, mReal);
	}

	// PRUEBA DISPARO DE TRANSICION NO SENSIBILIZADA
	@Test
	public void testDispararNoSensibilizada() {

		Log.Instance().Escribir("TestProcesador", " ***** EJECUTANDO TEST: DISPARAR TRANSICION NO SENSIBILIZADA *****");
		int[][] iEsperada = { { 0, 0, 1, 0, 0, 1, 0, 0 } };
		Matriz mEsperada = new Matriz(iEsperada);
 
		oRed.ejecutar(3);

		Matriz mReal = oRed.getMarcadoActual();
		comparar(mEsperada, mReal);
	}

	// PRUEBA DISPARO DE TRANSICION SENSIBILIZADA
	@Test
	public void testDispararSensibilizada() {

		Log.Instance().Escribir("TestProcesador", " ***** EJECUTANDO TEST: DISPARAR TRANSICION SENSIBILIZADA *****");
		int[][] iEsperada = { { 0, 0, 0, 1, 0, 1, 1, 0 } };
		Matriz mEsperada = new Matriz(iEsperada);

		oRed.ejecutar(2);

		Matriz mReal = oRed.getMarcadoActual();
		comparar(mEsperada, mReal);
	}

	// PRUEBA DE SECUENCIA CORRECTA DE DISPAROS
	@Test
	public void testDispararSecuenciaCorrecta() {

		Log.Instance().Escribir("TestProcesador",
				" ***** EJECUTANDO TEST: DISPARAR SECUENCIA CORRECTA DE TRANSICIONES *****");
		int[][] iEsperada = { { 0, 1, 0, 1, 0, 0, 0, 1 } };
		Matriz mEsperada = new Matriz(iEsperada);

		oRed.ejecutar(2);
		oRed.ejecutar(5);
		oRed.ejecutar(0);

		Matriz mReal = oRed.getMarcadoActual();
		comparar(mEsperada, mReal);
	}

	// PRUEBA DE SECUENCIA INCORRECTA DE DISPAROS
	@Test
	public void testDispararSecuenciaIncorrecta() {

		Log.Instance().Escribir("TestProcesador",
				" ***** EJECUTANDO TEST: DISPARAR SECUENCIA INCORRECTA DE TRANSICIONES *****");
		int[][] iEsperada = { { 1, 0, 1, 0, 0, 0, 0, 1 } };
		Matriz mEsperada = new Matriz(iEsperada);
		oRed.ejecutar(5);
		Matriz mReal = oRed.getMarcadoActual();
		oRed.ejecutar(0);
		comparar(mEsperada, mReal);
	}

	// METODO QUE HACE EL ASSERTEQUALS DE LAS MATRICES
	private void comparar(Matriz mEsperada, Matriz mReal) {
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < mReal.getColCount(); j++) {
				assertEquals(mEsperada.getVal(i, j), mReal.getVal(i, j));
			}
		}
	}
}
