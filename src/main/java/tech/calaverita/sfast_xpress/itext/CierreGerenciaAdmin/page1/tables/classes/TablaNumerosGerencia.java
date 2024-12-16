package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes;

public class TablaNumerosGerencia {
    private String titulo;
    private MetricasFinancieras metricasFinancieras;
    private MetricasClientes metricasClientes;
    private ObjetivosVentas objetivosVentas;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public MetricasFinancieras getMetricasFinancieras() {
        return metricasFinancieras;
    }

    public void setMetricasFinancieras(MetricasFinancieras metricasFinancieras) {
        this.metricasFinancieras = metricasFinancieras;
    }

    public MetricasClientes getMetricasClientes() {
        return metricasClientes;
    }

    public void setMetricasClientes(MetricasClientes metricasClientes) {
        this.metricasClientes = metricasClientes;
    }

    public ObjetivosVentas getObjetivosVentas() {
        return objetivosVentas;
    }

    public void setObjetivosVentas(ObjetivosVentas objetivosVentas) {
        this.objetivosVentas = objetivosVentas;
    }

    // Getters y setters

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

}
