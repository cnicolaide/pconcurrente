package MinaCarbon;

import java.util.HashMap;

import Auxiliar.Lector;
import Auxiliar.Matriz;
import ProcesadorPetri.Colas;
import ProcesadorPetri.Monitor;
import ProcesadorPetri.RdP;

public class Main {

	public static void main(String[] args) {
		// Lee las matrices de marcado, inicidencia e inhibicion desde el
		// archivo
		// HTML exportado en PIPE
		Lector miLector = new Lector("carros.html");
		HashMap<String, Matriz> datos = miLector.LeerHTML();
		RdP miRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"));

		// Crea la cola y el monitor
		Colas miCola = new Colas(6);
		Monitor miMonitor = new Monitor(miRed, miCola);

		int secuenciaA[] = { 5, 0, 1, 2, 3, 4 };
		int secuenciaB[] = { 2, 3, 4, 5, 0, 1 };

		// Crea los dos carros, les pasa el monitor, un id, y la secuencia de
		// disparo de cada uno
		Carro A = new Carro(miMonitor, 0, secuenciaA);
		Carro B = new Carro(miMonitor, 1, secuenciaB);

		// Crea los hilos para los carros
		Thread hiloA = new Thread(A);
		Thread hiloB = new Thread(B);

		hiloA.start();
		hiloB.start();
	}
}
