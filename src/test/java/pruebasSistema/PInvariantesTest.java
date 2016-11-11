package pruebasSistema;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import auxiliar.Lector;
import auxiliar.Matriz;
import minaCarbon.SistemaCompleto;

public class PInvariantesTest {
	private static Lector oLector = new Lector();
	static Matriz test;

	@BeforeClass
	public static void test() throws Exception {
		String[] args = {};
		SistemaCompleto.main(args);
		Thread.sleep(5000);
		test = oLector.leerLog("Marcado.txt");
		Thread.sleep(1000);
	}

	// M(P0) = 1
	@Test
	public void primerInvariante() {
		for (int i = 0; i < test.getFilCount(); i++) {
			Assert.assertEquals(test.getVal(i, 0), 1);
		}
	}

	// M(P11) + M(P13) + M(P9) = 1
	@Test
	public void segundoInvariante() {
		for (int i = 0; i < test.getFilCount(); i++) {
			Assert.assertEquals(test.getVal(i, 11) + test.getVal(i, 13) + test.getVal(i, 9), 1);
		}
	}

	// M(P10) + M(P14) + M(P8) = 1
	@Test
	public void tercerInvariante() {
		for (int i = 0; i < test.getFilCount(); i++) {
			Assert.assertEquals(test.getVal(i, 10) + test.getVal(i, 14) + test.getVal(i, 8), 1);
		}
	}

	// M(P15) = 1
	@Test
	public void cuartoInvariante() {
		for (int i = 0; i < test.getFilCount(); i++) {
			Assert.assertEquals(test.getVal(i, 15), 1);
		}
	}

	// M(P17) + M(P18) + M(P19) + M(P21) + M(P24) = 1
	@Test
	public void quintoInvariante() {
		for (int i = 0; i < test.getFilCount(); i++) {
			Assert.assertEquals(test.getVal(i, 17) + test.getVal(i, 18) + test.getVal(i, 19) + test.getVal(i, 21)
					+ test.getVal(i, 24), 1);
		}
	}

	// M(P10) + M(P11) + M(P12) + M(P7) + M(P8) + M(P9) = 2
	@Test
	public void sextoInvariante() {
		for (int i = 0; i < test.getFilCount(); i++) {
			Assert.assertEquals(test.getVal(i, 10) + test.getVal(i, 11) + test.getVal(i, 12) + test.getVal(i, 7)
					+ test.getVal(i, 8) + test.getVal(i, 9), 2);
		}
	}
}
