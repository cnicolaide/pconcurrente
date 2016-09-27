package ProcesadorPetri;

import java.util.Date;

public class Periodo {

	private Date oInicio;
	private int iInicio;
	private int iFinal;

	public Periodo(boolean bActivate, int iInicio, int iFinal) {
		this.iFinal = iFinal;
		this.iInicio = iInicio;
		if (bActivate) {
			oInicio = new Date();
		}
	}

	public boolean isActive() {
		return oInicio != null;
	}

	public boolean isInTime() {
		if (isActive()) {
			if (new Date().getTime() - oInicio.getTime() > iInicio) {
				// if(oInicio.before(new Date(oInicio.getTime()+iFinal)))
				return true;
				// else
				// return false;
			} else {
				return false;
			}
		}
		return isActive();
	}

	public void starts() {
		if (oInicio == null) {
			oInicio = new Date();
		} else {
			System.out.println("TIEMPO YA INICIADO ANTERIORMENTE");
		}
	}

	public void reset() {
		oInicio = null;
	}

	public int getiInicio() {
		return iInicio;
	}

	public int getiFinal() {
		return iFinal;
	}
}
