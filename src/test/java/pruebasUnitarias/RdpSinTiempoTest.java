package pruebasUnitarias;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import auxiliar.Lector;
import auxiliar.Log;
import auxiliar.Matriz;
import procesadorPetri.RdP;

public class RdpSinTiempoTest {

	private Lector miLector = new Lector("carros.html", "chicaSinTiempos.xls");
	private HashMap<String, Matriz> datos = miLector.leerRed();
	private RdP oRed;

	@Before
	public void setUp() {
		// Lee las matrices de marcado, inicidencia e inhibicion desde el
		// archivo HTML exportado en PIPE
		System.out.println("\n");
		oRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"), datos.get("tiempos"));
	}

	// PRUEBA DEL METODO GETSENSIBILIZADAS
	@Test
	public void testTransicionesSensibilizadas() {

		Log.getInstance().escribirEimprimir("TestProcesador", " ***** EJECUTANDO TEST: GETSENSIBILIZADAS *****", true);
		int[][] iEsperada = { { 0, 0, 1, 0, 0, 1 } };
		Matriz mEsperada = oRed.getSesibilizadas();
		Matriz mReal = new Matriz(iEsperada);
		comparar(mEsperada, mReal);
	}

	// PRUEBA DISPARO DE TRANSICION NO SENSIBILIZADA
	@Test
	public void testDispararNoSensibilizada() {

		Log.getInstance().escribirEimprimir("TestProcesador",
				" ***** EJECUTANDO TEST: DISPARAR TRANSICION NO SENSIBILIZADA *****", true);
		int[][] iEsperada = { { 0, 0, 1, 0, 0, 1, 0, 0 } };
		Matriz mEsperada = new Matriz(iEsperada);

		oRed.disparar(3);

		Matriz mReal = oRed.getMarcadoActual();
		comparar(mEsperada, mReal);
	}

	// PRUEBA DISPARO DE TRANSICION SENSIBILIZADA
	@Test
	public void testDispararSensibilizada() {

		Log.getInstance().escribirEimprimir("TestProcesador",
				" ***** EJECUTANDO TEST: DISPARAR TRANSICION SENSIBILIZADA *****", true);
		int[][] iEsperada = { { 0, 0, 0, 1, 0, 1, 1, 0 } };
		Matriz mEsperada = new Matriz(iEsperada);

		oRed.disparar(2);

		Matriz mReal = oRed.getMarcadoActual();
		comparar(mEsperada, mReal);
	}

	// PRUEBA DE SECUENCIA CORRECTA DE DISPAROS
	@Test
	public void testDispararSecuenciaCorrecta() {

		Log.getInstance().escribirEimprimir("TestProcesador",
				" ***** EJECUTANDO TEST: DISPARAR SECUENCIA CORRECTA DE TRANSICIONES *****", true);
		int[][] iEsperada = { { 0, 1, 0, 1, 0, 0, 0, 1 } };
		Matriz mEsperada = new Matriz(iEsperada);

		oRed.disparar(2);
		oRed.disparar(5);
		oRed.disparar(0);

		Matriz mReal = oRed.getMarcadoActual();
		comparar(mEsperada, mReal);
	}

	// PRUEBA DE SECUENCIA INCORRECTA DE DISPAROS
	@Test
	public void testDispararSecuenciaIncorrecta() {

		Log.getInstance().escribirEimprimir("TestProcesador",
				" ***** EJECUTANDO TEST: DISPARAR SECUENCIA INCORRECTA DE TRANSICIONES *****", true);
		int[][] iEsperada = { { 1, 0, 1, 0, 0, 0, 0, 1 } };
		Matriz mEsperada = new Matriz(iEsperada);
		oRed.disparar(5);
		Matriz mReal = oRed.getMarcadoActual();
		oRed.disparar(0);
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
