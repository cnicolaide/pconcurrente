package Auxiliar;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Lector {

	private int iIncidencia, iInhibicion;
	private int[] iMarcado = new int[2];
	private Document html;
	private Elements tableRowElements;
	private HashMap<String, Matriz> ldatos = new HashMap<>();
	private String sRed = null, sTiempos = null;

	public Lector(String sRed, String sTiempos) {
		this.sRed = sRed;
		this.sTiempos = sTiempos;
	}

	public HashMap<String, Matriz> LeerHTML() {
		File oFile = null;
		if (sRed == null) {
			JFileChooser fileChooser = new JFileChooser();
			int seleccion = fileChooser.showSaveDialog(fileChooser);
			if (seleccion == JFileChooser.APPROVE_OPTION) {
				oFile = fileChooser.getSelectedFile();
			}
		} else {
			oFile = new File(System.getProperty("user.dir") + "\\docs\\redes\\" + sRed);
		}
		try {
			html = Jsoup.parse(oFile, "UTF-8", "http://example.com/");
			Elements tableElements = html.select("table");
			tableRowElements = tableElements.select(":not(thead) tr");
			for (int i = 0; i < tableRowElements.size(); i++) {
				Element row = tableRowElements.get(i);
				Elements rowItems = row.select("td");

				for (int j = 0; j < rowItems.size(); j++) {
					switch (rowItems.get(j).text()) {
					case "Combined incidence matrix I":
						iIncidencia = i;
						break;
					case "Inhibition matrix H":
						iInhibicion = i;
						break;
					case "Current":
						iMarcado[0] = i;
						iMarcado[1] = j;
						break;
					}
				}
			}
			obtenerIncidencia();
			obtenerInhibicion();
			obtenerMarcado();
			LeerExcelTiempos();
		} catch (IOException ex) {
			Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ldatos;
	}

	// PARSEA LA MATRIZ DE INICDENCIA
	private void obtenerIncidencia() {
		Element row = tableRowElements.get(iIncidencia + 1);
		String[] datos = row.text().split(" ");
		int col = 0;
		int fil = 0;
		for (int i = 0; i < datos.length; i++) {
			if (datos[i].contains("T")) {
				col++;
			}
			if (datos[i].contains("P")) {
				fil++;
			}
		}
		int[] iNumTransicionesT = new int[col];
		int iNumT = 0;
		for (String dato : datos) {
			if (dato.contains("T")) {
				iNumTransicionesT[iNumT] = Integer.parseInt(dato.replace("T", ""));
				iNumT++;
			}
		}
		Matriz mat = new Matriz(fil, col);
		for (int i = 0; i < datos.length; i++) {
			if (datos[i].contains("P")) {
				fil = Integer.parseInt(datos[i].replace("P", ""));
				for (int j = 0; j < col; j++) {
					mat.setDato(fil, iNumTransicionesT[j], Integer.parseInt(datos[i + 1 + j]));
				}
			}
		}
		ldatos.put("incidencia", mat);
	}

	// PARSEA LA MATRIZ DE INHIBICION
	private void obtenerInhibicion() {
		Element row = tableRowElements.get(iInhibicion + 1);
		String[] datos = row.text().split(" ");
		int col = 0;
		int fil = 0;
		for (int i = 0; i < datos.length; i++) {
			if (datos[i].contains("T")) {
				col++;
			}
			if (datos[i].contains("P")) {
				fil++;
			}
		}
		int[] iNumTransicionesT = new int[col];
		int iNumT = 0;
		for (String dato : datos) {
			if (dato.contains("T")) {
				iNumTransicionesT[iNumT] = Integer.parseInt(dato.replace("T", ""));
				iNumT++;
			}
		}
		Matriz mat = new Matriz(fil, col);
		for (int i = 0; i < datos.length; i++) {
			if (datos[i].contains("P")) {
				fil = Integer.parseInt(datos[i].replace("P", ""));
				for (int j = 0; j < col; j++) {
					mat.setDato(fil, iNumTransicionesT[j], Integer.parseInt(datos[i + 1 + j]));
				}
			}
		}
		ldatos.put("inhibicion", mat);
	}

	// PARSEA EL MARCADO DE LA RED
	private void obtenerMarcado() {
		Element row = tableRowElements.get(iMarcado[0]);
		Elements rowItems = row.select("td");
		Elements rowItemsAnt = tableRowElements.get(iMarcado[0] - 2).select("td");
		Matriz mat = new Matriz(1, rowItems.size() - 1);

		for (int j = 0; j < mat.getColCount(); j++) {
			String sColumna = rowItemsAnt.get(j + 1).text();
			if (sColumna.contains("P")) {
				mat.setDato(0, Integer.parseInt(sColumna.replace("P", "")),
						Integer.parseInt(rowItems.get(j + 1).text()));
			}
		}
		ldatos.put("marcado", mat);
	}

	// PARSEA EL XLS CON LOS TIEMPOS DE LAS TRANSICIONES TEMPORALES
	private void LeerExcelTiempos() {
		Matriz tiempo = null;
		File file;
		if (sTiempos == null) {
			JFileChooser fileChooser = new JFileChooser();
			int seleccion = fileChooser.showSaveDialog(fileChooser);
			if (seleccion == JFileChooser.APPROVE_OPTION)
				file = fileChooser.getSelectedFile();
		}
		file = new File(System.getProperty("user.dir") + "\\docs\\tablas\\" + sTiempos);
		Workbook wbook = null;
		try {
			wbook = Workbook.getWorkbook(file);
		} catch (IOException ex) {
			Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (BiffException ex) {
			Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
		}
		Sheet hoja = wbook.getSheet(0);
		int columnas = hoja.getColumns();
		int filas = hoja.getRows();
		tiempo = new Matriz(filas, columnas);
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				Cell cell = hoja.getCell(j, i);
				if (cell.getType() == CellType.NUMBER) {
					NumberCell nc = (NumberCell) cell;
					tiempo.setDato(i, j, (int) nc.getValue());
				}
			}
		}
		ldatos.put("tiempos", tiempo);
	}
}
