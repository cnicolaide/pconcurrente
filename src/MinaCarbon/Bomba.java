package MinaCarbon;

import ProcesadorPetri.GestorDeMonitor;

public class Bomba implements Runnable {

	private GestorDeMonitor oMonitor;
	private int id, secuencia[];

	public Bomba(GestorDeMonitor oMonitor, int id, int secuencia[]) {
		this.oMonitor = oMonitor;
		this.id = id;
		this.secuencia = secuencia;
	}

	@Override
	public void run() {
		try {
			while (true) {
				for (int i = 0; i < secuencia.length; i++) {
					Thread.sleep(100 * (id + 1));
					System.out.println("\nSOY EL HILO: " + id); //
					// Seguimiento
					oMonitor.dispararTransicion(secuencia[i]);
					Thread.sleep(1000 * (id + 1));
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
