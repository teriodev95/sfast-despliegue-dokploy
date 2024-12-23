package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes;

import java.util.List;

import lombok.Data;

@Data
public class TablaDetallesCierreAgencias {
    private String tituloReporte;
    private String empresa;
    private String semana;
    private String anio;
    private String gerente;
    private String zona;
    private String idEmpleado;
    private String fechaHora;
    private List<Agencia> agencias;
    private String totalAgencias;
    private String totalAgentes;

    public String getTituloReporte() {
        return tituloReporte;
    }

    public void setTituloReporte(String tituloReporte) {
        this.tituloReporte = tituloReporte;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getSemana() {
        return semana;
    }

    public void setSemana(String semana) {
        this.semana = semana;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getGerente() {
        return gerente;
    }

    public void setGerente(String gerente) {
        this.gerente = gerente;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public List<Agencia> getAgencias() {
        return agencias;
    }

    public void setAgencias(List<Agencia> agencias) {
        this.agencias = agencias;
    }

    public String getTotalAgencias() {
        return totalAgencias;
    }

    public void setTotalAgencias(String totalAgencias) {
        this.totalAgencias = totalAgencias;
    }

    public String getTotalAgentes() {
        return totalAgentes;
    }

    public void setTotalAgentes(String totalAgentes) {
        this.totalAgentes = totalAgentes;
    }

    public static class Agencia {
        private String id;
        private String agente;
        private int debito;
        private int cobPuro;
        private int faltante;
        private double eficiencia;
        private Integer ventas;
        private Cliente clientes;
        private Cliente noPagos;
        private Cliente pagosReducidos;
        private Cliente clientesConLiquidacion;

        // Getters y Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAgente() {
            return agente;
        }

        public void setAgente(String agente) {
            this.agente = agente;
        }

        public int getDebito() {
            return debito;
        }

        public void setDebito(int debito) {
            this.debito = debito;
        }

        public int getCobPuro() {
            return cobPuro;
        }

        public void setCobPuro(int cobPuro) {
            this.cobPuro = cobPuro;
        }

        public int getFaltante() {
            return faltante;
        }

        public void setFaltante(int faltante) {
            this.faltante = faltante;
        }

        public double getEficiencia() {
            return eficiencia;
        }

        public void setEficiencia(double eficiencia) {
            this.eficiencia = eficiencia;
        }

        public Integer getVentas() {
            return ventas;
        }

        public void setVentas(Integer ventas) {
            this.ventas = ventas;
        }

        public Cliente getClientes() {
            return clientes;
        }

        public void setClientes(Cliente clientes) {
            this.clientes = clientes;
        }

        public Cliente getNoPagos() {
            return noPagos;
        }

        public void setNoPagos(Cliente noPagos) {
            this.noPagos = noPagos;
        }

        public Cliente getPagosReducidos() {
            return pagosReducidos;
        }

        public void setPagosReducidos(Cliente pagosReducidos) {
            this.pagosReducidos = pagosReducidos;
        }

        public Cliente getClientesConLiquidacion() {
            return clientesConLiquidacion;
        }

        public void setClientesConLiquidacion(Cliente clientesConLiquidacion) {
            this.clientesConLiquidacion = clientesConLiquidacion;
        }
    }

    // Clase interna para Cliente
    public static class Cliente {
        private int sant;
        private int sact;

        // Getters y Setters
        public int getSant() {
            return sant;
        }

        public void setSant(int sant) {
            this.sant = sant;
        }

        public int getSact() {
            return sact;
        }

        public void setSact(int sact) {
            this.sact = sact;
        }
    }
}
