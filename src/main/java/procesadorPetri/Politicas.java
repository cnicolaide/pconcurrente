package procesadorPetri;

import auxiliar.Matriz;

public class Politicas {
	RdP oRed;
	Matriz oPolitica;

	public Politicas(RdP oRed) {
		this.oRed = oRed;
		oPolitica = new Matriz(1, oRed.getSesibilizadas().getColCount());
		setPrioridad();
	}

	private void setPrioridad() {
		int[] iPolitica = { 17, 18, 14, 1, 5, 19, 20, 21, 22, 23, 24, 25, 15, 16, 27, 26, 28, 19, 13, 11, 10, 12 };

		for (int i = 0; i < oRed.getSesibilizadas().getColCount(); i++) {
			oPolitica.setDato(0, i, iPolitica[i]);
		}
	}

	public int cual(Matriz m) {

		int mayor = 0;
		int pos = 0;

		for (int i = 0; i < oRed.getSesibilizadas().getColCount(); i++)
			if (m.getVal(0, i) == 1 && oPolitica.getVal(0, i) > mayor) {
				mayor = oPolitica.getVal(0, i);
				pos = i;
			}
		return pos;
	}
}
