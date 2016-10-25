package ProcesadorPetri;

import Auxiliar.Matriz;

public class Politicas {
	RdP oRed;
	Matriz oPolitica;

	public Politicas(RdP oRed) {
		this.oRed = oRed;
		oPolitica = new Matriz(1, oRed.getSesibilizadas().getColCount());
		setPrioridad();
	}

	private void setPrioridad() {

		int[] iPolitica = { 1, 2, 3, 15, 5, 6 };

		for (int i = 0; i < oRed.getSesibilizadas().getColCount(); i++) {
			oPolitica.setDato(0, i, iPolitica[i]);
		}
	}

	public int cual(Matriz m) {

		int mayor = 0;
		int pos = 0;

		for (int i = 0; i < oRed.getSesibilizadas().getColCount(); i++) {
			if (m.getVal(0, i) == 1) {
				if (oPolitica.getVal(0, i) > mayor) {
					mayor = oPolitica.getVal(0, i);
					pos = i;
				}
			}
		}
		return pos;
	}
}
