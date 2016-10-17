package ProcesadorPetri;

import java.util.concurrent.Semaphore;

public class Colas {
	Semaphore[] arreglosemaforos;

	public Colas(int tamaño) {
		arreglosemaforos = new Semaphore[tamaño];

		for (int i = 0; i < tamaño; i++) {
			arreglosemaforos[i] = new Semaphore(1);
		}
	}

	public boolean desencolar(int i) throws InterruptedException {
		if (arreglosemaforos[i] != null) {
			arreglosemaforos[i].release();
			return true;
		}
		return false;
	}

	public int[] getColas() {
		int[] arreglo = new int[arreglosemaforos.length];

		for (int i = 0; i < arreglosemaforos.length; i++) {
			if (arreglosemaforos[i] == null) {
				arreglo[i] = 1;
			} else if (arreglosemaforos[i].availablePermits() == 0) {
				arreglo[i] = 1;
			}
		}

		return arreglo;
	}

	public void encolar(int transicion) throws InterruptedException {
		if (arreglosemaforos[transicion] != null) {
			arreglosemaforos[transicion].acquire();
		}
	}

}
