package ProcesadorPetri;

import java.util.concurrent.Semaphore;

public class Monitor {
	RdP oRed;
	Colas oCola;
	Semaphore mutex, semaforo;

	public Monitor(RdP oRed, Colas oCola) {
		this.oRed = oRed;
		this.oCola = oCola;
		mutex = new Semaphore(1);
		semaforo = new Semaphore(0);
	}

	public void ejecutar(int transicion) throws InterruptedException {
		mutex.acquire();

		while (!oRed.ejecutar(transicion, true)) {
			mutex.release();
			semaforo.acquire();
			// oCola.encolar(transicion);
			mutex.acquire();
		}

		mutex.release();
		semaforo.release();
		
		// int i = 0;
		//
		// while (!oCola.desencolar(i) && i < oCola.getColas().length) {
		// i++;
		// }

	}

}
