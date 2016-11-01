package MinaCarbon;

import java.util.HashMap;

import Auxiliar.Lector;
import Auxiliar.Matriz;
import ProcesadorPetri.Colas;
import ProcesadorPetri.GestorDeMonitor;
import ProcesadorPetri.RdP;

public class Main {

	public static void main(String[] args) {
		// Lee las matrices de marcado, inicidencia e inhibicion desde el
		// archivo
		// HTML exportado en PIPE
		Lector miLector = new Lector("carros.html", "tiempos.xls");
		HashMap<String, Matriz> datos = miLector.LeerHTML();
		RdP miRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"), datos.get("tiempos"));

		// Crea la cola y el monitor
		Colas miCola = new Colas(6);
		GestorDeMonitor miMonitor = GestorDeMonitor.getInstance(miRed, miCola);

		int secuenciaA[] = { 5, 0, 1, 2, 3, 4 };
		int secuenciaB[] = { 2, 3, 4, 5, 0, 1 };

		// Crea los dos carros, les pasa el monitor, un id, y la secuencia de
		// disparo de cada uno
		Hilo A = new Hilo(miMonitor, "Carro A", secuenciaA);
		Hilo B = new Hilo(miMonitor, "Carro B", secuenciaB);

		// Crea los hilos para los carros
		Thread hiloA = new Thread(A);
		Thread hiloB = new Thread(B);

		hiloA.start();
		hiloB.start();
		
	}
}
