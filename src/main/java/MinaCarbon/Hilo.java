package MinaCarbon;

import java.util.Random;

import ProcesadorPetri.GestorDeMonitor;

public class Hilo implements Runnable {

	private GestorDeMonitor oMonitor;
	private int secuencia[]; // ya que los hilos tienen disparos secuenciales
	private String nombre;
	private Random r = new Random();

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
					Thread.sleep(r.nextInt(100)); // sleep chico
																// random
					System.out.println("\nSOY EL HILO: " + nombre); // Seguimiento
					oMonitor.dispararTransicion(secuencia[i]); // dispara
																// transicion de
																// la secuencia
					Thread.sleep(r.nextInt(5000)); // sleep grande
																// random
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
