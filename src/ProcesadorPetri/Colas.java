package ProcesadorPetri;

import java.util.concurrent.Semaphore;

import Auxiliar.Matriz;

public class Colas {

	private Semaphore[] arregloSemaphores;

	public Colas(int tamaño) {
		arregloSemaphores = new Semaphore[tamaño];

		for (int i = 0; i < tamaño; i++) {
			arregloSemaphores[i] = new Semaphore(0, true);
		}
	}

	public boolean desencolar(int i) throws InterruptedException {
		if (arregloSemaphores[i] != null) {
			arregloSemaphores[i].release();
			return true;
		}
		return false;
	}

	public Matriz quienesEstan() {
		Matriz vc = new Matriz(1, arregloSemaphores.length);
		vc.Clear();

		for (int i = 0; i < arregloSemaphores.length; i++) {
			if (arregloSemaphores[i].getQueueLength() != 0) {
				vc.setDato(0, i, 1);
			}
		}

		return vc;
	}

	public void encolar(int transicion) throws InterruptedException {
		if (arregloSemaphores[transicion] != null) {
			arregloSemaphores[transicion].acquire();
		}
	}

}
