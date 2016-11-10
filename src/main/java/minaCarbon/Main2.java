package minaCarbon;

import java.util.HashMap;

import auxiliar.Lector;
import auxiliar.Matriz;
import procesadorPetri.Colas;
import procesadorPetri.GestorDeMonitor;
import procesadorPetri.RdP;

public class Main2 {
	private static Lector miLector = new Lector("redCompleta.html", "completaConTiempos.xls");
	private static HashMap<String, Matriz> datos = miLector.read();
	private static RdP miRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"),
			datos.get("tiempos"));

	public static void main(String[] args) {

		// Crea la cola y el monitor
		Colas miCola = new Colas(datos.get("incidencia").getColCount());
		GestorDeMonitor miMonitor = GestorDeMonitor.getInstance(miRed, miCola);

		// Crea las secuencias de disparo
		int secuenciaA[] = { 0 }; // CLOCK
		int secuenciaB[] = { 3 }; // RESTRICCIONES
		int secuenciaC[] = { 1, 4 }; // TANQUE VACIO
		int secuenciaD[] = { 2, 5 }; // TANQUE LLENO
		int secuenciaE[] = { 11, 6, 7, 8, 9, 10 }; // CARRO 1
		int secuenciaF[] = { 8, 9, 10, 11, 6, 7 }; // CARRO 2

		// Crea los hilos, les pasa el monitor, una descripcion, y la secuencia
		// de disparo de cada uno
		new Hilo(miMonitor, "Clock", secuenciaA);
		new Hilo(miMonitor, "Restricciones", secuenciaB);
		new Hilo(miMonitor, "Tanque Vacio", secuenciaC);
		new Hilo(miMonitor, "Tanque Lleno", secuenciaD);
		new Hilo(miMonitor, "Carro A", secuenciaE);
		new Hilo(miMonitor, "Carro B", secuenciaF);

	}

}
