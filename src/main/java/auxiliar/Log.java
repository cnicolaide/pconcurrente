package auxiliar;

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
		if (fichero.exists()) {
			fichero.delete();
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
			bw.write(sTextoFinal);
			bw.newLine();
			bw.close();
		} catch (IOException ex) {
			System.out.println(ex);
			JOptionPane.showMessageDialog(null, ex, "Log: ", JOptionPane.ERROR_MESSAGE);
		}
	}
}
