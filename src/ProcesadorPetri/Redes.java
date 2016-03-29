/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcesadorPetri;

import ProcesadorPetri.Matriz;
import Auxiliar.Ficheros;
import java.util.ArrayList;

/**
 * Esta clase se utiliza para ejecutar todas las acciones que involucran a la
 * red de Petri. Posee dos constructores: uno para cuando se usan redes sin
 * tiempo y otro para cuando se usan redes con tiempo. Si la red no posee
 * transiciones automáticas, solo hay qe enviarle el objeto nulo en el
 * constructor. Cada vez que se utiliza el método ejectar un disparo,
 * independientemente de si puede ejecutarse o no, se muestran en pantalla todos
 * los vectores de estado de la red.
 */
public class Redes {

    private int[][] iMarcadoInicial;
    private int[][] iIncidencia;
    private int[][] iH;
    private int[][] iTiempo;
    private int[][] iMarcadoActual;
    private int[][] iFdeH;
    private int[][] iAutomaticas;
    private boolean print = true;
    private boolean isTimetable = false;
    private ArrayList<Tiempos> lTiempos;

    public Redes(int[][] iMarcado, int[][] iIncidencia, int[][] iH, int[][] iTiempo, int[][] iAutomaticas) {
        this.iMarcadoActual = iMarcado;
        this.iMarcadoInicial = iMarcado;
        this.iIncidencia = iIncidencia;
        this.iH = iH;
        this.iAutomaticas = iAutomaticas;
        isTimetable = true;
        setTiempos(iTiempo);
        if (iAutomaticas != null) {
            verificarAutomaticas();
        }
        printEstados();
    }

    public Redes(int[][] iMarcado, int[][] iIncidencia, int[][] iH, int[][] iAutomaticas) {
        this.iMarcadoActual = iMarcado;
        this.iMarcadoInicial = iMarcado;
        this.iIncidencia = iIncidencia;
        this.iAutomaticas = iAutomaticas;
        this.iH = iH;
        isTimetable = false;
        if (iAutomaticas != null) {
            verificarAutomaticas();
        }
    }

    public boolean ejecutar(int[][] iDisparo, boolean editar) {
        Matriz oIncidencia = new Matriz(iIncidencia);
        Matriz oDisparo = new Matriz(iDisparo);
        Matriz oNuevoMarcado = new Matriz(iMarcadoInicial);
        Matriz oFdeH = crearFdeH();
        Matriz oMand = oDisparo.AND(oFdeH);
        int aux = 0;//si aux es 0 significa que no hay transición sensibilizada
        for (int i = 0; i < oMand.getFilCount(); i++) {
            for (int j = 0; j < oMand.getColCount(); j++) {
                aux = oMand.getVal(i, j) + aux;
            }
        }
        if (aux == 0) {
            if (print) {
                Ficheros.Instance().Escribir("RED", "El disparo: {" + oDisparo.toString() + "}, NO se puede ejecutar\n");
            }
            return false;
        }
        Matriz oIncidenciaxDisparo = (oIncidencia.mult(oMand.transpose())).transpose();
        oNuevoMarcado = oNuevoMarcado.plus(oIncidenciaxDisparo);
        //Recorrido del nuevo marcado en busca de números negativos
        for (int i = 0; i < oNuevoMarcado.getFilCount(); i++) {
            for (int j = 0; j < oNuevoMarcado.getColCount(); j++) {
                if ((oNuevoMarcado.getVal(i, j)) < 0) {
                    if (print) {
                        Ficheros.Instance().Escribir("RED", "El disparo: {" + oDisparo.toString() + "}, NO se puede ejecutar\n");
                    }
                    return false;
                }
            }
        }
        if (editar) {
            for (int i = 0; i < oNuevoMarcado.getFilCount(); i++) {
                for (int j = 0; j < oNuevoMarcado.getColCount(); j++) {
                    iMarcadoActual[i][j] = oNuevoMarcado.getVal(i, j);
                }
            }
            if (print) {
                Ficheros.Instance().Escribir("RED", "El disparo: {" + oDisparo.toString() + "}, se ejecuto\n");
            }
            printEstados();
        }
        return true;
    }

