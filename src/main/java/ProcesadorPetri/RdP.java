package ProcesadorPetri;

import Auxiliar.Log;
import Auxiliar.Matriz;

public class RdP {

	private Matriz mMarcadoInicial, mMarcadoActual, mIncidencia, mInhibicion, mSensibilizadas;// ,
																								// mTiempo;

	// CONSTRUCTOR DE LA RED DE PETRI
	public RdP(Matriz mMarcado, Matriz mIncidencia, Matriz mInhibicion, Matriz mTiempo) {
		this.mMarcadoActual = mMarcado;
		this.mMarcadoInicial = mMarcado;
		this.mIncidencia = mIncidencia;
		this.mInhibicion = mInhibicion;
		// this.mTiempo = mTiempo;
		mSensibilizadas = calcularSensibilizadas();
		// System.out.println("Tiempos: " + mTiempo);
		// System.out.println("\n");
		Log.getInstance().escribir("RdP", " ***** SE INSTANCIO LA RED DE PETRI *****");
		printEstados();
	}

	// DISPARA UNA TRANSICION DE LA RED DE PETRI UTILIZANDO LA FORMULA: Mi+1 =
	// Mi+I*d.AND*(!(F*H))
	public boolean disparar(int posicion) {

		// Transforma la posicion que recibe en un vector de disparo
		Matriz mDisparo = crearVectorDisparo(posicion);

		// Verifica que la transicion que le pasan este sensibilizada, si esta,
		// dispara.
		if (mSensibilizadas.getVal(0, posicion) == 1) {

			// Carga en el marcado actual, los valores iniciales para operar
			mMarcadoActual = mMarcadoInicial;

			// Crea la matriz I*d
			Matriz mIncidenciaxDisparo = mIncidencia.mult(mDisparo.transpose()).transpose();

			// Almacena en la matriz el nuevo marcado
			mMarcadoActual = mMarcadoActual.plus(mIncidenciaxDisparo);

			// Guarda el nuevo estado de la red
			mMarcadoInicial = mMarcadoActual;

			// Examina cuales son las nuevas transiciones que se pueden disparar
			mSensibilizadas = calcularSensibilizadas();

			// Imprime los estados y retorna TRUE
			Log.getInstance().escribir("RdP", "Se ejecuto el disparo: T" + posicion);
			printEstados();
			return true;
		}

		// Imprime los estados y retorna FALSE
		Log.getInstance().escribir("RdP", "No se puede ejecutar el disparo: T" + posicion);
		printEstados();
		return false;
	}

	// CREA EL VECTOR F(Mi)
	private Matriz crearVectorFdeMi() {
		Matriz mVectorVi = new Matriz(mMarcadoActual.getFilCount(), mMarcadoActual.getColCount());
		return mVectorVi.getFdeMi(mMarcadoActual);
	}

	// CREA EL VECTOR DE DISPARO
	private Matriz crearVectorDisparo(int pos) {
		Matriz mDisparo = new Matriz(1, mIncidencia.getColCount());
		mDisparo.clear();
		mDisparo.setDato(0, pos, 1);
		return mDisparo;
	}

	// CREA F POR H
	private Matriz crearFporH() {
		Matriz mVectorVi = crearVectorFdeMi();
		Matriz mFdeH = mVectorVi.mult(mInhibicion);
		return mFdeH;
	}

	// Calendar ahora = Calendar.getInstance();

	private Matriz calcularSensibilizadas() {
		Matriz mSensibilizadas = new Matriz(1, mIncidencia.getColCount());
		// long[] timestamp = new long[mIncidencia.getColCount()];
		//
		// System.out.println(" Current time is : " + ahora.getTimeInMillis());

		// Inicializa el vector de Sensibilizadas en 1
		for (int i = 0; i < mIncidencia.getColCount(); i++) {
			mSensibilizadas.setDato(0, i, 1);
			// mTiempo.setDato(i, 0, (int) (mTiempo.getVal(i, 0) +
			// ahora.getTimeInMillis()));
		}

		// Crea la matriz (!(F*H))
		Matriz mFdeH = crearFporH().negar();

		// Recorre la matriz, hace las sumas y en caso que aux < 0 coloca 0 en
		// la matriz de sensibilizadas
		for (int i = 0; i < mIncidencia.getFilCount(); i++) {
			for (int j = 0; j < mIncidencia.getColCount(); j++) {
				// Verifica que (!(F*H)) o que la suma de Incidencia y Marcado
				// sea menor cero, en ese caso coloca 0 en la posicion de la
				// transicion
				if (mIncidencia.getVal(i, j) + mMarcadoActual.getVal(0, i) < 0 || mFdeH.getVal(0, j) == 0) {
					mSensibilizadas.setDato(0, j, 0);
				}
				// if (!testVentanaTiempo(1, timestamp[j]))
				// mSensibilizadas.setDato(0, j, 0);
			}
		}
		return mSensibilizadas;
	}

	// private boolean testVentanaTiempo(int disparo, long timestamp) {
	//
	// boolean esperando = false;
	// System.out
	// .println("Alfa: " + (mTiempo.getVal(disparo, 0) + timestamp) + "Ahora: "
	// + System.currentTimeMillis());
	// if ((mTiempo.getVal(disparo, 0) + timestamp < ahora.getTimeInMillis())
	// && (mTiempo.getVal(disparo, 1) + timestamp > ahora.getTimeInMillis()) &&
	// ((esperando == false)))
	// return true;
	// return false;
	//
	// }

	// DEVUELVE MATRIZ CON TRANSICIONES SENSIBILIZADAS
	public Matriz getSesibilizadas() {
		return mSensibilizadas;
	}

	// DEVUELVE MATRIZ CON EL MARCADO INICIAL
	public Matriz getMarcadoInicial() {
		return mMarcadoInicial;
	}

	// DEVUELVE MATRIZ CON EL NUEVO MARCADO
	public Matriz getMarcadoActual() {
		return mMarcadoActual;
	}

	// IMPRIME LOS ESTADOS DE LA RED EN CIERTO MOMENTO
	private void printEstados() {
		Log.getInstance().escribir("RdP", "Marcado Actual: " + mMarcadoActual);
		Log.getInstance().escribir("RdP", "Transiciones Sensibilizadas: " + mSensibilizadas);
	}
}