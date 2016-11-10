package auxiliar;

public class Matriz {

	private int[][] dato;

	public Matriz(int fil, int col) {
		dato = new int[fil][col];
	}

	public Matriz(int[][] dato) {
		this.dato = dato;
	}

	public int[][] getDato() {
		return dato;
	}

	public int getVal(int fil, int col) {
		return dato[fil][col];
	}

	public void setDato(int fil, int col, int dato) {
		this.dato[fil][col] = dato;
	}

	public int getFilCount() {
		return dato.length;
	}

	public int getColCount() {
		return dato[0].length;
	}

	// COLOCA CEROS EN TODOS LOS ELEMENTOS DE LA PATRIZ
	public void clear() {
		for (int i = 0; i < this.getFilCount(); i++) {
			for (int j = 0; j < this.getColCount(); j++) {
				this.setDato(i, j, 0);
			}
		}
	}

	// CREA Y DEVUELVE LA MATRIZ TRANSPUESTA DE LA MATRIZ QUE LO INVOCA
	public Matriz transpose() {
		Matriz A = new Matriz(this.getColCount(), this.getFilCount());
		for (int i = 0; i < this.getFilCount(); i++) {
			for (int j = 0; j < this.getColCount(); j++) {
				A.setDato(j, i, this.getDato()[i][j]);
			}
		}
		return A;
	}

	@Override
	// IMPRIME LA MATRIZ EN FILAS Y COLUMNAS
	public String toString() {
		String texto = "";
		for (int i = 0; i < this.getFilCount(); i++) {
			for (int j = 0; j < this.getColCount(); j++) {
				texto += " " + this.dato[i][j];
			}
			texto += " ";
		}
		texto += "";
		return texto;
	}

	// SUMA DOS MATRICES C = A + B
	public Matriz plus(Matriz B) {
		Matriz A = this;
		if (B.getFilCount() != A.getFilCount() || B.getColCount() != A.getColCount()) {
			throw new RuntimeException("Dimensiones incompatibles");
		}
		Matriz C = new Matriz(getFilCount(), getColCount());
		for (int i = 0; i < getFilCount(); i++) {
			for (int j = 0; j < getColCount(); j++) {
				C.setDato(i, j, A.getDato()[i][j] + B.getDato()[i][j]);
			}
		}
		return C;
	}

	// MULTIPLICA DOS MATRICES C = A * B
	public Matriz mult(Matriz B) {
		Matriz A = this;
		if (A.getColCount() != B.getFilCount()) {
			throw new RuntimeException("Dimensiones no compatibles.");
		}
		Matriz C = new Matriz(A.getFilCount(), B.getColCount());
		for (int i = 0; i < C.getFilCount(); i++) {
			for (int j = 0; j < C.getColCount(); j++)
				for (int k = 0; k < A.getColCount(); k++)
					C.setDato(i, j, C.getDato()[i][j] + (A.getDato()[i][k] * B.getDato()[k][j]));
		}
		return C;
	}

	public Matriz and(Matriz B) {
		Matriz A = this;
		Matriz oMand = new Matriz(B.getFilCount(), B.getColCount());

		for (int i = 0; i < A.getFilCount(); i++) {
			for (int j = 0; j < A.getColCount(); j++) {
				if (A.getVal(i, j) == 1 && B.getVal(i, j) == 1) {
					oMand.setDato(i, j, 1);
				} else {
					oMand.setDato(i, j, 0);
				}
			}
		}
		return oMand;
	}

	// SUMA EN UNA VARIABLE AUX EL CONTENIDO DE LA OPERACION
	public boolean esCero() {
		int aux = 0;

		for (int i = 0; i < this.getFilCount(); i++)
			for (int j = 0; j < this.getColCount(); j++)
				aux = this.getVal(i, j) + aux;

		return aux == 0;
	}

	public Matriz getFdeMi(Matriz A) {
		Matriz mVectorVi = new Matriz(A.getFilCount(), A.getColCount());
		for (int i = 0; i < A.getFilCount(); i++) {
			for (int j = 0; j < A.getColCount(); j++) {
				if (A.getVal(i, j) == 0) {
					mVectorVi.setDato(i, j, 0);
				} else {
					mVectorVi.setDato(i, j, 1);
				}
			}
		}
		return mVectorVi;
	}

	// DEVUELVE LA NEGACION DE LA MATRIZ ORIGINAL
	public Matriz negar() {
		Matriz Negacion = new Matriz(this.getFilCount(), this.getColCount());
		for (int i = 0; i < this.getFilCount(); i++) {
			for (int j = 0; j < this.getColCount(); j++) {
				if (this.getVal(i, j) == 1) {
					Negacion.setDato(i, j, 0);
				} else {
					Negacion.setDato(i, j, 1);
				}
			}
		}
		return Negacion;
	}
}
