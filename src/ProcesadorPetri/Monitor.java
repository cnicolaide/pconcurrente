package ProcesadorPetri;

import Auxiliar.Matriz;

public class Monitor {
	private RdP oRed;
	private Colas oCola;
	private Semaforo mutex, semaforo;

	public Monitor(RdP oRed, Colas oCola) {
		this.oRed = oRed;
		this.oCola = oCola;
		mutex = new Semaforo(1);
		semaforo = new Semaforo(0);
	}

	public void ejecutar(int transicion) throws InterruptedException {
		mutex.WAIT();

		while (!oRed.ejecutar(transicion, true)) {
			mutex.SIGNAL();
			semaforo.WAIT();
			mutex.WAIT();

		}
		
		Matriz vs = oRed.getSensibilizadas();
		Matriz vc = oCola.quienesEstan();
		
		Matriz m = vs.AND(vc);
		
//		System.err.println("VS: ------------------------------ \n" + vs.toString());
//		System.err.println("VC: ------------------------------ \n" + vc.toString());
//		System.err.println("M: ------------------------------ \n" + m.toString());
		
		mutex.SIGNAL();
		
		if (semaforo.getBloqueados() > 0) {
			semaforo.SIGNAL();
		}
	}

}
