package minaCarbon;

import java.util.HashMap;

import auxiliar.Lector;
import auxiliar.Matriz;
import procesadorPetri.Colas;
import procesadorPetri.GestorDeMonitor;
import procesadorPetri.RdP;

public class Main {
	public static void main(String[] args) {
		// Lee las matrices de marcado, inicidencia e inhibicion desde el
		// archivo HTML exportado en PIPE
		Lector miLector = new Lector("carros.html", "tiempos.xls");
		HashMap<String, Matriz> datos = miLector.read();
		RdP miRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"),
				datos.get("tiempos"));

		// Crea la cola y el monitor
		Colas miCola = new Colas(datos.get("incidencia").getColCount());
		GestorDeMonitor miMonitor = GestorDeMonitor.getInstance(miRed, miCola);

		// Crea las secuencias de disparo
		int secuenciaA[] = { 5, 0, 1, 2, 3, 4 };
		int secuenciaB[] = { 2, 3, 4, 5, 0, 1 };

		// Crea los hilos, les pasa el monitor, una descripcion, y la secuencia
		// de disparo de cada uno
		Hilo A = new Hilo(miMonitor, "Carro A", secuenciaA);
		Hilo B = new Hilo(miMonitor, "Carro B", secuenciaB);

		// Instancia los hilos
		Thread hiloA = new Thread(A);
		Thread hiloB = new Thread(B);

		// Pone las instancias a correr
		hiloA.start();
		hiloB.start();

	}
}
