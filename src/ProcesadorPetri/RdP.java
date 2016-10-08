package ProcesadorPetri;

import Auxiliar.Log;
import Auxiliar.Matriz;

public class RdP {

	private Matriz mMarcadoInicial;
	private Matriz mIncidencia;
	private Matriz mInhibidor;
	private Matriz mMarcadoActual;
	private Matriz mFdeH;
	private boolean print = true;

	// CONSTRUCTOR DE LA RED DE PETRI
	public RdP(int[][] iMarcado, int[][] iIncidencia, int[][] iInhibidor, int[][] iAutomaticas) {
		mMarcadoActual = new Matriz(iMarcado);
		mMarcadoInicial = new Matriz(iMarcado);
		mIncidencia = new Matriz(iIncidencia);
		mInhibidor = new Matriz(iInhibidor);
	}

	// DISPARA UNA TRANSICION DE LA RED DE PETRI
	public boolean ejecutar(int[][] iDisparo, boolean memoria) {
		Matriz mDisparo = new Matriz(iDisparo);
		mFdeH = crearFporH();
		Matriz mNuevoMarcado = mMarcadoInicial;
		Matriz mMand = mDisparo.AND(mFdeH);

		// Verfica que que la operacion d.AND*(!(F*H)) no sea cero. Si aux es 0
		// significa que no hay transicion sensibilizada.
		if (mMand.ANDporCERO()) {
			if (print) {
				Log.Instance().Escribir("RED", "NO se puede ejecutar el disparo: " + mDisparo.toString());
			}
			return false;
		}

		// Continuo operando
		Matriz mIncidenciaxDisparo = (mIncidencia.mult(mMand.transpose())).transpose();
		mNuevoMarcado = mNuevoMarcado.plus(mIncidenciaxDisparo);

		// Me fijo si es posible el disparo
		if (!mNuevoMarcado.isPos()) {
			if (print) {
				Log.Instance().Escribir("RED", "NO se puede ejecutar el disparo: " + mDisparo.toString());
			}
			return false;
		}

		if (memoria) {
			mMarcadoActual = mNuevoMarcado;

			if (print) {
				Log.Instance().Escribir("RED", "Se ejecuto el disparo: " + mDisparo.toString());
			}
			printEstados();
		}
		return true;
	}

	public Matriz getMarcadoInicial() {
		return mMarcadoInicial;
	}

	public int[][] getNuevoMarcado() {
		return mMarcadoActual.getDato();
	}

	public Matriz crearVectorVi() {
		Matriz mMarcado = mMarcadoInicial;
		Matriz mVectorVi = new Matriz(mMarcado.getFilCount(), mMarcado.getColCount());
		for (int i = 0; i < mMarcado.getFilCount(); i++) {
			for (int j = 0; j < mMarcado.getColCount(); j++) {
				if (mMarcado.getVal(i, j) == 0) {
					mVectorVi.setDato(i, j, 0);
				} else {
					mVectorVi.setDato(i, j, 1);
				}
			}
		}
		return mVectorVi;
	}

	public Matriz crearFporH() {
		Matriz oVectorVi = crearVectorVi();// es igual a la de Marcado Inicial
		Matriz oH = mInhibidor;
		Matriz oFdeH = oVectorVi.mult(oH);
		Matriz aux = new Matriz(oFdeH.getFilCount(), oFdeH.getColCount());

		// Niego la matriz oFdeH
		for (int i = 0; i < aux.getFilCount(); i++) {
			for (int j = 0; j < aux.getColCount(); j++) {
				if (oFdeH.getVal(i, j) == 1) {
					aux.setDato(i, j, 0);
				} else {
					aux.setDato(i, j, 1);
				}
			}
		}
		return aux;// devuelvo la negacion de oFdeH
	}

	public Matriz getSensibilizadas() {
		Matriz mDisparoAux = new Matriz(1, mIncidencia.getColCount());
		Matriz mSensibilizadas = new Matriz(1, mIncidencia.getColCount());
		for (int i = 0; i < mSensibilizadas.getColCount(); i++) {
			mSensibilizadas.setDato(0, i, 0);
			mDisparoAux.setDato(0, i, 0);
		}
		for (int i = 0; i < mDisparoAux.getColCount(); i++) {
			mDisparoAux.setDato(0, i, 1);
			if (ejecutar(mDisparoAux.getDato(), false)) {
				mSensibilizadas.setDato(0, i, 1);
			}
			mDisparoAux.setDato(0, i, 0);
		}

		return mSensibilizadas;
	}

	public int getCantidadTransiciones() {
		return mIncidencia.getColCount();
	}

	private void printEstados() {
		if (print) {
			Log.Instance().Escribir("RED", "PRINT ESTADOS");
			Log.Instance().Escribir("RED", "Marcado Actual es " + mMarcadoActual.toString());
			Log.Instance().Escribir("RED", "Transiciones Sensibilizadas: " + getSensibilizadas().toString());
		}
	}
}
