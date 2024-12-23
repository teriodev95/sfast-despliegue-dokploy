package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes;

import lombok.Data;

@Data
public class MetricasFinancieras {
    private Metrica debit;
    private Metrica cobPura;
    private Metrica faltante;
    private Metrica eficiencia;

    public Metrica getDebit() {
        return debit;
    }

    public void setDebit(Metrica debit) {
        this.debit = debit;
    }

    public Metrica getCobPura() {
        return cobPura;
    }

    public void setCobPura(Metrica cobPura) {
        this.cobPura = cobPura;
    }

    public Metrica getFaltante() {
        return faltante;
    }

    public void setFaltante(Metrica faltante) {
        this.faltante = faltante;
    }

    public Metrica getEficiencia() {
        return eficiencia;
    }

    public void setEficiencia(Metrica eficiencia) {
        this.eficiencia = eficiencia;
    }

    // Getters y setters
}
