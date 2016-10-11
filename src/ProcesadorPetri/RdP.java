package ProcesadorPetri;

import Auxiliar.Log;
import Auxiliar.Matriz;

public class RdP {

	private Matriz mMarcadoInicial;
	private Matriz mMarcadoActual;
	private Matriz mIncidencia;
	private Matriz mInhibicion;
	private Matriz mFdeH;

	// CONSTRUCTOR DE LA RED DE PETRI
	public RdP(Matriz mMarcado, Matriz mIncidencia, Matriz mInhibicion) {
		this.mMarcadoActual = mMarcado;
		this.mMarcadoInicial = mMarcado;
		this.mIncidencia = mIncidencia;
		this.mInhibicion = mInhibicion;
	}

	// DISPARA UNA TRANSICION DE LA RED DE PETRI UTILIZANDO LA FORMULA: Mi+1 =
	// Mi+I*d.AND*(!(F*H))
	public boolean ejecutar(int posicion, boolean memoria) {

		// Transforma la posicion que recibe en un vector de disparo
		Matriz mDisparo = crearVectorDisparo(posicion);

		// Carga en el marcado actual, los valores iniciales para operar
		mMarcadoActual = mMarcadoInicial;

		// Crea la matriz (!(F*H))
		mFdeH = crearFporH().Negacion();

		// Crea la matriz d.AND*(!(F*H))
		Matriz mDandFporHNegado = mDisparo.AND(mFdeH);

		// Verfica que que la operacion d.AND*(!(F*H)) no sea cero
		if (mDandFporHNegado.ANDporCERO()) {
			if (memoria)
				Log.Instance().Escribir("RdP", "No se puede ejecutar el disparo: " + mDisparo.toString());
			return false;
		}

		// Crea la matriz I*d.AND*(!(F*H))
		Matriz mIncidenciaxDisparo = (mIncidencia.mult(mDandFporHNegado.transpose())).transpose();

		// Almacena en la matriz nuevo marcado el resultado de
		// Mi+I*d.AND*(!(F*H))
		mMarcadoActual = mMarcadoActual.plus(mIncidenciaxDisparo);

		// Se fija si fue valido el disparo, en caso de que no imprimi mensaje
		if (!mMarcadoActual.isPos()) {
			if (memoria)
				Log.Instance().Escribir("RdP", "No se puede ejecutar el disparo: " + mDisparo.toString());
			return false;
		}

		// Si esta activado el flag, guarda el nuevo estado de la red para
		// continuar operando. Si no esta activado, es porque se esta usando
		// para obtener las transiciones sensibilizadas
		if (memoria) {
			mMarcadoInicial = mMarcadoActual;
			Log.Instance().Escribir("RdP", "Se ejecuto el disparo: " + mDisparo.toString());
			printEstados();
		} else {
			mMarcadoActual = mMarcadoInicial;
		}
		return true;
	}

	// CREA EL VECTOR F(Mi)
	public Matriz crearVectorFdeMi() {
		Matriz mVectorVi = new Matriz(mMarcadoActual.getFilCount(), mMarcadoActual.getColCount());
		return mVectorVi.FdeMi(mMarcadoActual);
	}

	// CREA EL VECTOR DE DISPARO
	public Matriz crearVectorDisparo(int pos) {
		Matriz mDisparo = new Matriz(1, mIncidencia.getColCount());
		mDisparo.Clear();
		mDisparo.setDato(0, pos, 1);
		return mDisparo;
	}

	// CREA F POR H
	public Matriz crearFporH() {
		Matriz mVectorVi = crearVectorFdeMi();
		Matriz mFdeH = mVectorVi.mult(mInhibicion);
		return mFdeH;
	}

	// DEVUELVE CUALES SON LAS TRANSICIONES SENSIBILIZADAS
	public Matriz getSensibilizadas() {
		Matriz mSensibilizadas = new Matriz(1, mIncidencia.getColCount());

		// Crea los vectores de prueba en cero
		mSensibilizadas.Clear();

		// Prueba cada una de las transiciones, llamando al metodo ejecutar con
		// el flag de memoria en false asi este no guarda el nuevo estado en
		// caso de estar sensibilizada
		for (int i = 0; i < mIncidencia.getColCount(); i++) {
			if (ejecutar(i, false)) {
				mSensibilizadas.setDato(0, i, 1);
			}
		}

		Log.Instance().Escribir("RdP", "Transiciones Sensibilizadas: " + mSensibilizadas.toString());

		return mSensibilizadas;
	}

	// DEVUELVE EL MARCADO INICIAL
	public Matriz getMarcadoInicial() {
		return mMarcadoInicial;
	}

	// DEVUELVE EL NUEVO MARCADO
	public Matriz getNuevoMarcado() {
		return mMarcadoActual;
	}

	// DEVUELVE LA CANTIDAD DE TRANSCICIONES QUE TIENE LA RED
	public int getCantidadTransiciones() {
		return mIncidencia.getColCount();
	}

	// IMPRIME LOS ESTADOS DE LA RED EN CIERTO MOMENTO
	private void printEstados() {
		Log.Instance().Escribir("RdP", "Marcado Actual: " + mMarcadoActual.toString());
	}
}