    public ArrayList<Integer> ejecutar(int[][] iDisparo) {
        Matriz oIncidencia = new Matriz(iIncidencia);
        Matriz oDisparo = new Matriz(iDisparo);
        Matriz oNuevoMarcado = new Matriz(iMarcadoInicial);
        Matriz oFdeH = crearFdeH();
        Matriz oMand;

        if (isTimetable) { //si es con tiempo, se agrega a la AND el vector temporal y las sensibilizadas
            verificarTiempos();
            Matriz oANDdisparoFdeH = new Matriz(oFdeH.getFilCount(), oFdeH.getColCount());
            Matriz oTiempo = new Matriz(iTiempo);
            oANDdisparoFdeH = oDisparo.AND(oFdeH);
            Matriz oANDTiempoSensibilizadas = getSensibilizadas().AND(oTiempo);
            oMand = oANDdisparoFdeH.AND(oANDTiempoSensibilizadas);
        } else {
            oMand = oDisparo.AND(oFdeH);     //sino no es con tiempo, la AND será con el disparo y la H   
        }
        int aux = 0;
        for (int i = 0; i < oMand.getFilCount(); i++) {
            for (int j = 0; j < oMand.getColCount(); j++) {
                aux = oMand.getVal(i, j) + aux;
            }
        }
        if (aux == 0) {
            if (print) {
                Ficheros.Instance().Escribir("RED", "El disparo: {" + oDisparo.toString() + "}, NO se puede ejecutar\n");
            }
            ArrayList<Integer> Vectores = new ArrayList<>();
            for (int k = 0; k < iDisparo[0].length; k++) {
                if (iDisparo[0][k] == 1) {
                    Vectores.add(k);
                }
            }
            return Vectores;
        }
        //Operaciones sobre matrices        
        Matriz oIncidenciaxDisparo = (oIncidencia.mult(oMand.transpose())).transpose();
        oNuevoMarcado = oNuevoMarcado.plus(oIncidenciaxDisparo);
        ///Recorrido del nuevo marcado en busca de números negativos
        for (int i = 0; i < oNuevoMarcado.getFilCount(); i++) {
            for (int j = 0; j < oNuevoMarcado.getColCount(); j++) {
                if ((oNuevoMarcado.getVal(i, j)) < 0) {
                    if (print) {
                        Ficheros.Instance().Escribir("RED", "El disparo: {" + oDisparo.toString() + "}, NO se puede ejecutar\n");
                    }
                    ArrayList<Integer> Vectores = new ArrayList<>();
                    for (int k = 0; k < iDisparo[0].length; k++) {
                        if (iDisparo[0][k] == 1) {
                            Vectores.add(k);
                        }
                    }
                    return Vectores;
                }
            }
        }
        for (int i = 0; i < oNuevoMarcado.getFilCount(); i++) {
            for (int j = 0; j < oNuevoMarcado.getColCount(); j++) {
                iMarcadoActual[i][j] = oNuevoMarcado.getVal(i, j);
            }
        }
        if (print) {
            Ficheros.Instance().Escribir("RED", "El disparo: {" + oDisparo.toString() + "}, SI se puede ejecutar\n");
        }
        if (isTimetable) {/*Verifico si alguna transicion que estaba sensibilizada por tiempo se
           ejecutó para resetear el contador de tiempo.*/
            int t = 0;
            for (int i = 0; i < iDisparo.length; i++) {
                for (int j = 0; j < iDisparo[i].length; j++) {
                    if (iDisparo[i][j] == 1) {
                        t = j;
                    }
                }
            }
            iTiempo[0][t] = 0;
            lTiempos.get(t).reset();
        }
        if (iAutomaticas != null) {
            verificarAutomaticas();
        }
        printEstados();
        return null;
    }

