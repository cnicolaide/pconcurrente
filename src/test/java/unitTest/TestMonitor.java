package unitTest;

import java.util.HashMap;
import java.util.logging.Logger;

import org.junit.Assert;

import Auxiliar.Lector;
import Auxiliar.Matriz;
import ProcesadorPetri.Colas;
import ProcesadorPetri.GestorDeMonitor;
import ProcesadorPetri.RdP;
import junit.framework.TestCase;

public class TestMonitor extends TestCase {
	private GestorDeMonitor sone = null, stwo = null;
	private static Logger logger = Logger.getAnonymousLogger();

	// Lee las matrices de marcado, inicidencia e inhibicion desde el
	// archivo
	// HTML exportado en PIPE
	Lector miLector = new Lector("carros.html", "tiempos.xls");
	HashMap<String, Matriz> datos = miLector.read();
	RdP miRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"), datos.get("tiempos"));
	// Crea la cola y el monitor
	private Colas miCola = new Colas(6);

	public void setUp() {
		logger.info("tomando singleton...");
		sone = GestorDeMonitor.getInstance(miRed, miCola);
		logger.info("...tiene singleton: " + sone);
		logger.info("tomando singleton...");
		stwo = GestorDeMonitor.getInstance(miRed, miCola);
		logger.info("...tiene singleton: " + stwo);
	}

	public void testUnicaInstancia() {
		logger.info("Verificando que las dos instancias sean la misma");
		Assert.assertEquals(true, sone == stwo);
	}
}
