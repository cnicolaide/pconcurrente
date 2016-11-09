package pruebasUnitarias;

import java.util.HashMap;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;

import auxiliar.Lector;
import auxiliar.Matriz;
import junit.framework.TestCase;
import procesadorPetri.Colas;
import procesadorPetri.GestorDeMonitor;
import procesadorPetri.RdP;

public class MonitorTest extends TestCase {
	private GestorDeMonitor sone = null, stwo = null;
	private static Logger logger = Logger.getAnonymousLogger();
	private Lector miLector = new Lector("carros.html", "chicaSinTiempos.xls");
	private HashMap<String, Matriz> datos = miLector.read();
	private RdP oRed;
	private Colas miCola;

	@Before
	public void setUp() {
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
