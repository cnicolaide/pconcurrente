package ProcesadorPetri;

public class Semaforo {
	private int contador = 0, bloqueados = 0;

	Semaforo(int v) {
		contador = v;
	}

	synchronized public void WAIT() {
		while (contador == 0) {
			try {
				bloqueados = 1;
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		contador = 0;
		bloqueados = 0;
	}

	synchronized public void SIGNAL() {
		contador = 1;
		notify();
	}

	synchronized public int getContador() {
		return contador;
	}

	synchronized public int getBloqueados() {
		return bloqueados;
	}

}