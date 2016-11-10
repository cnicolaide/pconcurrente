package minaCarbon;

import java.util.HashMap;

import auxiliar.Lector;
import auxiliar.Matriz;
import procesadorPetri.Colas;
import procesadorPetri.GestorDeMonitor;
import procesadorPetri.RdP;

public class Main3 {
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
		int secuenciaB[] = { 5 }; // RESTRICCION AGUA
		int secuenciaC[] = { 1, 4 }; // TANQUE VACIO
		int secuenciaD[] = { 2, 3 }; // TANQUE LLENO
		int secuenciaE[] = { 8, 11, 10, 9, 6, 7 }; // CARRO 1
		int secuenciaF[] = { 9, 6, 7, 8, 11, 10 }; // CARRO 2
		int secuenciaG[] = { 12 }; // CLOCK SENSOR CH4
		int secuenciaH[] = { 13 }; // LECTOR CH4
		int secuenciaI[] = { 19, 20 }; // TIMEOUT
		int secuenciaJ[] = { 16 }; // NO HAY GAS
		int secuenciaK[] = { 15, 17 }; // HAY GAS
		int secuenciaL[] = { 18 }; // RESTRICCION CH4
		int secuenciaM[] = { 21 }; // RESET
		int secuenciaN[] = { 14 }; // MEDICION CH4

		// Crea los hilos, les pasa el monitor, una descripcion, y la secuencia
		// de disparo de cada uno
		new Hilo(miMonitor, "Clock Agua", secuenciaA);
		new Hilo(miMonitor, "Restriccion Agua", secuenciaB);
		new Hilo(miMonitor, "Tanque Vacio", secuenciaC);
		new Hilo(miMonitor, "Tanque Lleno", secuenciaD);
		new Hilo(miMonitor, "Carro A", secuenciaE);
		new Hilo(miMonitor, "Carro B", secuenciaF);
		new Hilo(miMonitor, "Clock CH4", secuenciaG);
		new Hilo(miMonitor, "Lector CH4", secuenciaH);
		new Hilo(miMonitor, "Timeout", secuenciaI);
		new Hilo(miMonitor, "No hay gas", secuenciaJ);
		new Hilo(miMonitor, "Hay gas", secuenciaK);
		new Hilo(miMonitor, "Restriccion CH4", secuenciaL);
		new Hilo(miMonitor, "Reset", secuenciaM);
		new Hilo(miMonitor, "Medicion CH4", secuenciaN);

	}
}