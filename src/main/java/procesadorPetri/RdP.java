package procesadorPetri;

import auxiliar.Log;
import auxiliar.Matriz;

public class RdP {
	private Matriz mMarcadoInicial, mMarcadoActual, mIncidencia, mInhibicion, mSensibilizadas, mTiempo;
	private long tiempoSensibilizada;
	private long[] timestamp;
	private int ventana, sensibilizada, primeraVez = 0;

	// CONSTRUCTOR DE LA RED DE PETRI
	public RdP(Matriz mMarcado, Matriz mIncidencia, Matriz mInhibicion, Matriz mTiempo) {
		this.mMarcadoActual = mMarcado;
		this.mMarcadoInicial = mMarcado;
		this.mIncidencia = mIncidencia;
		this.mInhibicion = mInhibicion;
		this.mTiempo = mTiempo;
		timestamp = new long[mIncidencia.getColCount()];
		mSensibilizadas = new Matriz(1, mIncidencia.getColCount());
		mSensibilizadas = calcularSensibilizadas();
		Log.getInstance().escribir("RdP", " ***** SE INSTANCIO LA RED DE PETRI *****", true);
	}

	// DISPARA UNA TRANSICION DE LA RED DE PETRI UTILIZANDO LA FORMULA: Mi+1 =
	// Mi+I*d.AND*(!(F*H))
	public boolean disparar(int posicion) {

		// Se fija si la transicion tiene tiene o no tiempo
		ventana = calcularVentanaTiempo(posicion);
		sensibilizada = mSensibilizadas.getVal(0, posicion);

		// Transforma la posicion que recibe en un vector de disparo
		Matriz mDisparo = crearVectorDisparo(posicion);

		// Verifica que la transicion que le pasan este sensibilizada, si esta y
		// es automatica, dispara.
		if (sensibilizada == 1 && ventana == 0) {

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
			printEstados(ventana, sensibilizada, posicion);
			return true;
		}

		// Imprime los estados y retorna FALSE
		printEstados(ventana, sensibilizada, posicion);
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

	private Matriz calcularSensibilizadas() {
		tiempoSensibilizada = System.currentTimeMillis();

		// Inicializa el vector de Sensibilizadas en 1
		for (int i = 0; i < mIncidencia.getColCount(); i++) {
			mSensibilizadas.setDato(0, i, 1);
			if (primeraVez == 0)
				timestamp[i] = tiempoSensibilizada;
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
					// Si no esta sensibilizada coloca tiempo 0
					timestamp[j] = 0;
				}
			}
		}

		// Si esta sensibilizada, y no tiene tiempo 0 seteado, actualiza el
		// tiempo
		for (int i = 0; i < mIncidencia.getColCount(); i++) {
			if (mSensibilizadas.getVal(0, i) == 1 && timestamp[i] == 0) {
				timestamp[i] = tiempoSensibilizada;
			}
		}

		primeraVez = 1;
		return mSensibilizadas;
	}

	private int calcularVentanaTiempo(int disparo) {

		long tiempoActual = System.currentTimeMillis();

		// Pregunta si la transicion es temporizada o no
		if (mTiempo.getVal(disparo, 0) == 0 && mTiempo.getVal(disparo, 1) == 0) {
			return 0;
		}

		// Pregunta si el tiempo transcurrido esta entre alfa y beta
		else if ((mTiempo.getVal(disparo, 0) < (tiempoActual - timestamp[disparo]))
				&& (mTiempo.getVal(disparo, 1) > (tiempoActual - timestamp[disparo]))) {
			return 0;
		}

		// Pregunta si el tiempo transcurrido es menor que alfa
		else if (mTiempo.getVal(disparo, 0) > (tiempoActual - timestamp[disparo])) {
			return (int) (mTiempo.getVal(disparo, 0) - ((tiempoActual - timestamp[disparo])));
		}

		return -1;
	}

	// DEVUELVE MATRIZ CON TRANSICIONES SENSIBILIZADAS
	public Matriz getSesibilizadas() {
		return mSensibilizadas;
	}

	// DEVUELVE MATRIZ CON EL NUEVO MARCADO
	public Matriz getMarcadoActual() {
		return mMarcadoActual;
	}

	// DEVUELVE VENTANA DE TIEMPO EN TRANSICIONES TEMPORIZADAS
	public int getVentana() {
		return ventana;
	}

	// IMPRIME LOS ESTADOS DE LA RED EN CIERTO MOMENTO
	private void printEstados(int ventana, int sensibilizada, int posicion) {
		String resultado = " ---- Este caso no fue tenido en cuenta, verificar ---- ";

		synchronized (this) {
			Log.getInstance().escribir("Hilo", Thread.currentThread().getName(), true);
			// System.err.println(Thread.currentThread().getName());

			if (ventana == 0 && sensibilizada == 1)
				resultado = "Se ejecuto el disparo T";
			if (ventana == 0 && sensibilizada == 0)
				resultado = "No se puede ejecutar el disparo, por no estar sensibilizada T";
			if (ventana == -1 && sensibilizada == 0)
				resultado = "No se puede ejecutar el disparo, por ser temporizada y no estar sensibilizada T";
			if (ventana == -1 && sensibilizada == 1)
				resultado = "No se puede ejecutar el disparo, por ser temporizada y llegar despues del intervalo T";
			if (ventana > 0 && sensibilizada == 0)
				resultado = "No se puede ejecutar el disparo, por llegar " + ventana
						+ " antes pero no estar sensibilizada T";
			if (ventana > 0 && sensibilizada == 1)
				resultado = "No se puede ejecutar el disparo, por llegar " + ventana + " antes y estar sensibilizada T";

			Log.getInstance().escribir("RdP", "Resultado: " + resultado + posicion, false);
			Log.getInstance().escribir("RdP", "Marcado Actual: " + mMarcadoActual, false);
			Log.getInstance().escribir("RdP", "Trans. Sensibi: " + mSensibilizadas + "\n", false);
		}
	}
}