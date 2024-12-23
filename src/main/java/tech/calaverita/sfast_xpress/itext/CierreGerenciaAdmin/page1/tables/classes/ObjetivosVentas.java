package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes;

import lombok.Data;

@Data
public class ObjetivosVentas {
    private int objetivo;
    private float actual;
    private int ventasNuevas;
    private int clientesNuevos;
    private int renovaciones;
    private int cantidadRenovaciones;
    private int total;
    private int cantidadTotal;

    public int getClientesNuevos() {
        return clientesNuevos;
    }

    public void setClientesNuevos(int clientesNuevos) {
        this.clientesNuevos = clientesNuevos;
    }

    public int getCantidadRenovaciones() {
        return cantidadRenovaciones;
    }

    public void setCantidadRenovaciones(int cantidadRenovaciones) {
        this.cantidadRenovaciones = cantidadRenovaciones;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public int getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(int objetivo) {
        this.objetivo = objetivo;
    }

    public float getActual() {
        return actual;
    }

    public void setActual(float actual) {
        this.actual = actual;
    }

    public int getVentasNuevas() {
        return ventasNuevas;
    }

    public void setVentasNuevas(int ventasNuevas) {
        this.ventasNuevas = ventasNuevas;
    }

    public int getRenovaciones() {
        return renovaciones;
    }

    public void setRenovaciones(int renovaciones) {
        this.renovaciones = renovaciones;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    // Getters y setters
}