    public void setiTiempo(int[][] iTiempo) {
        this.iTiempo = iTiempo;
    }

    public int[][] getMarcadoInicial() {
        return iMarcadoInicial;
    }

    public int[][] getNuevoMarcado() {
        return iMarcadoActual;
    }

    public Matriz crearVectorVi() {
        Matriz oMarcado = new Matriz(iMarcadoInicial);
        Matriz oVectorVi = new Matriz(oMarcado.getFilCount(), oMarcado.getColCount());
        for (int i = 0; i < oMarcado.getFilCount(); i++) {
            for (int j = 0; j < oMarcado.getColCount(); j++) {
                if (oMarcado.getVal(i, j) == 0) {
                    oVectorVi.setDato(i, j, 0);
                } else {
                    oVectorVi.setDato(i, j, 1);
                }
            }
        }
        return oVectorVi;
    }

    public Matriz crearFdeH() {
        Matriz oVectorVi = crearVectorVi();//es igual a la de Marcado Inicial
        Matriz oH = new Matriz(iH);
        Matriz oFdeH = oVectorVi.mult(oH);
        Matriz aux = new Matriz(oFdeH.getFilCount(), oFdeH.getColCount());
        //Niego la matriz oFdeH
        for (int i = 0; i < aux.getFilCount(); i++) {
            for (int j = 0; j < aux.getColCount(); j++) {
                if (oFdeH.getVal(i, j) == 1) {
                    aux.setDato(i, j, 0);
                } else {
                    aux.setDato(i, j, 1);
                }
            }
        }
        return aux;//devuelvo la negacion de oFdeH
    }

    public Matriz getSensibilizadas() {
        int[][] iDisparoAux = new int[1][iIncidencia[0].length];
        int[][] iSensibilizadas = new int[1][iIncidencia[0].length];
        for (int i = 0; i < iSensibilizadas[0].length; i++) {
            iSensibilizadas[0][i] = 0;
            iDisparoAux[0][i] = 0;
        }
        for (int i = 0; i < iDisparoAux[0].length; i++) {
            iDisparoAux[0][i] = 1;
            if (ejecutar(iDisparoAux, false)) {
                iSensibilizadas[0][i] = 1;
            }
            iDisparoAux[0][i] = 0;
        }

        Matriz oSensibilizadas = new Matriz(iSensibilizadas);
        return oSensibilizadas;
    }

    public Matriz getSensibilizadasTemporales() {
        verificarTiempos();
        int[][] iDisparoAux = new int[1][iIncidencia[0].length];
        int[][] iSensibilizadas = new int[1][iIncidencia[0].length];
        for (int i = 0; i < iSensibilizadas[0].length; i++) {
            iSensibilizadas[0][i] = 0;
            iDisparoAux[0][i] = 0;
        }
        for (int i = 0; i < iDisparoAux[0].length; i++) {
            iDisparoAux[0][i] = 1;
            if (ejecutar(iDisparoAux, false)) {
                iSensibilizadas[0][i] = 1;
            }
            iDisparoAux[0][i] = 0;
        }

        Matriz oSensibilizadas = new Matriz(iSensibilizadas);
        Matriz AND = oSensibilizadas.AND(new Matriz(iTiempo));
        return AND;
    }

    public int[][] getTemporales() {
        verificarTiempos();
        return iTiempo;
    }

    public int getCantidadTransiciones() {
        Matriz oIncidencia = new Matriz(iIncidencia);
        return oIncidencia.getColCount();
    }

    public int[][] getiTiempo() {
        return iTiempo;
    }

    private void setTiempos(int[][] iTiempoIntervalo) {
        lTiempos = new ArrayList<>();
        iTiempo = new int[1][iTiempoIntervalo.length];//creo el iVector para devolver a la red.
        for (int i = 0; i < iTiempo[0].length; i++) {//seteo en 1 las transiciones sin tiempo.
            if (iTiempoIntervalo[i][1] == 0) {
                iTiempo[0][i] = 1;
            }
        }
        for (int i = 0; i < iTiempo[0].length; i++) {
            Tiempos oTiempos = new Tiempos(false, iTiempoIntervalo[i][0], iTiempoIntervalo[i][1]);
            lTiempos.add(oTiempos);
        }
        verificarTiempos();
    }

