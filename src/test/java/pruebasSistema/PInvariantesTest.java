package pruebasSistema;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import auxiliar.Lector;
import auxiliar.Matriz;

public class PInvariantesTest {
	private static Lector miLector = new Lector("redCompleta.html", "completaConTiempos.xls");
	static Matriz test;

	@Before
	public void setUp() throws Exception {
		test = miLector.leerLog("Marcado.txt");
	}

	@Test
	// M(P0) = 1
	public void primerInvariante() {
		for (int i = 0; i < test.getFilCount(); i++) {
			Assert.assertEquals(test.getVal(i, 0), 1);
		}
	}

	@Test
	// M(P11) + M(P13) + M(P9) = 1
	public void segundoInvariante() {
		for (int i = 0; i < test.getFilCount(); i++) {
			Assert.assertEquals(test.getVal(i, 11) + test.getVal(i, 13) + test.getVal(i, 9), 1);
		}
	}

	@Test
	// M(P10) + M(P14) + M(P8) = 1
	public void tercerInvariante() {
		for (int i = 0; i < test.getFilCount(); i++) {
			Assert.assertEquals(test.getVal(i, 10) + test.getVal(i, 14) + test.getVal(i, 8), 1);
		}
	}

	@Test
	// M(P15) = 1
	public void cuartoInvariante() {
		for (int i = 0; i < test.getFilCount(); i++) {
			Assert.assertEquals(test.getVal(i, 15), 1);
		}
	}

	@Test
	// M(P17) + M(P18) + M(P19) + M(P21) + M(P24) = 1
	public void quintoInvariante() {
		for (int i = 0; i < test.getFilCount(); i++) {
			Assert.assertEquals(test.getVal(i, 17) + test.getVal(i, 18) + test.getVal(i, 19) + test.getVal(i, 21)
					+ test.getVal(i, 24), 1);
		}
	}

	@Test
	// M(P10) + M(P11) + M(P12) + M(P7) + M(P8) + M(P9) = 2
	public void sextoInvariante() {
		for (int i = 0; i < test.getFilCount(); i++) {
			Assert.assertEquals(test.getVal(i, 10) + test.getVal(i, 11) + test.getVal(i, 12) + test.getVal(i, 7)
					+ test.getVal(i, 8) + test.getVal(i, 9), 2);
		}
	}
}
