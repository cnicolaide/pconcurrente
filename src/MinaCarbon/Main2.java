package MinaCarbon;

import java.util.HashMap;

import Auxiliar.Lector;
import Auxiliar.Matriz;
import ProcesadorPetri.Colas;
import ProcesadorPetri.GestorDeMonitor;
import ProcesadorPetri.RdP;

public class Main2 {
	public static void main(String[] args) {
		// Lee las matrices de marcado, inicidencia e inhibicion desde el
		// archivo
		// HTML exportado en PIPE
		Lector miLector = new Lector("bombaYsensor.html");
		HashMap<String, Matriz> datos = miLector.LeerHTML();
		RdP miRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"));

		// Crea la cola y el monitor
		Colas miCola = new Colas(6);
		GestorDeMonitor miMonitor = GestorDeMonitor.getInstance(miRed, miCola);

		int secuenciaA[] = { 0 }; // CLOCK
		int secuenciaB[] = { 3 }; // RESTRICCIONES
		int secuenciaC[] = { 1, 4 }; // TANQUE VACIO
		int secuenciaD[] = { 2, 5 }; // TANQUE LLENO

		// Crea los dos carros, les pasa el monitor, un id, y la secuencia de
		// disparo de cada uno
		Bomba A = new Bomba(miMonitor, 0, secuenciaA);
		Bomba B = new Bomba(miMonitor, 1, secuenciaB);
		Bomba C = new Bomba(miMonitor, 2, secuenciaC);
		Bomba D = new Bomba(miMonitor, 3, secuenciaD);

		// Crea los hilos para los carros
		Thread hiloA = new Thread(A);
		Thread hiloB = new Thread(B);
		Thread hiloC = new Thread(C);
		Thread hiloD = new Thread(D);

		hiloA.start();
		hiloB.start();
		hiloC.start();
		hiloD.start();
	}

}
