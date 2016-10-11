package ProcesadorPetri;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Monitor {
	RdP rdp;
	Semaphore mutex;

	public Monitor(RdP rdp) {
		this.rdp = rdp;
		mutex = new Semaphore(1);
	}

	public void ejecutar(int transicion) throws InterruptedException {
		mutex.acquire();

//		while (!rdp.ejecutar(transicion)) {
//			mutex.release();
//			mutex.wait();
//		}

		rdp.getSensibilizadas();

		mutex.release();
	}

}
