package pruebasSistema;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import auxiliar.Lector;
import auxiliar.Matriz;
import procesadorPetri.RdP;

public class TInvariantesTest {
	private static Lector miLector = new Lector("redCompleta.html", "completaSinTiempos.xls");
	private static HashMap<String, Matriz> datos = miLector.leerRed();
	private RdP oRed;
	private Matriz disparos = new Matriz(8, 2);
	private Matriz disparos2 = new Matriz(12, 2);
	private Matriz marcadoIncial;

	@Before
	public void setUp() {
		oRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"), datos.get("tiempos"));

		// primera secuencias de disparos que vuelven al estado incial
		disparos.setDato(0, 0, 12);
		disparos.setDato(0, 1, 11);
		disparos.setDato(1, 0, 13);
		disparos.setDato(1, 1, 11);
		disparos.setDato(2, 0, 14);
		disparos.setDato(2, 1, 11);
		disparos.setDato(3, 0, 15);
		disparos.setDato(3, 1, 1);
		disparos.setDato(4, 0, 16);
		disparos.setDato(4, 1, 10);
		disparos.setDato(5, 0, 17);
		disparos.setDato(5, 1, 1);
		disparos.setDato(6, 0, 18);
		disparos.setDato(6, 1, 10);
		disparos.setDato(7, 0, 21);
		disparos.setDato(7, 1, 1);

		disparos2.setDato(0, 0, 0);
		disparos2.setDato(0, 1, 11);
		disparos2.setDato(1, 0, 1);
		disparos2.setDato(1, 1, 1);
		disparos2.setDato(2, 0, 10);
		disparos2.setDato(2, 1, 1);
		disparos2.setDato(3, 0, 11);
		disparos2.setDato(3, 1, 1);
		disparos2.setDato(4, 0, 2);
		disparos2.setDato(4, 1, 10);
		disparos2.setDato(5, 0, 3);
		disparos2.setDato(5, 1, 10);
		disparos2.setDato(6, 0, 4);
		disparos2.setDato(6, 1, 1);
		disparos2.setDato(7, 0, 8);
		disparos2.setDato(7, 1, 1);
		disparos2.setDato(8, 0, 5);
		disparos2.setDato(8, 1, 10);
		disparos2.setDato(9, 0, 6);
		disparos2.setDato(9, 1, 1);
		disparos2.setDato(10, 0, 7);
		disparos2.setDato(10, 1, 1);
		disparos2.setDato(11, 0, 9);
		disparos2.setDato(11, 1, 1);
	}

	// t12*11 t13*11 t14*11 t15 t16*10 t17 t18*10 t21
	@Test
	public void secuenciaT1() {
		marcadoIncial = datos.get("marcado");
		int cont = 56;// cantidad total de disparos
		while (cont != 0) {

			for (int fil = 0; fil < 8; fil++) {
				if (oRed.getSesibilizadas().getVal(0, disparos.getVal(fil, 0)) == 1 && disparos.getVal(fil, 1) != 0) {
					oRed.disparar(disparos.getVal(fil, 0));
					disparos.setDato(fil, 1, (disparos.getVal(fil, 1) - 1));
					cont--;
				}
			}
		}
		comparar(marcadoIncial, oRed.getMarcadoActual());
	}

	// t0*11 t1 t10 t11 t2*10 t3*10 t4 t5*10 t6 t7 t8 t9
	@Test
	public void secuenciaT2() {
		marcadoIncial = datos.get("marcado");
		int cont = 49;// cantidad total de disparos
		while (cont != 0) {

			for (int fil = 0; fil < 12; fil++) {
				if (oRed.getSesibilizadas().getVal(0, disparos2.getVal(fil, 0)) == 1 && disparos2.getVal(fil, 1) != 0) {
					oRed.disparar(disparos2.getVal(fil, 0));
					disparos2.setDato(fil, 1, (disparos2.getVal(fil, 1) - 1));
					cont--;
				}
			}
		}
		comparar(marcadoIncial, oRed.getMarcadoActual());
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
