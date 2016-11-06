package minaCarbon;

import java.util.HashMap;

import auxiliar.Lector;
import auxiliar.Matriz;
import procesadorPetri.Colas;
import procesadorPetri.GestorDeMonitor;
import procesadorPetri.RdP;

public class Main2 {
	public static void main(String[] args) {
		// Lee las matrices de marcado, inicidencia e inhibicion desde el
		// archivo
		// HTML exportado en PIPE
		Lector miLector = new Lector("bombaYSensorycarro.html", "completasintiempos.xls");
		HashMap<String, Matriz> datos = miLector.read();
		RdP miRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"),
				datos.get("tiempos"));
		
		// Crea la cola y el monitor
		Colas miCola = new Colas(12);
		GestorDeMonitor miMonitor = GestorDeMonitor.getInstance(miRed, miCola);

		// Crea las secuencias de disparo
		int secuenciaA[] = { 0 }; // CLOCK
		int secuenciaB[] = { 3 }; // RESTRICCIONES
		int secuenciaC[] = { 1, 4 }; // TANQUE VACIO
		int secuenciaD[] = { 2, 5 }; // TANQUE LLENO
		int secuenciaE[] = { 11, 6, 7, 8, 9, 10 }; // CARRO 1
		int secuenciaF[] = { 8, 9, 10, 11, 6, 7 }; // CARRO 2

		// Crea los hilos, les pasa el monitor, una descripcion, y la secuencia de
		// disparo de cada uno
		Hilo A = new Hilo(miMonitor, "Clock", secuenciaA);
		Hilo B = new Hilo(miMonitor, "Restricciones", secuenciaB);
		Hilo C = new Hilo(miMonitor, "Tanque Vacio", secuenciaC);
		Hilo D = new Hilo(miMonitor, "Tanque Lleno", secuenciaD);
		Hilo E = new Hilo(miMonitor, "Carro A", secuenciaE);
		Hilo F = new Hilo(miMonitor, "Carro B", secuenciaF);

		// Instancia los hilos 
		Thread hiloA = new Thread(A);
		Thread hiloB = new Thread(B);
		Thread hiloC = new Thread(C);
		Thread hiloD = new Thread(D);
		Thread hiloE = new Thread(E);
		Thread hiloF = new Thread(F);

		// Pone las instancias a correr
		hiloA.start();
		hiloB.start();
		hiloC.start();
		hiloD.start();
		hiloE.start();
		hiloF.start();

	}

}