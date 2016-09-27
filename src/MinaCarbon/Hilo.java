package MinaCarbon;

import Auxiliar.Matriz;
import ProcesadorPetri.Monitor;

public class Hilo implements Runnable {

	private Monitor miMonitor;
	
	public Hilo(Monitor miMonitor) {
		this.miMonitor = miMonitor;
	}

	public void Disparar() {
		try {
			miMonitor.hacerAlgo();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("Me pusieron a correr");
		Disparar();

	}

}
