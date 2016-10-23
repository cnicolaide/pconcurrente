package Test;

import static org.junit.Assert.*;

import java.util.HashMap;
import org.junit.Test;
import Auxiliar.Lector;
import Auxiliar.Matriz;
import ProcesadorPetri.RdP;

public class TestProcesador {

	// Lee las matrices de marcado, inicidencia e inhibicion desde el archivo
	// HTML exportado en PIPE
	private Lector miLector = new Lector("carros.html");
	private HashMap<String, Matriz> datos = miLector.LeerHTML();
	private RdP oRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"));

	// PRUEBA DISPARO DE TRANSICION NO SENSIBILIZADA
	@Test
	public void testDispararNoSensibilizada() {

		int[][] iEsperada = { { 0, 0, 1, 0, 0, 1, 0, 0 } };
		Matriz mEsperada = new Matriz(iEsperada);

		oRed.ejecutar(3, true);

		Matriz mReal = oRed.getMarcadoInicial();
		comparar(mEsperada, mReal);

	}

	// PRUEBA DISPARO DE TRANSICION SENSIBILIZADA
	@Test
	public void testDispararSensibilizada() {

		int[][] iEsperada = { { 0, 0, 0, 1, 0, 1, 1, 0 } };
		Matriz mEsperada = new Matriz(iEsperada);

		oRed.ejecutar(2, true);

		Matriz mReal = oRed.getNuevoMarcado();
		comparar(mEsperada, mReal);
	}

	// PRUEBA DE SECUENCIA CORRECTA DE DISPAROS
	@Test
	public void testDispararSecuenciaCorrecta() {

		int[][] iEsperada = { { 0, 1, 0, 1, 0, 0, 0, 1 } };
		Matriz mEsperada = new Matriz(iEsperada);

		oRed.ejecutar(2, true);
		oRed.ejecutar(5, true);
		oRed.ejecutar(0, true);

		Matriz mReal = oRed.getNuevoMarcado();
		comparar(mEsperada, mReal);

	}

	// PRUEBA DE SECUENCIA INCORRECTA DE DISPAROS
	@Test
	public void testDispararSecuenciaIncorrecta() {

		int[][] iEsperada = { { 1, 0, 1, 0, 0, 0, 0, 1 } };
		Matriz mEsperada = new Matriz(iEsperada);
		oRed.ejecutar(5, true);
		Matriz mReal = oRed.getNuevoMarcado();
		oRed.ejecutar(0, true);
		comparar(mEsperada, mReal);

	}

	// PRUEBA DEL METODO GETSENSIBILIZADAS
	@Test
	public void testTransicionesSensibilizadas() {

		int[][] iEsperada = { { 0, 0, 1, 0, 0, 1 } };
		Matriz mEsperada = oRed.getSensibilizadas();
		Matriz mReal = new Matriz(iEsperada);
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
