package Auxiliar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class Log {

	private String sNombre;
	private String sArchivo;
	private static Log oFicheros = null;
	private SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	private int contador = 0;

	private Log() {
		this.sNombre = "Redes de Petri Temporales";
		this.sArchivo = "System.txt";
		File fichero = new File(System.getProperty("user.dir") + "\\log\\" + sArchivo);
		if (fichero.exists()) {
			fichero.delete();
		}
	}

	public static Log Instance() {
		if (oFicheros == null) {
			oFicheros = new Log();
		}
		return oFicheros;
	}

	public void Escribir(String sTitulo, String sTexto) {
		contador++;
		sTitulo = sTitulo + ": " + sTexto;
		String sTextoFinal = "" + contador + " > " + sd.format(new Date()) + " - " + sTitulo;
		System.out.println(sTextoFinal);
		File fichero = new File(System.getProperty("user.dir") + "\\log\\" + sArchivo);
		BufferedWriter bw = null;
		try {
			if (fichero.exists()) {
				bw = new BufferedWriter(new FileWriter(fichero, true));
				// pw = new PrintWriter(fichero,true);
			} else {
				bw = new BufferedWriter(new FileWriter(fichero, true));
				// pw = new PrintWriter(fichero);
			}
			bw.write(sTextoFinal);
			bw.close();
			// pw.close();
		} catch (IOException ex) {
			System.out.println(ex);
			JOptionPane.showMessageDialog(null, ex, "Escribir", JOptionPane.ERROR_MESSAGE);
		}
	}

	public String getsNombre() {
		return sNombre;
	}

	public String getsArchivo() {
		return sArchivo;
	}
}
