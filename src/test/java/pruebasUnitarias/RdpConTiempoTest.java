package pruebasUnitarias;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import auxiliar.Lector;
import auxiliar.Log;
import auxiliar.Matriz;
import procesadorPetri.RdP;

public class RdpConTiempoTest {
	private Lector miLector = new Lector("carros.html", "chicaConTiempos.xls");
	private HashMap<String, Matriz> datos = miLector.read();
	private RdP oRed;

	@Before
	public void setUp() {
		// Lee las matrices de marcado, inicidencia e inhibicion desde el
		// archivo HTML exportado en PIPE
		System.out.println("\n");
		oRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"), datos.get("tiempos"));
	}

	// PRUEBA DISPARO DE TRANSICION SENSIBILIZADA
	@Test
	public void testDispararSensibilizadaAntes() {

		Log.getInstance().escribir("TestProcesador",
				" ***** EJECUTANDO TEST: DISPARAR TRANSICION SENSIBILIZADA ANTES *****");
		int[][] iEsperada = { { 0, 0, 1, 0, 0, 1, 0, 0 } };
		Matriz mEsperada = new Matriz(iEsperada);

		oRed.disparar(2);

		Matriz mReal = oRed.getMarcadoActual();
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
