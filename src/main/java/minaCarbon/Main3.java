package minaCarbon;

import java.util.HashMap;

import auxiliar.Lector;
import auxiliar.Matriz;
import procesadorPetri.Colas;
import procesadorPetri.GestorDeMonitor;
import procesadorPetri.RdP;

public class Main3 {
	public static void main(String[] args) {
		// Lee las matrices de marcado, inicidencia e inhibicion desde el
		// archivo
		// HTML exportado en PIPE
		Lector miLector = new Lector("redCompleta.html", "completaSinTiempos.xls");
		HashMap<String, Matriz> datos = miLector.read();
		RdP miRed = new RdP(datos.get("marcado"), datos.get("incidencia"), datos.get("inhibicion"),
				datos.get("tiempos"));

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
		int secuenciaH[] = { 13, 14 }; // LECTOR CH4
		int secuenciaI[] = { 19, 20 }; // TIMEOUT
		int secuenciaJ[] = { 16 }; // NO HAY GAS
		int secuenciaK[] = { 15, 17 }; // HAY GAS
		int secuenciaL[] = { 18 }; // RESTRICCION CH4
		int secuenciaM[] = { 21 }; // RESET

		// Crea los hilos, les pasa el monitor, una descripcion, y la secuencia
		// de
		// disparo de cada uno
		Hilo A = new Hilo(miMonitor, "Clock Agua", secuenciaA);
		Hilo B = new Hilo(miMonitor, "Restriccion Agua", secuenciaB);
		Hilo C = new Hilo(miMonitor, "Tanque Vacio", secuenciaC);
		Hilo D = new Hilo(miMonitor, "Tanque Lleno", secuenciaD);
		Hilo E = new Hilo(miMonitor, "Carro A", secuenciaE);
		Hilo F = new Hilo(miMonitor, "Carro B", secuenciaF);
		Hilo G = new Hilo(miMonitor, "Clock CH4", secuenciaG);
		Hilo H = new Hilo(miMonitor, "Lector CH4", secuenciaH);
		Hilo I = new Hilo(miMonitor, "Timeout", secuenciaI);
		Hilo J = new Hilo(miMonitor, "No hay gas", secuenciaJ);
		Hilo K = new Hilo(miMonitor, "Hay gas", secuenciaK);
		Hilo L = new Hilo(miMonitor, "Restriccion CH4", secuenciaL);
		Hilo M = new Hilo(miMonitor, "Reset", secuenciaM);

		// Instancia los hilos
		Thread hiloA = new Thread(A);
		Thread hiloB = new Thread(B);
		Thread hiloC = new Thread(C);
		Thread hiloD = new Thread(D);
		Thread hiloE = new Thread(E);
		Thread hiloF = new Thread(F);
		Thread hiloG = new Thread(G);
		Thread hiloH = new Thread(H);
		Thread hiloI = new Thread(I);
		Thread hiloJ = new Thread(J);
		Thread hiloK = new Thread(K);
		Thread hiloL = new Thread(L);
		Thread hiloM = new Thread(M);

		// Pone las instancias a correr
		hiloA.start();
		hiloB.start();
		hiloC.start();
		hiloD.start();
		hiloE.start();
		hiloF.start();
		hiloG.start();
		hiloH.start();
		hiloI.start();
		hiloJ.start();
		hiloK.start();
		hiloL.start();
		hiloM.start();
	}
}