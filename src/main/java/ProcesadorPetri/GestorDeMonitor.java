package ProcesadorPetri;

import java.util.concurrent.Semaphore;

import Auxiliar.Matriz;

public class GestorDeMonitor {
	private RdP oRed;
	private Colas oCola;
	private Politicas oPolitica;
	private Semaphore mutex;
	private static GestorDeMonitor instance = null;

	// CONSTRUCTOR DE LA CLASE MONITOR
	protected GestorDeMonitor(RdP oRed, Colas oCola) {
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

	public void dispararTransicion(int transicion) throws InterruptedException {
		mutex.acquire();

		while (!oRed.disparar(transicion)) {
			mutex.release();
			oCola.encolar(transicion);
			mutex.acquire();
		}

		Matriz vs = oRed.getSesibilizadas();
		Matriz vc = oCola.quienesEstan();

		Matriz m = vs.AND(vc);

		// System.err.println("VS: ------------------------------ \n" +
		// vs.toString());
		// System.err.println("VC: ------------------------------ \n" +
		// vc.toString());
		// System.err.println("M: ------------------------------ \n" + m);

		mutex.release();

		// System.err.println("La prioridad es de T" + oPolitica.cual(m));

		if (!m.esCero()) {
			oCola.desencolar(oPolitica.cual(m));
		}

	}
}
