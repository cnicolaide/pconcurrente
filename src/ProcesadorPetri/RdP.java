package ProcesadorPetri;

import Auxiliar.Log;
import Auxiliar.Matriz;

public class RdP {

	private Matriz mMarcadoInicial, mMarcadoActual, mIncidencia, mInhibicion, mFdeH, mSensibilizadas;

	// CONSTRUCTOR DE LA RED DE PETRI
	public RdP(Matriz mMarcado, Matriz mIncidencia, Matriz mInhibicion) {
		this.mMarcadoActual = mMarcado;
		this.mMarcadoInicial = mMarcado;
		this.mIncidencia = mIncidencia;
		this.mInhibicion = mInhibicion;
		mSensibilizadas = calcularSensibilizadas();
		System.out.println("\n");
		Log.Instance().Escribir("RdP", " ***** SE INSTANCIO LA RED DE PETRI *****");
		printEstados();
	}

	// DISPARA UNA TRANSICION DE LA RED DE PETRI UTILIZANDO LA FORMULA: Mi+1 =
	// Mi+I*d.AND*(!(F*H))
	public boolean disparar(int posicion) {

		// Transforma la posicion que recibe en un vector de disparo
		Matriz mDisparo = crearVectorDisparo(posicion);

		if (mSensibilizadas.getVal(0, posicion) == 1) {

			// Carga en el marcado actual, los valores iniciales para operar
			mMarcadoActual = mMarcadoInicial;

			// Crea la matriz (!(F*H))
			mFdeH = crearFporH().Negacion();

			// Crea la matriz d.AND*(!(F*H))
			Matriz mDandFporHNegado = mDisparo.AND(mFdeH);

			// Crea la matriz I*d.AND*(!(F*H))
			Matriz mIncidenciaxDisparo = (mIncidencia.mult(mDandFporHNegado.transpose())).transpose();

			// Almacena en la matriz nuevo marcado el resultado de
			// Mi+I*d.AND*(!(F*H))
			mMarcadoActual = mMarcadoActual.plus(mIncidenciaxDisparo);

			// Guarda el nuevo estado de la red
			mMarcadoInicial = mMarcadoActual;

			// Examina cuales son las nuevas transiciones que se pueden disparar
			mSensibilizadas = calcularSensibilizadas();

			// Imprime los estados y retorna TRUE
			Log.Instance().Escribir("RdP", "Se ejecuto el disparo: " + mDisparo);
			printEstados();
			return true;
		}

		// Imprime los estados y retorna FALSE
		Log.Instance().Escribir("RdP", "No se puede ejecutar el disparo: " + mDisparo.toString());
		printEstados();
		return false;
	}

	// CREA EL VECTOR F(Mi)
	private Matriz crearVectorFdeMi() {
		Matriz mVectorVi = new Matriz(mMarcadoActual.getFilCount(), mMarcadoActual.getColCount());
		return mVectorVi.FdeMi(mMarcadoActual);
	}

	// CREA EL VECTOR DE DISPARO
	private Matriz crearVectorDisparo(int pos) {
		Matriz mDisparo = new Matriz(1, mIncidencia.getColCount());
		mDisparo.Clear();
		mDisparo.setDato(0, pos, 1);
		return mDisparo;
	}

	// CREA F POR H
	private Matriz crearFporH() {
		Matriz mVectorVi = crearVectorFdeMi();
		Matriz mFdeH = mVectorVi.mult(mInhibicion);
		return mFdeH;
	}

	private Matriz calcularSensibilizadas() {
		Matriz mSensibilizadas = new Matriz(1, mIncidencia.getColCount());

		// Inicializa el vector de Sensibilizadas en 1
		for (int i = 0; i < mIncidencia.getColCount(); i++)
			mSensibilizadas.setDato(0, i, 1);

		// Recorre la matriz, hace las sumas y en caso que aux < 0 coloca 0 en
		// la matriz de sensibilizadas
		for (int i = 0; i < mIncidencia.getFilCount(); i++) {
			for (int j = 0; j < mIncidencia.getColCount(); j++) {
				if (mIncidencia.getVal(i, j) + mMarcadoActual.getVal(0, i) < 0) {
					mSensibilizadas.setDato(0, j, 0);
				}
			}
		}
		return mSensibilizadas;
	}

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
		Log.Instance().Escribir("RdP", "Marcado Actual: " + mMarcadoActual);
		Log.Instance().Escribir("RdP", "Transiciones Sensibilizadas: " + mSensibilizadas);
	}
}