package ProcesadorPetri;

import MinaCarbon.Hilo;

public class Monitor {

	private Mutex Mutex;
	private RdP RDP;

	public Monitor() {
		Mutex = new Mutex();
		// RDP = new Red ();

	}

	public void hacerAlgo() throws InterruptedException {
		if (Mutex.acquire() == 0)
			hacerAlgo2();
		else
			dormir();
	}

	synchronized public void dormir() throws InterruptedException {
		System.out.println("Me mandaron a dormir 10 seg");
		wait();
	}

	public void hacerAlgo2() {
		Mutex.setKey();
		System.out.println("Estoy haciendo algo");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Mutex.release();
		System.out.println("Libere la exclusion");
	}

}
