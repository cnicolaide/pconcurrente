package ProcesadorPetri;

import Auxiliar.Matriz;

public class Monitor {
	private RdP oRed;
	private Colas oCola;
	private Semaforo mutex, semaforo;
	private static Monitor instance = null;
 
	// CONSTRUCTOR DE LA CLASE MONITOR
	protected Monitor(RdP oRed, Colas oCola) {
		this.oRed = oRed;
		this.oCola = oCola;
		mutex = new Semaforo(1);
		semaforo = new Semaforo(0);
	}

	// METODO PARA IMPLEMENTAR PATRON SINGLETON
	public static Monitor getInstance(RdP oRed, Colas oCola) {
		if (instance == null) {
			instance = new Monitor(oRed, oCola);
		}
		return instance;
	}

	public void ejecutar(int transicion) throws InterruptedException {
		mutex.WAIT();

		while (!oRed.ejecutar(transicion)) {
			mutex.SIGNAL();
			semaforo.WAIT();
			mutex.WAIT();

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

		mutex.SIGNAL();

		if (semaforo.getBloqueados() > 0) {
			semaforo.SIGNAL();
		}
	}

}
