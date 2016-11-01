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
		Lector miLector = new Lector("bombaYSensorycarro.html", "tiempos.xls");
		HashMap<String, Matriz> datos = miLector.LeerHTML();
		RdP miRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"),
				datos.get("tiempos"));
		// Crea la cola y el monitor
		Colas miCola = new Colas(12);
		GestorDeMonitor miMonitor = GestorDeMonitor.getInstance(miRed, miCola);

		int secuenciaA[] = { 0 }; // CLOCK
		int secuenciaB[] = { 3 }; // RESTRICCIONES
		int secuenciaC[] = { 1, 4 }; // TANQUE VACIO
		int secuenciaD[] = { 2, 5 }; // TANQUE LLENO
		int secuenciaE[] = { 11, 6, 7, 8, 9, 10 }; // CARRO 1
		int secuenciaF[] = { 8, 9, 10, 11, 6, 7 }; // CARRO 2

		// Crea los dos carros, les pasa el monitor, un id, y la secuencia de
		// disparo de cada uno
		Hilo A = new Hilo(miMonitor, "Clock", secuenciaA);
		Hilo B = new Hilo(miMonitor, "Restricciones", secuenciaB);
		Hilo C = new Hilo(miMonitor, "Tanque Vacio", secuenciaC);
		Hilo D = new Hilo(miMonitor, "Tanque Lleno", secuenciaD);
		Hilo E = new Hilo(miMonitor, "Carro A", secuenciaE);
		Hilo F = new Hilo(miMonitor, "Carro B", secuenciaF);

		// Crea los hilos para los carros
		Thread hiloA = new Thread(A);
		Thread hiloB = new Thread(B);
		Thread hiloC = new Thread(C);
		Thread hiloD = new Thread(D);
		Thread hiloE = new Thread(E);
		Thread hiloF = new Thread(F);

		hiloA.start();
		hiloB.start();
		hiloC.start();
		hiloD.start();
		hiloE.start();
		hiloF.start();

	}

}
