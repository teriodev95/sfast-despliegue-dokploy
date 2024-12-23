package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes;

import lombok.Data;

@Data
public class TablaCierreSemanalGerencia {
    private String titulo;
    private Ingresos ingresos;
    private Egresos egresos;
    private double efectivoAEntregar;

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Ingresos getIngresos() {
        return ingresos;
    }

    public void setIngresos(Ingresos ingresos) {
        this.ingresos = ingresos;
    }

    public Egresos getEgresos() {
        return egresos;
    }

    public void setEgresos(Egresos egresos) {
        this.egresos = egresos;
    }

    public double getEfectivoAEntregar() {
        return efectivoAEntregar;
    }

    public void setEfectivoAEntregar(double efectivoAEntregar) {
        this.efectivoAEntregar = efectivoAEntregar;
    }

    // Clases internas que reflejan la estructura del JSON
    public static class Ingresos {
        private IngAdministrativos administrativos;
        private IngOperativos operativos;
        private double totalDeIngresos;

        public IngAdministrativos getAdministrativos() {
            return administrativos;
        }

        public void setAdministrativos(IngAdministrativos administrativos) {
            this.administrativos = administrativos;
        }

        public IngOperativos getOperativos() {
            return operativos;
        }

        public void setOperativos(IngOperativos operativos) {
            this.operativos = operativos;
        }

        public double getTotalDeIngresos() {
            return totalDeIngresos;
        }

        public void setTotalDeIngresos(double totalDeIngresos) {
            this.totalDeIngresos = totalDeIngresos;
        }

        public static class IngAdministrativos {
            private double asignacionesDeCajaChica;
            private double asignacionesDeSeguridad;
            private double asignacionesDeOperacion;
            private double ingresoPorIncidente;
            private double totalDeAsignaciones;

            public double getAsignacionesDeCajaChica() {
                return asignacionesDeCajaChica;
            }

            public void setAsignacionesDeCajaChica(double asignacionesDeCajaChica) {
                this.asignacionesDeCajaChica = asignacionesDeCajaChica;
            }

            public double getAsignacionesDeSeguridad() {
                return asignacionesDeSeguridad;
            }

            public void setAsignacionesDeSeguridad(double asignacionesDeSeguridad) {
                this.asignacionesDeSeguridad = asignacionesDeSeguridad;
            }

            public double getAsignacionesDeOperacion() {
                return asignacionesDeOperacion;
            }

            public void setAsignacionesDeOperacion(double asignacionesDeOperacion) {
                this.asignacionesDeOperacion = asignacionesDeOperacion;
            }

            public double getIngresoPorIncidente() {
                return ingresoPorIncidente;
            }

            public void setIngresoPorIncidente(double ingresoPorIncidente) {
                this.ingresoPorIncidente = ingresoPorIncidente;
            }

            public double getTotalDeAsignaciones() {
                return totalDeAsignaciones;
            }

            public void setTotalDeAsignaciones(double totalDeAsignaciones) {
                this.totalDeAsignaciones = totalDeAsignaciones;
            }
        }

        public static class IngOperativos {
            private double cobranzaPuraDeLaSemana;
            private double montoExcedente;
            private double liquidaciones;
            private double primerosPagos;
            private double multas;
            private double otros;
            private double totalDeCobranza;

            public double getCobranzaPuraDeLaSemana() {
                return cobranzaPuraDeLaSemana;
            }

            public void setCobranzaPuraDeLaSemana(double cobranzaPuraDeLaSemana) {
                this.cobranzaPuraDeLaSemana = cobranzaPuraDeLaSemana;
            }

            public double getMontoExcedente() {
                return montoExcedente;
            }

            public void setMontoExcedente(double montoExcedente) {
                this.montoExcedente = montoExcedente;
            }

            public double getLiquidaciones() {
                return liquidaciones;
            }

            public void setLiquidaciones(double liquidaciones) {
                this.liquidaciones = liquidaciones;
            }

            public double getPrimerosPagos() {
                return primerosPagos;
            }

            public void setPrimerosPagos(double primerosPagos) {
                this.primerosPagos = primerosPagos;
            }

            public double getMultas() {
                return multas;
            }

            public void setMultas(double multas) {
                this.multas = multas;
            }

            public double getOtros() {
                return otros;
            }

            public void setOtros(double otros) {
                this.otros = otros;
            }

            public double getTotalDeCobranza() {
                return totalDeCobranza;
            }

            public void setTotalDeCobranza(double totalDeCobranza) {
                this.totalDeCobranza = totalDeCobranza;
            }
        }
    }

    public static class Egresos {
        private EgrAdministrativos administrativos;
        private EgrOperativos operativos;
        private double totalDeEgresos;

        public EgrAdministrativos getAdministrativos() {
            return administrativos;
        }

        public void setAdministrativos(EgrAdministrativos administrativos) {
            this.administrativos = administrativos;
        }

