package minaCarbon;

import java.util.Random;

import procesadorPetri.GestorDeMonitor;

public class Hilo implements Runnable {

	private GestorDeMonitor oMonitor;
	private int secuencia[];
	private String nombre;
	private Thread hilo;
	private Random r = new Random();

	public Hilo(GestorDeMonitor oMonitor, String nombre, int secuencia[]) {
		this.oMonitor = oMonitor;
		this.nombre = nombre;
		this.secuencia = secuencia;
		hilo = new Thread(this, nombre);
		hilo.start();
	}

	@Override
	public void run() {
		try {
			while (true) {
				for (int i = 0; i < secuencia.length; i++) {
					Thread.sleep(r.nextInt(10));
					System.out.println("\nSOY EL HILO: " + nombre);
					oMonitor.dispararTransicion(secuencia[i]);
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
