package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes;

import lombok.Data;

@Data
public class ClienteMetrica {
    private int anterior;
    private int actual;
    private int diferencia;

    public int getAnterior() {
        return anterior;
    }

    public void setAnterior(int anterior) {
        this.anterior = anterior;
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
