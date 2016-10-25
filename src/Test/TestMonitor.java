package Test;

import java.util.HashMap;
import java.util.logging.Logger;

import org.junit.Assert;

import Auxiliar.Lector;
import Auxiliar.Matriz;
import ProcesadorPetri.Colas;
import ProcesadorPetri.Monitor;
import ProcesadorPetri.RdP;
import junit.framework.TestCase;

public class TestMonitor extends TestCase {
	private Monitor sone = null, stwo = null;
	private static Logger logger = Logger.getAnonymousLogger();

	// Lee las matrices de marcado, inicidencia e inhibicion desde el
	// archivo
	// HTML exportado en PIPE
	private Lector miLector = new Lector("carros.html");
	private HashMap<String, Matriz> datos = miLector.LeerHTML();
	private RdP miRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"));

	// Crea la cola y el monitor
	private Colas miCola = new Colas(6);

	public void setUp() {
		logger.info("tomando singleton...");
		sone = Monitor.getInstance(miRed, miCola);
		logger.info("...tiene singleton: " + sone);
		logger.info("tomando singleton...");
		stwo = Monitor.getInstance(miRed, miCola);
		logger.info("...tiene singleton: " + stwo);
	}

	public void testUnicaInstancia() {
		logger.info("Verificando que las dos instancias sean la misma");
		Assert.assertEquals(true, sone == stwo);
	}
}
