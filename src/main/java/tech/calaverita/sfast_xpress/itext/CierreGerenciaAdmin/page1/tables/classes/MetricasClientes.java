package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes;

import lombok.Data;

@Data
public class MetricasClientes {
    private ClienteMetrica clientes;
    private ClienteMetrica noPagos;
    private ClienteMetrica pReducidos;
    private ClienteMetrica cltsLiquid;
    private int descuentosPorLiquidacion;

    public ClienteMetrica getClientes() {
        return clientes;
    }

    public void setClientes(ClienteMetrica clientes) {
        this.clientes = clientes;
    }

    public ClienteMetrica getNoPagos() {
        return noPagos;
    }

    public void setNoPagos(ClienteMetrica noPagos) {
        this.noPagos = noPagos;
    }

    public ClienteMetrica getpReducidos() {
        return pReducidos;
    }

    public void setpReducidos(ClienteMetrica pReducidos) {
        this.pReducidos = pReducidos;
    }

    public ClienteMetrica getCltsLiquid() {
        return cltsLiquid;
    }

    public void setCltsLiquid(ClienteMetrica cltsLiquid) {
        this.cltsLiquid = cltsLiquid;
    }

    public int getDescuentosPorLiquidacion() {
        return descuentosPorLiquidacion;
    }

    public void setDescuentosPorLiquidacion(int descuentosPorLiquidacion) {
        this.descuentosPorLiquidacion = descuentosPorLiquidacion;
    }

    // Getters y setters
}
