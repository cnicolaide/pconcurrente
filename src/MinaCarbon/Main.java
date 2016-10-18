package MinaCarbon;

import java.util.HashMap;

import Auxiliar.Lector;
import Auxiliar.Matriz;
import ProcesadorPetri.Colas;
import ProcesadorPetri.Monitor;
import ProcesadorPetri.RdP;

public class Main {

	public static void main(String[] args) {

		Lector miLector = new Lector("carros.html");
		HashMap<String, Matriz> datos = miLector.LeerHTML();

		Colas miCola = new Colas(6);
		RdP miRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"));
		Monitor miMonitor = new Monitor(miRed, miCola);

		Hilo A = new Hilo(miMonitor, 1);
		Hilo B = new Hilo(miMonitor, 2);

		Thread hiloA = new Thread(A);
		Thread hiloB = new Thread(B);

		hiloA.start();
		hiloB.start();
		System.err.println("ASDAD");
	}
}
