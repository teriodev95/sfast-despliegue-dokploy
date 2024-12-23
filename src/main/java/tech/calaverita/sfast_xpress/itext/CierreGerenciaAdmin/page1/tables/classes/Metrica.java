package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes;

import lombok.Data;

@Data
public class Metrica {
    private int semanaAnterior;
    private int actual;
    private int diferencia; // Asegúrate de que todos los valores de diferencia son del mismo tipo

    public int getSemanaAnterior() {
        return semanaAnterior;
    }

    public void setSemanaAnterior(int semanaAnterior) {
        this.semanaAnterior = semanaAnterior;
    }

    public int getActual() {
        return actual;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }

    public int getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(int diferencia) {
        this.diferencia = diferencia;
    }

    // Getters y setters
}
