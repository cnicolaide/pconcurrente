package MinaCarbon;

import ProcesadorPetri.Monitor;

public class Main {

	public static void main(String[] args) {

		Monitor miMonitor = new Monitor();

		Hilo A = new Hilo(miMonitor);
		Hilo B = new Hilo(miMonitor);
		
		Thread hiloA = new Thread(A);
		Thread hiloB = new Thread(B);

		hiloA.start();
		hiloB.start();

	}

}
