package MinaCarbon;

import ProcesadorPetri.Monitor;

public class Hilo implements Runnable {

	private Monitor oMonitor;
	int id = 0;

	public Hilo(Monitor oMonitor, int id) {
		this.oMonitor = oMonitor;
		this.id = id;
	}

	@Override
	public void run() {
		try {

			while (true) {
				
				if (id == 1) {
					System.out.println("SOY EL HILO: " + id);
					oMonitor.ejecutar(5);
					System.out.println("SOY EL HILO: " + id);
					oMonitor.ejecutar(0);
					System.out.println("SOY EL HILO: " + id);
					oMonitor.ejecutar(1);
					System.out.println("SOY EL HILO: " + id);
					oMonitor.ejecutar(2);
					System.out.println("SOY EL HILO: " + id);
					oMonitor.ejecutar(3);
					System.out.println("SOY EL HILO: " + id);
					oMonitor.ejecutar(4);
					Thread.sleep(5000);
				} else {
					System.out.println("SOY EL HILO: " + id);
					oMonitor.ejecutar(2);
					System.out.println("SOY EL HILO: " + id);
					oMonitor.ejecutar(3);
					System.out.println("SOY EL HILO: " + id);
					oMonitor.ejecutar(4);
					System.out.println("SOY EL HILO: " + id);
					oMonitor.ejecutar(5);
					System.out.println("SOY EL HILO: " + id);
					oMonitor.ejecutar(0);
					System.out.println("SOY EL HILO: " + id);
					oMonitor.ejecutar(1);
					Thread.sleep(5000);
				}
				
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Me pusieron a correr");

	}

}
