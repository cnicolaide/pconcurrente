package Auxiliar;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Lector {

	private int iIncidencia;
	private int iInhibicion;
	private int[] iMarcado = new int[2];
	private Document html;
	private Elements tableRowElements;
	private HashMap<String, Matriz> ldatos = new HashMap<>();
	private boolean print = false;
	private String sPrioridad = null;
	private String sAutomaticas = null;
	private String sRed = null;
	private String sTiempos;

	public Lector(String sPrioridad, String sAutomaticas, String sRed, String sTiempos) {
		this.sAutomaticas = sAutomaticas;
		this.sPrioridad = sPrioridad;
		this.sRed = sRed;
		this.sTiempos = sTiempos;
	}

	public Lector() {

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
			oFile = new File(System.getProperty("user.dir") + "\\redes\\" + sRed);
		}
		try {
			html = Jsoup.parse(oFile, "UTF-8", "http://example.com/");
			Elements tableElements = html.select("table");
			tableRowElements = tableElements.select(":not(thead) tr");
			for (int i = 0; i < tableRowElements.size(); i++) {
				Element row = tableRowElements.get(i);
				// System.out.println("row");
				Elements rowItems = row.select("td");

				for (int j = 0; j < rowItems.size(); j++) {
					switch (rowItems.get(j).text()) {
					case "Combined incidence matrix I":
						iIncidencia = i;
						// System.out.println(rowItems.get(j).text());
						// System.out.println("Posicion: "+i+" - "+j);
						break;
					case "Inhibition matrix H":
						iInhibicion = i;
						// System.out.println(rowItems.get(j).text());
						// System.out.println("Posicion: "+i+" - "+j);
						break;
					case "Current":
						iMarcado[0] = i;
						iMarcado[1] = j;
						// System.out.println(rowItems.get(j).text());
						// System.out.println("Posicion: "+i+" - "+j);
						break;
					}
				}
				// System.out.println();
			}
			obtenerIncidencia();
			obtenerInhibicion();
			obtenerMarcado();
			LeerExcelPrioridad();
			LeerExcelTiempos();
			LeerExcelAutomaticas();
		} catch (IOException ex) {
			Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ldatos;
	}

	public void LeerExcelPrioridad() {
		Matriz prioridad = null;
		File file = null;
		if (sPrioridad == null) {
			JFileChooser fileChooser = new JFileChooser();
			int seleccion = fileChooser.showSaveDialog(fileChooser);
			if (seleccion == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
			}
		}
		file = new File(System.getProperty("user.dir") + "\\Redes\\" + sPrioridad);
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
		prioridad = new Matriz(filas, columnas);
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				Cell cell = hoja.getCell(j, i);
				if (cell.getType() == CellType.NUMBER) {
					NumberCell nc = (NumberCell) cell;
					prioridad.setDato(i, j, (int) nc.getValue());
				}
			}
		}
		Matriz oMatriz = prioridad;
		if (print) {
			// Ficheros.Instance().Escribir("LEER_ARCHIVO", "PRIORIDAD");
			// Ficheros.Instance().Escribir("LEER_ARCHIVO", oMatriz.toString());
		}
		ldatos.put("prioridad", prioridad);
	}

	public void LeerExcelAutomaticas() {
		Matriz prioridad = null;
		File file = null;
		if (sAutomaticas == null) {
			JFileChooser fileChooser = new JFileChooser();
			int seleccion = fileChooser.showSaveDialog(fileChooser);
			if (seleccion == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
			}
		}
		file = new File(System.getProperty("user.dir") + "\\Redes\\" + sAutomaticas);
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
		prioridad = new Matriz(filas, columnas);
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				Cell cell = hoja.getCell(j, i);
				if (cell.getType() == CellType.NUMBER) {
					NumberCell nc = (NumberCell) cell;
					prioridad.setDato(i, j, (int) nc.getValue());
				}
			}
		}
		Matriz oMatriz = prioridad;
		if (print) {
			// Ficheros.Instance().Escribir("LEER_ARCHIVO", "AUTOMATICAS");
			// Ficheros.Instance().Escribir("LEER_ARCHIVO", oMatriz.toString());
		}
		ldatos.put("automaticas", prioridad);
	}

	public void LeerExcelTiempos() {
		Matriz prioridad = null;
		File file;
		if (sTiempos == null) {
			JFileChooser fileChooser = new JFileChooser();
			int seleccion = fileChooser.showSaveDialog(fileChooser);
			if (seleccion == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
			}
		}
		file = new File(System.getProperty("user.dir") + "\\Redes\\" + sTiempos);
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
		prioridad = new Matriz(filas, columnas);
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				Cell cell = hoja.getCell(j, i);
				if (cell.getType() == CellType.NUMBER) {
					NumberCell nc = (NumberCell) cell;
					prioridad.setDato(i, j, (int) nc.getValue());
				}
			}
		}
		Matriz oMatriz = prioridad;
		if (print) {
			// Ficheros.Instance().Escribir("LEER_ARCHIVO", "TIEMPOS");
			// Ficheros.Instance().Escribir("LEER_ARCHIVO", oMatriz.toString());
		}
		ldatos.put("tiempos", prioridad);
	}

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
		if (print) {
			Matriz oMatriz = mat;
			// Ficheros.Instance().Escribir("LEER_ARCHIVO", "Incidencia");
			// Ficheros.Instance().Escribir("LEER_ARCHIVO", oMatriz.toString());
		}
		ldatos.put("incidencia", mat);
	}

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
		if (print) {
			Matriz oMatriz = mat;
			// Ficheros.Instance().Escribir("LEER_ARCHIVO", "Inhibicion");
			// Ficheros.Instance().Escribir("LEER_ARCHIVO", oMatriz.toString());
		}
		ldatos.put("inhibicion", mat);
	}

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
			// mat[0][j] = Integer.parseInt(rowItems.get(j+1).text());
		}
		if (print) {
			Matriz oMatriz = mat;
			// Ficheros.Instance().Escribir("LEER_ARCHIVO", "Marcado");
			// Ficheros.Instance().Escribir("LEER_ARCHIVO", oMatriz.toString());
		}
		ldatos.put("marcado", mat);
	}
}
