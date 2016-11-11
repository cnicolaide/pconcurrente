package pruebasSistema;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import auxiliar.Lector;
import auxiliar.Matriz;
import minaCarbon.SistemaCompleto;

public class EstadosyDisparosTest {
	private static Lector oLector = new Lector();
	static Matriz marcado, transiciones;

	@BeforeClass
	public static void test() throws Exception {
		String[] args = {};
		SistemaCompleto.main(args);
		Thread.sleep(5000);
		marcado = oLector.leerLog("Marcado.txt");
		transiciones = oLector.leerLog("Transiciones.txt");
		Thread.sleep(1000);
	}

	@Test
	// M(P25) >= 1 => t9=0. No se puede encender la bomba si hay CH4
	public void hayCH4() {
		for (int i = 0; i < marcado.getFilCount(); i++) {
			if (marcado.getVal(i, 25) >= 1)
				Assert.assertEquals(transiciones.getVal(i, 9), 0);
		}
	}

	@Test
	// M(P9) = 1 => t9=0. Dos carros no pueden ocupar la misma Via
	public void via1() {
		for (int i = 0; i < marcado.getFilCount(); i++) {
			if (marcado.getVal(i, 9) == 1)
				Assert.assertEquals(transiciones.getVal(i, 6), 0);
		}
	}

	@Test
	// M(P10) = 1 => t11=0. Dos carros no pueden ocupar la misma Via
	public void via2() {
		for (int i = 0; i < marcado.getFilCount(); i++) {
			if (marcado.getVal(i, 10) == 1)
				Assert.assertEquals(transiciones.getVal(i, 11), 0);
		}
	}

	@Test
	// M(P19) >= 1 => t15! = t16. La medicion de CH4 no puede dar alta y baja
	public void noHayCH4yHayCH4() {
		for (int i = 0; i < marcado.getFilCount(); i++) {
			if (marcado.getVal(i, 19) >= 1)
				Assert.assertNotEquals(transiciones.getVal(i, 15), transiciones.getVal(i, 16));
		}
	}

	@Test
	// M(P1) >= 1 => t2! = t1. La medicion de agua no puede dar alta y baja
	public void tanqueLlenoyVacio() {
		for (int i = 0; i < marcado.getFilCount(); i++) {
			if (marcado.getVal(i, 1) == 1)
				Assert.assertNotEquals(transiciones.getVal(i, 2), transiciones.getVal(i, 1));
		}
	}
}
