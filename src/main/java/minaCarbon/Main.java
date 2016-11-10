package minaCarbon;

import java.util.HashMap;

import auxiliar.Lector;
import auxiliar.Matriz;
import procesadorPetri.Colas;
import procesadorPetri.GestorDeMonitor;
import procesadorPetri.RdP;

public class Main {
	private static Lector miLector = new Lector("carros.html", "chicaConTiempos.xls");
	private static HashMap<String, Matriz> datos = miLector.read();
	private static RdP miRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"),
			datos.get("tiempos"));

	public static void main(String[] args) {

		// Crea la cola y el monitor
		Colas miCola = new Colas(datos.get("incidencia").getColCount());
		GestorDeMonitor miMonitor = GestorDeMonitor.getInstance(miRed, miCola);

		// Crea las secuencias de disparo
		int secuenciaA[] = { 5, 0, 1, 2, 3, 4 };
		int secuenciaB[] = { 2, 3, 4, 5, 0, 1 };

		// Crea los hilos, les pasa el monitor, una descripcion, y la secuencia
		// de disparo de cada uno
		new Hilo(miMonitor, "Carro A", secuenciaA);
		new Hilo(miMonitor, "Carro B", secuenciaB);

	}
}
