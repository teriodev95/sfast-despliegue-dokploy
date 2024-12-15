package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables.classes;

import java.util.List;

public class TablaFlujoEfectivo {

    private String titulo;
    private String gerente;
    private String zona;
    private int semana;
    private int anio;
    private CierreSemanalCobranzaAgencias cierreSemanalCobranzaAgencias;
    private ComisionesYAsignaciones comisionesYAsignaciones;
    private Tabulador tabulador;
    private Resumen resumen;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public CierreSemanalCobranzaAgencias getCierreSemanalCobranzaAgencias() {
        return cierreSemanalCobranzaAgencias;
    }

    public void setCierreSemanalCobranzaAgencias(CierreSemanalCobranzaAgencias cierreSemanalCobranzaAgencias) {
        this.cierreSemanalCobranzaAgencias = cierreSemanalCobranzaAgencias;
    }

    public ComisionesYAsignaciones getComisionesYAsignaciones() {
        return comisionesYAsignaciones;
    }

    public void setComisionesYAsignaciones(ComisionesYAsignaciones comisionesYAsignaciones) {
        this.comisionesYAsignaciones = comisionesYAsignaciones;
    }

    public Tabulador getTabulador() {
        return tabulador;
    }

    public void setTabulador(Tabulador tabulador) {
        this.tabulador = tabulador;
    }

    public Resumen getResumen() {
        return resumen;
    }

    public void setResumen(Resumen resumen) {
        this.resumen = resumen;
    }

    public static class CierreSemanalCobranzaAgencias {
        private List<AgenteCobranza> agentes;
        private TotalesCobranza totales;

        public List<AgenteCobranza> getAgentes() {
            return agentes;
        }

        public void setAgentes(List<AgenteCobranza> agentes) {
            this.agentes = agentes;
        }

        public TotalesCobranza getTotales() {
            return totales;
        }

        public void setTotales(TotalesCobranza totales) {
            this.totales = totales;
        }

        public static class AgenteCobranza {
            private String nombre;
            private String agencia;
            private int noClientes;
            private int noPagos;
            private int noClientesConLiquidacion;
            private double cobranzaPura;
            private double liquidaciones;
            private double montoExcedente;
            private double multas;

            public String getNombre() {
                return nombre;
            }

            public void setNombre(String nombre) {
                this.nombre = nombre;
            }

            public String getAgencia() {
                return agencia;
            }

            public void setAgencia(String agencia) {
                this.agencia = agencia;
            }

            public int getNoClientes() {
                return noClientes;
            }

            public void setNoClientes(int noClientes) {
                this.noClientes = noClientes;
            }

            public int getNoPagos() {
                return noPagos;
            }

            public void setNoPagos(int noPagos) {
                this.noPagos = noPagos;
            }

            public int getNoClientesConLiquidacion() {
                return noClientesConLiquidacion;
            }

            public void setNoClientesConLiquidacion(int noClientesConLiquidacion) {
                this.noClientesConLiquidacion = noClientesConLiquidacion;
            }

            public double getCobranzaPura() {
                return cobranzaPura;
            }

            public void setCobranzaPura(double cobranzaPura) {
                this.cobranzaPura = cobranzaPura;
            }

            public double getLiquidaciones() {
                return liquidaciones;
            }

            public void setLiquidaciones(double liquidaciones) {
                this.liquidaciones = liquidaciones;
            }

            public double getMontoExcedente() {
                return montoExcedente;
            }

            public void setMontoExcedente(double montoExcedente) {
                this.montoExcedente = montoExcedente;
            }

            public double getMultas() {
                return multas;
            }

            public void setMultas(double multas) {
                this.multas = multas;
            }

        }

        public static class TotalesCobranza {
            private int noClientes;
            private int noPagos;
            private int noClientesConLiquidacion;
            private double cobranzaPura;
            private double liquidaciones;
            private double montoExcedente;
            private double multas;

            public int getNoClientes() {
                return noClientes;
            }

            public void setNoClientes(int noClientes) {
                this.noClientes = noClientes;
            }

            public int getNoPagos() {
                return noPagos;
            }

            public void setNoPagos(int noPagos) {
                this.noPagos = noPagos;
            }

            public int getNoClientesConLiquidacion() {
                return noClientesConLiquidacion;
            }

            public void setNoClientesConLiquidacion(int noClientesConLiquidacion) {
                this.noClientesConLiquidacion = noClientesConLiquidacion;
            }

            public double getCobranzaPura() {
                return cobranzaPura;
            }

            public void setCobranzaPura(double cobranzaPura) {
                this.cobranzaPura = cobranzaPura;
            }

            public double getLiquidaciones() {
                return liquidaciones;
            }

            public void setLiquidaciones(double liquidaciones) {
                this.liquidaciones = liquidaciones;
            }

            public double getMontoExcedente() {
                return montoExcedente;
            }

            public void setMontoExcedente(double montoExcedente) {
                this.montoExcedente = montoExcedente;
            }

            public double getMultas() {
                return multas;
            }

            public void setMultas(double multas) {
                this.multas = multas;
            }

        }
    }

    public static class ComisionesYAsignaciones {
        private List<AgenteComision> agentes;
        private TotalesComisiones totales;

        public List<AgenteComision> getAgentes() {
            return agentes;
        }

        public void setAgentes(List<AgenteComision> agentes) {
            this.agentes = agentes;
        }

        public TotalesComisiones getTotales() {
            return totales;
        }

        public void setTotales(TotalesComisiones totales) {
            this.totales = totales;
        }

