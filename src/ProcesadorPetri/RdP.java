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
	public boolean ejecutar(Matriz mDisparo, boolean memoria) {

		// Crea la matriz (!(F*H))
		mFdeH = crearFporH().Negacion();

		Matriz mNuevoMarcado = mMarcadoInicial;

		// Crea la matriz d.AND*(!(F*H))
		Matriz mDandFporHNegado = mDisparo.AND(mFdeH);

		// Verfica que que la operacion d.AND*(!(F*H)) no sea cero
		if (mDandFporHNegado.ANDporCERO()) {
			Log.Instance().Escribir("RED", "NO se puede ejecutar el disparo: " + mDisparo.toString());
			return false;
		}

		// Crea la matriz I*d.AND*(!(F*H))
		Matriz mIncidenciaxDisparo = (mIncidencia.mult(mDandFporHNegado.transpose())).transpose();

		// Almacena en la matriz nuevo marcado el resultado de
		// Mi+I*d.AND*(!(F*H))
		mNuevoMarcado = mNuevoMarcado.plus(mIncidenciaxDisparo);

		// Se fija si fue valido el disparo, en caso de que no imprimi mensaje
		if (!mNuevoMarcado.isPos()) {
			Log.Instance().Escribir("RED", "NO se puede ejecutar el disparo: " + mDisparo.toString());
			return false;
		}

		// Guarda el nuevo estado de la red e imprime los estados
		// if (memoria) {
		mMarcadoActual = mNuevoMarcado;
		Log.Instance().Escribir("RED", "Se ejecuto el disparo: " + mDisparo.toString());
		printEstados();
		// }
		return true;
	}

	// CREA EL VECTOR F(Mi)
	public Matriz crearVectorFdeMi() {
		Matriz mVectorVi = new Matriz(mMarcadoInicial.getFilCount(), mMarcadoInicial.getColCount());
		return mVectorVi.FdeMi(mMarcadoInicial);
	}

	// CREA F POR H
	public Matriz crearFporH() {
		Matriz mVectorVi = crearVectorFdeMi();
		Matriz mFdeH = mVectorVi.mult(mInhibicion);
		return mFdeH;
	}

	// DEVUELVE CUALES SON LAS TRANSICIONES SENSIBILIZADAS
	public Matriz getSensibilizadas() {
//		Matriz bup = mMarcadoActual;
		Matriz mDisparoAux = new Matriz(1, mIncidencia.getColCount());
		Matriz mSensibilizadas = new Matriz(1, mIncidencia.getColCount());

		mSensibilizadas.Clear();
		mDisparoAux.Clear();

		for (int i = 0; i < mDisparoAux.getColCount(); i++) {
			mDisparoAux.setDato(0, i, 1);
//			mMarcadoActual = bup;

			if (ejecutar(mDisparoAux, true)) {
				mSensibilizadas.setDato(0, i, 1);
			}
//			bup = mMarcadoActual;
			mDisparoAux.Clear();
		}

		Log.Instance().Escribir("RED", "Transiciones Sensibilizadas: " + mSensibilizadas.toString());

		return mSensibilizadas;
	}

	// DEVUELVE EL MARCADO INICIAL
	public Matriz getMarcadoInicial() {
		return mMarcadoInicial;
	}

	// DEVUELVE EL NUEVO MARCADO
	public int[][] getNuevoMarcado() {
		return mMarcadoActual.getDato();
	}

	// DEVUELVE LA CANTIDAD DE TRANSCICIONES QUE TIENE LA RED
	public int getCantidadTransiciones() {
		return mIncidencia.getColCount();
	}

	// IMPRIME LOS ESTADOS DE LA RED EN CIERTO MOMENTO
	private void printEstados() {
		// Log.Instance().Escribir("RED", "PRINT ESTADOS");
		Log.Instance().Escribir("RED", "Marcado Actual es " + mMarcadoActual.toString());
		// Log.Instance().Escribir("RED", "Transiciones Sensibilizadas: " +
		// getSensibilizadas().toString());
	}
}