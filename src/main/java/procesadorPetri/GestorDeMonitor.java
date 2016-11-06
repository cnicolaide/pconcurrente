package procesadorPetri;

import java.util.concurrent.Semaphore;

import auxiliar.Matriz;

public class GestorDeMonitor {
	private RdP oRed;
	private Colas oCola;
	private Politicas oPolitica;
	private Semaphore mutex;
	private static GestorDeMonitor instance = null;

	// CONSTRUCTOR DE LA CLASE MONITOR
	private GestorDeMonitor(RdP oRed, Colas oCola) {
		this.oRed = oRed;
		this.oCola = oCola;
		oPolitica = new Politicas(oRed);
		mutex = new Semaphore(1, true);
	}

	// METODO PARA IMPLEMENTAR PATRON SINGLETON
	public static synchronized GestorDeMonitor getInstance(RdP oRed, Colas oCola) {
		if (instance == null) {
			instance = new GestorDeMonitor(oRed, oCola);
		}
		return instance;
	}

	// METODO QUE DISPARA TRANSICIONES TEMPORIZADAS Y AUTOMATICAS
	public void dispararTransicion(int transicion) throws InterruptedException {
		mutex.acquire();

		while (!oRed.disparar(transicion)) {
			mutex.release();
			// Verifica si es una transicion con tiempo
			if (oRed.getVentana() > 0) {
				Thread.sleep(oRed.getVentana());
				mutex.acquire();
				// En caso contrario
			} else {
				oCola.encolar(transicion);
				mutex.acquire();
			}
		}

		Matriz vs = oRed.getSesibilizadas();
		Matriz vc = oCola.quienesEstan();
		Matriz m = vs.AND(vc);

		mutex.release();

		if (!m.esCero()) {
			oCola.desencolar(oPolitica.cual(m));
		}
	}
}