        public static class AgenteComision {
            private String nombre;
            private String agencia;
            private double asignaciones;
            private double comisionesPorCobranza;
            private double comisionesPorVentas;
            private double bonos;
            private double descPorLiquidacion;
            private double total;

            public String getNombre() {
                return nombre;
            }

            public void setNombre(String nombre) {
                this.nombre = nombre;
            }

            public String getAgencia() {
                return agencia;
            }

            public void setAgencia(String agencia) {
                this.agencia = agencia;
            }

            public double getAsignaciones() {
                return asignaciones;
            }

            public void setAsignaciones(double asignaciones) {
                this.asignaciones = asignaciones;
            }

            public double getComisionesPorCobranza() {
                return comisionesPorCobranza;
            }

            public void setComisionesPorCobranza(double comisionesPorCobranza) {
                this.comisionesPorCobranza = comisionesPorCobranza;
            }

            public double getComisionesPorVentas() {
                return comisionesPorVentas;
            }

            public void setComisionesPorVentas(double comisionesPorVentas) {
                this.comisionesPorVentas = comisionesPorVentas;
            }

            public double getBonos() {
                return bonos;
            }

            public void setBonos(double bonos) {
                this.bonos = bonos;
            }

            public double getDescPorLiquidacion() {
                return descPorLiquidacion;
            }

            public void setDescPorLiquidacion(double descPorLiquidacion) {
                this.descPorLiquidacion = descPorLiquidacion;
            }

            public double getTotal() {
                return total;
            }

            public void setTotal(double total) {
                this.total = total;
            }

        }

        public static class TotalesComisiones {
            private double asignaciones;
            private double comisionesPorCobranza;
            private double comisionesPorVentas;
            private double bonos;
            private double descPorLiquidacion;
            private double total;

            public double getAsignaciones() {
                return asignaciones;
            }

            public void setAsignaciones(double asignaciones) {
                this.asignaciones = asignaciones;
            }

            public double getComisionesPorCobranza() {
                return comisionesPorCobranza;
            }

            public void setComisionesPorCobranza(double comisionesPorCobranza) {
                this.comisionesPorCobranza = comisionesPorCobranza;
            }

            public double getComisionesPorVentas() {
                return comisionesPorVentas;
            }

            public void setComisionesPorVentas(double comisionesPorVentas) {
                this.comisionesPorVentas = comisionesPorVentas;
            }

            public double getBonos() {
                return bonos;
            }

            public void setBonos(double bonos) {
                this.bonos = bonos;
            }

            public double getDescPorLiquidacion() {
                return descPorLiquidacion;
            }

            public void setDescPorLiquidacion(double descPorLiquidacion) {
                this.descPorLiquidacion = descPorLiquidacion;
            }

            public double getTotal() {
                return total;
            }

            public void setTotal(double total) {
                this.total = total;
            }

        }
    }

    public static class Tabulador {
        private List<Billete> billetes;
        private double totalEnBilletes;
        private List<Moneda> monedas;
        private double totalEnMonedas;

        public List<Billete> getBilletes() {
            return billetes;
        }

        public void setBilletes(List<Billete> billetes) {
            this.billetes = billetes;
        }

        public double getTotalEnBilletes() {
            return totalEnBilletes;
        }

        public void setTotalEnBilletes(double totalEnBilletes) {
            this.totalEnBilletes = totalEnBilletes;
        }

        public List<Moneda> getMonedas() {
            return monedas;
        }

        public void setMonedas(List<Moneda> monedas) {
            this.monedas = monedas;
        }

        public double getTotalEnMonedas() {
            return totalEnMonedas;
        }

        public void setTotalEnMonedas(double totalEnMonedas) {
            this.totalEnMonedas = totalEnMonedas;
        }

        public static class Billete {
            private double denominacion;
            private int cantidad;
            private int cantidad2;
            private double subTotal;

            public double getDenominacion() {
                return denominacion;
            }

            public void setDenominacion(double denominacion) {
                this.denominacion = denominacion;
            }

            public int getCantidad() {
                return cantidad;
            }

            public void setCantidad(int cantidad) {
                this.cantidad = cantidad;
            }

            public double getSubTotal() {
                return subTotal;
            }

            public void setSubTotal(double subTotal) {
                this.subTotal = subTotal;
            }

            public int getCantidad2() {
                return cantidad2;
            }

            public void setCantidad2(int cantidad2) {
                this.cantidad2 = cantidad2;
            }

        }

        public static class Moneda {
            private double denominacion;
            private int cantidad;
            private int cantidad2;
            private double subTotal;

            public double getDenominacion() {
                return denominacion;
            }

            public void setDenominacion(double denominacion) {
                this.denominacion = denominacion;
            }

            public int getCantidad() {
                return cantidad;
            }

            public void setCantidad(int cantidad) {
                this.cantidad = cantidad;
            }

            public double getSubTotal() {
                return subTotal;
            }

            public void setSubTotal(double subTotal) {
                this.subTotal = subTotal;
            }

            public int getCantidad2() {
                return cantidad2;
            }

            public void setCantidad2(int cantidad2) {
                this.cantidad2 = cantidad2;
            }

        }
    }

    public static class Resumen {
        private double totalEfectivo;
        private double balance;
        private double diferencia;
        private String estado;

        public double getTotalEfectivo() {
            return totalEfectivo;
        }

        public void setTotalEfectivo(double totalEfectivo) {
            this.totalEfectivo = totalEfectivo;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getDiferencia() {
            return diferencia;
        }

        public void setDiferencia(double diferencia) {
            this.diferencia = diferencia;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

    }

}
