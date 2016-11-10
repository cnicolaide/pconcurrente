package auxiliar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class Log {

	private static Log oFicheros = null;
	private SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	private String sArchivo = "System.txt", sArchivo2 = "Marcado.txt", sArchivo3 = "Transiciones.txt";
	private String sUbicacion = System.getProperty("user.dir") + "\\log\\";
	private int contador = 0;

	private Log() {
		File fichero = new File(sUbicacion + sArchivo);
		File fichero2 = new File(sUbicacion + sArchivo2);
		File fichero3 = new File(sUbicacion + sArchivo3);

		if (fichero.exists()) {
			fichero.delete();
		}
		if (fichero2.exists()) {
			fichero2.delete();
		}
		if (fichero3.exists()) {
			fichero3.delete();
		}
	}

	// IMPLEMENTA SINGLETON, PARA QUE SOLO EXISTA UNA INSTANCIA DE LOG
	public static synchronized Log getInstance() {
		if (oFicheros == null) {
			oFicheros = new Log();
		}
		return oFicheros;
	}

	// IMPRIME EL LOG POR CONSOLA Y TAMBIEN LO GUARDA EN UN ARCHIVO DE TEXTO
	public void escribir(String sTitulo, String sTexto, boolean color) {
		contador++;
		String sTextoFinal = contador + " > " + sd.format(new Date()) + " - " + sTitulo + ": " + sTexto;

		if (color)
			System.err.println(sTextoFinal);
		else
			System.out.println(sTextoFinal);

		File fichero = new File(System.getProperty("user.dir") + "\\log\\" + sArchivo);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fichero, true));
			bw.write(sTexto);
			bw.newLine();
			bw.close();
		} catch (IOException ex) {
			System.out.println(ex);
			JOptionPane.showMessageDialog(null, ex, "Log: ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void escribirTransiciones(String sTexto) {
		File fichero3 = new File(System.getProperty("user.dir") + "\\log\\" + sArchivo3);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fichero3, true));
			bw.write(sTexto);
			bw.newLine();
			bw.close();
		} catch (IOException ex) {
			System.out.println(ex);
			JOptionPane.showMessageDialog(null, ex, "Log: ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void escribirMarcado(String sTexto) {
		File fichero2 = new File(System.getProperty("user.dir") + "\\log\\" + sArchivo2);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fichero2, true));
			bw.write(sTexto);
			bw.newLine();
			bw.close();
		} catch (IOException ex) {
			System.out.println(ex);
			JOptionPane.showMessageDialog(null, ex, "Log: ", JOptionPane.ERROR_MESSAGE);
		}
	}

}
