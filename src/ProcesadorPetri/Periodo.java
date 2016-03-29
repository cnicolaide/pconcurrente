/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcesadorPetri;

import java.util.Date;

/**
 * Esta clase se utiliza para tomar los tiempos de las transiones temporizadas.
 * En el constructor se entrega un booleano que indica si la transicion ya está
 * activa, y dos enteros con los tiempos de inicio y final de la transición.
 */
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
//               if(oInicio.before(new Date(oInicio.getTime()+iFinal)))
                return true;
//               else
//                   return false;
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
