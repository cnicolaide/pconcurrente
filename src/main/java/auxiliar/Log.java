package auxiliar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

public class Log {

	private static Log oFicheros = null;
	private SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	private String sArchivo = "System.txt";
	private String sUbicacion = System.getProperty("user.dir") + "//log//";
	private int contador = 0;

	private Log() {
		File file = new File(sUbicacion);
		try {
			FileUtils.cleanDirectory(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized Log getInstance() {
		if (oFicheros == null) {
			oFicheros = new Log();
		}
		return oFicheros;
	}

	// IMPRIME EL LOG POR CONSOLA Y TAMBIEN LO GUARDA EN UN ARCHIVO DE TEXTO
	public void escribirEimprimir(String sTitulo, String sTexto, boolean color) {
		contador++;
		String sTextoFinal = contador + " > " + sd.format(new Date()) + " - " + sTitulo + ": " + sTexto;

		if (color)
			System.err.println(sTextoFinal);
		else
			System.out.println(sTextoFinal);

		File fichero = new File(sUbicacion + sArchivo);
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

	// GUARDA EN UN ARCHIVO CON EL NOMBRE QUE SE LE PASA EL TEXTO QUE SE DESEE
	public void escribir(String sArchivo, String sTexto) {
		File fichero = new File(sUbicacion + sArchivo + ".txt");
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

}
