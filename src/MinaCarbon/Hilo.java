package MinaCarbon;

import ProcesadorPetri.GestorDeMonitor;

public class Hilo implements Runnable {

	private GestorDeMonitor oMonitor;
	private int secuencia[];
	private String nombre;

	public Hilo(GestorDeMonitor oMonitor, String nombre, int secuencia[]) {
		this.oMonitor = oMonitor;
		this.nombre = nombre;
		this.secuencia = secuencia;
	}

	@Override
	public void run() {
		try {
			while (true) {
				for (int i = 0; i < secuencia.length; i++) {
					Thread.sleep((int) (Math.random() * 100));
					System.out.println("\nSOY EL HILO: " + nombre); //
					// Seguimiento
					oMonitor.dispararTransicion(secuencia[i]);
					Thread.sleep((int) (Math.random() * 1000));
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
