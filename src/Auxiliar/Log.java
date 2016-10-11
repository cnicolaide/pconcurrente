package Auxiliar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class Log {

	private String sArchivo;
	private static Log oFicheros = null;
	private SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	private int contador = 0;

	private Log() {
		this.sArchivo = "System.txt";
		File fichero = new File(System.getProperty("user.dir") + "\\log\\" + sArchivo);
		// Asegura escribir un log nuevo por cada corrida
		if (fichero.exists()) {
			fichero.delete();
		}
	}

	// ASEGURA CON EL PATRON SINGLETON, QUE SOLO EXISTA UNA INSTANCIA DE LOG
	public static Log Instance() {
		if (oFicheros == null) {
			oFicheros = new Log();
		}
		return oFicheros;
	}

	// IMPRIME EL LOG POR CONSOLA Y TAMBIEN LO GUARDA EN UN ARCHIVO DE TEXTO
	// PLANO
	public void Escribir(String sTitulo, String sTexto) {
		contador++;
		String sTextoFinal = contador + " > " + sd.format(new Date()) + " - " + sTitulo + ": " + sTexto;
		System.out.println(sTextoFinal);
		File fichero = new File(System.getProperty("user.dir") + "\\log\\" + sArchivo);
		BufferedWriter bw = null;
		try {
			if (fichero.exists()) {
				bw = new BufferedWriter(new FileWriter(fichero, true));
			} else {
				bw = new BufferedWriter(new FileWriter(fichero, true));
			}
			bw.write(sTextoFinal);
			bw.close();
		} catch (IOException ex) {
			System.out.println(ex);
			JOptionPane.showMessageDialog(null, ex, "Escribir", JOptionPane.ERROR_MESSAGE);
		}
	}

}
