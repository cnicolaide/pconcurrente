package MinaCarbon;

import ProcesadorPetri.Monitor;

public class Carro implements Runnable {

	private Monitor oMonitor;
	int id;
	int secuencias[][] = { { 5, 0, 1, 2, 3, 4 }, { 2, 3, 4, 5, 0, 1 } };

	public Carro(Monitor oMonitor, int id) {
		this.oMonitor = oMonitor;
		this.id = id;
	}

	@Override
	public void run() {
		try {
			while (true) {
				for (int j = 0; j < 6; j++) {
					Thread.sleep(100 * (id+1));
					System.out.println("\nSOY EL CARRO: " + id);
					oMonitor.ejecutar(secuencias[id][j]);
					Thread.sleep(1000 * (id+1));
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
