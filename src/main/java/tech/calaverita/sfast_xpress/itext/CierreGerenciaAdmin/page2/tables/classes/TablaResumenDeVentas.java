package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page2.tables.classes;

import java.util.List;

public class TablaResumenDeVentas {
    private String titulo;
    private String vendedor;
    private String zona;
    private int semana;
    private int anio;
    private Resumen resumen;
    private List<DetalleVenta> detalleVentas;

    public static class Resumen {
        private Clientes clientesNuevos;
        private Clientes renovaciones;
        private Clientes totalVentas;
        private double primerosPagos;

        public static class Clientes {
            private int cantidad;
            private double monto;

            // Getters y Setters
            public int getCantidad() {
                return cantidad;
            }

            public void setCantidad(int cantidad) {
                this.cantidad = cantidad;
            }

            public double getMonto() {
                return monto;
            }

            public void setMonto(double monto) {
                this.monto = monto;
            }
        }

        // Getters y Setters para Resumen
        public Clientes getClientesNuevos() {
            return clientesNuevos;
        }

        public void setClientesNuevos(Clientes clientesNuevos) {
            this.clientesNuevos = clientesNuevos;
        }

        public Clientes getRenovaciones() {
            return renovaciones;
        }

        public void setRenovaciones(Clientes renovaciones) {
            this.renovaciones = renovaciones;
        }

        public Clientes getTotalVentas() {
            return totalVentas;
        }

        public void setTotalVentas(Clientes totalVentas) {
            this.totalVentas = totalVentas;
        }

        public double getPrimerosPagos() {
            return primerosPagos;
        }

        public void setPrimerosPagos(double primerosPagos) {
            this.primerosPagos = primerosPagos;
        }
    }

    public static class DetalleVenta {
        private int numero;
        private String fecha;
        private String agente;
        private String nombreCliente;
        private String tipo;
        private String nivel;
        private String plazo;
        private double monto;
        private double primerPago;

        // Getters y Setters para DetalleVenta
        public int getNumero() {
            return numero;
        }

        public void setNumero(int numero) {
            this.numero = numero;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getAgente() {
            return agente;
        }

        public void setAgente(String agente) {
            this.agente = agente;
        }

        public String getNombreCliente() {
            return nombreCliente;
        }

        public void setNombreCliente(String nombreCliente) {
            this.nombreCliente = nombreCliente;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getNivel() {
            return nivel;
        }

        public void setNivel(String nivel) {
            this.nivel = nivel;
        }

        public String getPlazo() {
            return plazo;
        }

        public void setPlazo(String plazo) {
            this.plazo = plazo;
        }

        public double getMonto() {
            return monto;
        }

        public void setMonto(double monto) {
            this.monto = monto;
        }

        public double getPrimerPago() {
            return primerPago;
        }

        public void setPrimerPago(double primerPago) {
            this.primerPago = primerPago;
        }
    }

    // Getters y Setters para la clase principal ResumenDeVentas
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public int getSemana() {
        return semana;
    }

    public void setSemana(int semana) {
        this.semana = semana;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public Resumen getResumen() {
        return resumen;
    }

    public void setResumen(Resumen resumen) {
        this.resumen = resumen;
    }

    public List<DetalleVenta> getDetalleVentas() {
        return detalleVentas;
    }

    public void setDetalleVentas(List<DetalleVenta> detalleVentas) {
        this.detalleVentas = detalleVentas;
    }

}