        public EgrOperativos getOperativos() {
            return operativos;
        }

        public void setOperativos(EgrOperativos operativos) {
            this.operativos = operativos;
        }

        public double getTotalDeEgresos() {
            return totalDeEgresos;
        }

        public void setTotalDeEgresos(double totalDeEgresos) {
            this.totalDeEgresos = totalDeEgresos;
        }

        public static class EgrAdministrativos {
            private double asignacionesACajaChica;
            private double asignacionesASeguridad;
            private double asignacionesDeOperacion;
            private double gastoPorIncidente;
            private double totalDeAsignaciones;

            public double getAsignacionesACajaChica() {
                return asignacionesACajaChica;
            }

            public void setAsignacionesACajaChica(double asignacionesACajaChica) {
                this.asignacionesACajaChica = asignacionesACajaChica;
            }

            public double getAsignacionesASeguridad() {
                return asignacionesASeguridad;
            }

            public void setAsignacionesASeguridad(double asignacionesASeguridad) {
                this.asignacionesASeguridad = asignacionesASeguridad;
            }

            public double getAsignacionesDeOperacion() {
                return asignacionesDeOperacion;
            }

            public void setAsignacionesDeOperacion(double asignacionesDeOperacion) {
                this.asignacionesDeOperacion = asignacionesDeOperacion;
            }

            public double getGastoPorIncidente() {
                return gastoPorIncidente;
            }

            public void setGastoPorIncidente(double gastoPorIncidente) {
                this.gastoPorIncidente = gastoPorIncidente;
            }

            public double getTotalDeAsignaciones() {
                return totalDeAsignaciones;
            }

            public void setTotalDeAsignaciones(double totalDeAsignaciones) {
                this.totalDeAsignaciones = totalDeAsignaciones;
            }
        }

        public static class EgrOperativos {
            private Ventas ventas;
            private ComisionesYBonos comisionesYBonos;
            private GastosOperativos gastosOperativos;

            public Ventas getVentas() {
                return ventas;
            }

            public void setVentas(Ventas ventas) {
                this.ventas = ventas;
            }

            public ComisionesYBonos getComisionesYBonos() {
                return comisionesYBonos;
            }

            public void setComisionesYBonos(ComisionesYBonos comisionesYBonos) {
                this.comisionesYBonos = comisionesYBonos;
            }

            public GastosOperativos getGastosOperativos() {
                return gastosOperativos;
            }

            public void setGastosOperativos(GastosOperativos gastosOperativos) {
                this.gastosOperativos = gastosOperativos;
            }

            public static class Ventas {
                private double clientesNuevos;
                private double renovaciones;
                private double totalDeVentas;

                public double getClientesNuevos() {
                    return clientesNuevos;
                }

                public void setClientesNuevos(double clientesNuevos) {
                    this.clientesNuevos = clientesNuevos;
                }

                public double getRenovaciones() {
                    return renovaciones;
                }

                public void setRenovaciones(double renovaciones) {
                    this.renovaciones = renovaciones;
                }

                public double getTotalDeVentas() {
                    return totalDeVentas;
                }

                public void setTotalDeVentas(double totalDeVentas) {
                    this.totalDeVentas = totalDeVentas;
                }
            }

            public static class ComisionesYBonos {
                private double comisionesPorCobranza;
                private double comisionesPorVentas;
                private double bonosDeAgentes;
                private double totalDeBonosYComisiones;

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

                public double getBonosDeAgentes() {
                    return bonosDeAgentes;
                }

                public void setBonosDeAgentes(double bonosDeAgentes) {
                    this.bonosDeAgentes = bonosDeAgentes;
                }

                public double getTotalDeBonosYComisiones() {
                    return totalDeBonosYComisiones;
                }

                public void setTotalDeBonosYComisiones(double totalDeBonosYComisiones) {
                    this.totalDeBonosYComisiones = totalDeBonosYComisiones;
                }
            }

            public static class GastosOperativos {
                private double gasolina;
                private double casetas;
                private double apoyoDeAutomovil;
                private double otros;
                private double totalDeGastos;

                public double getGasolina() {
                    return gasolina;
                }

                public void setGasolina(double gasolina) {
                    this.gasolina = gasolina;
                }

                public double getCasetas() {
                    return casetas;
                }

                public void setCasetas(double casetas) {
                    this.casetas = casetas;
                }

                public double getApoyoDeAutomovil() {
                    return apoyoDeAutomovil;
                }

                public void setApoyoDeAutomovil(double apoyoDeAutomovil) {
                    this.apoyoDeAutomovil = apoyoDeAutomovil;
                }

                public double getOtros() {
                    return otros;
                }

                public void setOtros(double otros) {
                    this.otros = otros;
                }

                public double getTotalDeGastos() {
                    return totalDeGastos;
                }

                public void setTotalDeGastos(double totalDeGastos) {
                    this.totalDeGastos = totalDeGastos;
                }
            }
        }
    }
}
