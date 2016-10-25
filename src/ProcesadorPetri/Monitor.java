package ProcesadorPetri;

import java.util.concurrent.Semaphore;

import Auxiliar.Matriz;

public class Monitor {
	private RdP oRed;
	private Colas oCola;
	private Semaphore mutex, semaforo;
	private static Monitor instance = null;

	// CONSTRUCTOR DE LA CLASE MONITOR
	protected Monitor(RdP oRed, Colas oCola) {
		this.oRed = oRed;
		this.oCola = oCola;
		mutex = new Semaphore(1, true);
		semaforo = new Semaphore(0, true);
	}

	// METODO PARA IMPLEMENTAR PATRON SINGLETON
	public static Monitor getInstance(RdP oRed, Colas oCola) {
		if (instance == null) {
			instance = new Monitor(oRed, oCola);
		}
		return instance;
	}

	public void ejecutar(int transicion) throws InterruptedException {
		mutex.acquire();

		while (!oRed.ejecutar(transicion)) {
			mutex.release();
			semaforo.acquire();
			mutex.acquire();

		}

		Matriz vs = oRed.getSesibilizadas();
		Matriz vc = oCola.quienesEstan();

		@SuppressWarnings("unused")
		Matriz m = vs.AND(vc);

		// System.err.println("VS: ------------------------------ \n" +
		// vs.toString());
		// System.err.println("VC: ------------------------------ \n" +
		// vc.toString());
		// System.err.println("M: ------------------------------ \n" +
		// m.toString());

		mutex.release();

		System.out.println(semaforo.availablePermits());
		if (semaforo.availablePermits() > 0) {
			semaforo.release();
		}
	}

}