    public void verificarTiempos() {
        //verificar si una transicion deberia estar en un estado y no lo esta. En ese caso resetear el contador.
        ArrayList<Integer> lTransiciones = obtenerTransActiva(getSensibilizadas().getDato());
        for (int i = 0; i < lTransiciones.size(); i++) {
            Tiempos oTiempos = lTiempos.get(lTransiciones.get(i));
            if (!oTiempos.isActive()) {
                oTiempos.starts();
            } else if (oTiempos.isInTime()) {
                iTiempo[0][lTransiciones.get(i)] = 1;
            } else {
                iTiempo[0][lTransiciones.get(i)] = 0;
            }
        }
        for (int i = 0; i < lTiempos.size(); i++) {
            if (lTiempos.get(i).getiFinal() == 0) {
                iTiempo[0][i] = 1;
            }
        }
    }

    private ArrayList<Integer> obtenerTransActiva(int[][] resultado) {
        ArrayList<Integer> lTransiciones = new ArrayList<>();
        for (int i = 0; i < resultado.length; i++) {
            for (int j = 0; j < resultado[i].length; j++) {
                if (resultado[i][j] == 1) {
                    lTransiciones.add(j);
                }
            }
        }
        return lTransiciones;
    }

    private void verificarAutomaticas() {
        Matriz oSensibilizadas;
        if (isTimetable) {
            oSensibilizadas = getSensibilizadasTemporales();
        } else {
            oSensibilizadas = getSensibilizadas();
        }
        Matriz AND = oSensibilizadas.AND(new Matriz(iAutomaticas));
        while (Matriz.TransActive(AND.getDato()) != 0) {
            if (print) {
                System.out.println("Transciones Automaticas listas a disparar: " + AND.toString());
            }
            for (int i = 0; i < AND.getFilCount(); i++) {
                for (int j = 0; j < AND.getColCount(); j++) {
                    if (AND.getVal(i, j) == 1) {
                        //crear el disparo para dispararlo
                        int[][] iDisparo = new int[AND.getFilCount()][AND.getColCount()];
                        for (int k = 0; k < AND.getColCount(); k++) {
                            if (j == k) {
                                iDisparo[0][k] = 1;
                            } else {
                                iDisparo[0][k] = 0;
                            }
                        }
                        if (print) {
                            Ficheros.Instance().Escribir("RED", "Disparo Automatico:  " + new Matriz(iDisparo).toString());
                            Ficheros.Instance().Escribir("RED", "Marcado Actual:  " + new Matriz(iMarcadoActual).toString());
                        }
                        ejecutar(iDisparo, true);
                    }
                }
            }
            AND = getSensibilizadas().AND(new Matriz(iAutomaticas));
        }
    }

    private void printEstados() {
        if (print) {
            Ficheros.Instance().Escribir("RED", "PRINT ESTADOS");
            if (isTimetable) {
                verificarTiempos();
                Ficheros.Instance().Escribir("RED", "Vector Temporal es " + new Matriz(this.iTiempo).toString());
                Ficheros.Instance().Escribir("RED", "Transiciones Sensibilizadas Temporales: " + getSensibilizadasTemporales().toString());
            }
            if (iAutomaticas != null) {
                Ficheros.Instance().Escribir("RED", "Vector Automatico es " + new Matriz(iAutomaticas).toString());
            }
            Ficheros.Instance().Escribir("RED", "Marcado Actual es " + new Matriz(iMarcadoActual).toString());
            Ficheros.Instance().Escribir("RED", "Transiciones Sensibilizadas: " + getSensibilizadas().toString());
        }
    }
}
