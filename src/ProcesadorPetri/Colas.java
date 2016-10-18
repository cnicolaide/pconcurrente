package ProcesadorPetri;

import Auxiliar.Matriz;

public class Colas {
	Semaforo[] arreglosemaforos;

	public Colas(int tamaño) {
		arreglosemaforos = new Semaforo[tamaño];

		for (int i = 0; i < tamaño; i++) {
			arreglosemaforos[i] = new Semaforo(1);
		}
	}

	public boolean desencolar(int i) throws InterruptedException {
		if (arreglosemaforos[i] != null) {
			arreglosemaforos[i].SIGNAL();
			return true;
		}
		return false;
	}

	public Matriz quienesEstan() {
		Matriz vc = new Matriz(1, arreglosemaforos.length);
		vc.Clear();

		for (int i = 0; i < arreglosemaforos.length; i++) {
			if (arreglosemaforos[i].getContador() == 0) {
				vc.setDato(0, i, 1);
			}
		}

		return vc;
	}

	public void encolar(int transicion) throws InterruptedException {
		if (arreglosemaforos[transicion] != null) {
			arreglosemaforos[transicion].WAIT();
		}
	}

}
