package ProcesadorPetri;

public class Semaforo {
	int contador = 0;

	Semaforo(int v) {
		contador = v;
	}

	synchronized public void WAIT() {
		while (contador == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		contador--;
	}

	synchronized public void SIGNAL() {
		contador = 1;
		notify();
	}

	synchronized public int getEstado() {
		return contador;
	}

}