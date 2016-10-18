package ProcesadorPetri;

public class Monitor {
	RdP oRed;
	Colas oCola;
	Semaforo mutex, semaforo;

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
			// oCola.encolar(transicion);
			mutex.WAIT();

		}

		mutex.SIGNAL();
		semaforo.SIGNAL();

		// int i = 0;
		//
		// while (!oCola.desencolar(i) && i < oCola.getColas().length) {
		// i++;
		// }

	}

}
