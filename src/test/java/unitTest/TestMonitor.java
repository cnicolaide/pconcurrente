package unitTest;

import java.util.HashMap;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;

import Auxiliar.Lector;
import Auxiliar.Matriz;
import ProcesadorPetri.Colas;
import ProcesadorPetri.GestorDeMonitor;
import ProcesadorPetri.RdP;
import junit.framework.TestCase;

public class TestMonitor extends TestCase {
	private GestorDeMonitor sone = null, stwo = null;
	private static Logger logger = Logger.getAnonymousLogger();
	private Lector miLector;
	private HashMap<String, Matriz> datos;
	private RdP oRed;
	private Colas miCola;

	@Before
	public void setUp() {
		miLector = new Lector("carros.html", "tiempos.xls");
		datos = miLector.read();
		oRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"), datos.get("tiempos"));
		miCola = new Colas(6);
		logger.info("tomando singleton...");
		sone = GestorDeMonitor.getInstance(oRed, miCola);
		logger.info("...tiene singleton: " + sone);
		logger.info("tomando singleton...");
		stwo = GestorDeMonitor.getInstance(oRed, miCola);
		logger.info("...tiene singleton: " + stwo);
	}

	public void testUnicaInstancia() {
		logger.info("Verificando que las dos instancias sean la misma");
		Assert.assertEquals(true, sone == stwo);
	}
}
