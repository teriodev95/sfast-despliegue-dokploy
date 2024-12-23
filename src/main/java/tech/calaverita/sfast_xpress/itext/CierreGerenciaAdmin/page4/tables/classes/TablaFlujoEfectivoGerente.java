package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.classes;

import java.util.List;

import lombok.Data;

@Data
public class TablaFlujoEfectivoGerente {
    private String titulo;
    private String gerente;
    private String zona;
    private int semana;
    private int anio;
    private Asignaciones asignacionesAdministracion;
    private Asignaciones asignacionesSeguridad;
    private Asignaciones asignacionesOperacion;
    private Asignaciones incidentes;
    private Asignaciones reposicionesSemanaAnterior;
    private Gasolina gasolina;
    private Casetas casetas;
    private MantenimientoAuto mantenimientoAuto;
    private Otros otros;
    private Resumen resumen;

    // Clase para los ingresos y egresos
    public static class Asignaciones {
        private List<Ingreso> ingresos;
        private double subTotalIngresos;
        private List<Egreso> egresos;
        private double subTotalEgresos;

        // Getters y setters
        public List<Ingreso> getIngresos() {
            return ingresos;
        }

        public void setIngresos(List<Ingreso> ingresos) {
            this.ingresos = ingresos;
        }

        public double getSubTotalIngresos() {
            return subTotalIngresos;
        }

        public void setSubTotalIngresos(double subTotalIngresos) {
            this.subTotalIngresos = subTotalIngresos;
        }

        public List<Egreso> getEgresos() {
            return egresos;
        }

        public void setEgresos(List<Egreso> egresos) {
            this.egresos = egresos;
        }

        public double getSubTotalEgresos() {
            return subTotalEgresos;
        }

        public void setSubTotalEgresos(double subTotalEgresos) {
            this.subTotalEgresos = subTotalEgresos;
        }
    }

    // Clase para los ingresos
    public static class Ingreso {
        private String fecha;
        private String recibiDe;
        private double monto;
        private String comentario; // para incidentes y reposiciones

        // Getters y setters
        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getRecibiDe() {
            return recibiDe;
        }

        public void setRecibiDe(String recibiDe) {
            this.recibiDe = recibiDe;
        }

        public double getMonto() {
            return monto;
        }

        public void setMonto(double monto) {
            this.monto = monto;
        }

        public String getComentario() {
            return comentario;
        }

        public void setComentario(String comentario) {
            this.comentario = comentario;
        }
    }

    // Clase para los egresos
    public static class Egreso {
        private String fecha;
        private String asigneA;
        private double monto;
        private String comentario; // para incidentes y reposiciones
        private int litros; // para gasolina
        private String concepto;

        // Getters y setters
        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getAsigneA() {
            return asigneA;
        }

        public void setAsigneA(String asigneA) {
            this.asigneA = asigneA;
        }

        public double getMonto() {
            return monto;
        }

        public void setMonto(double monto) {
            this.monto = monto;
        }

        public String getComentario() {
            return comentario;
        }

        public void setComentario(String comentario) {
            this.comentario = comentario;
        }

        public int getLitros() {
            return litros;
        }

        public void setLitros(int litros) {
            this.litros = litros;
        }

        public String getConcepto() {
            return concepto;
        }

        public void setConcepto(String concepto) {
            this.concepto = concepto;
        }
    }

    // Clase para gasolina
    public static class Gasolina {
        private List<Egreso> egresos;
        private SubTotal subTotal;

        // Getters y setters
        public List<Egreso> getEgresos() {
            return egresos;
        }

        public void setEgresos(List<Egreso> egresos) {
            this.egresos = egresos;
        }

        public SubTotal getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(SubTotal subTotal) {
            this.subTotal = subTotal;
        }
    }

    public static class Casetas {
        private List<Egreso> egresos;
        private double subTotal;

        // Getters y setters
        public List<Egreso> getEgresos() {
            return egresos;
        }

        public void setEgresos(List<Egreso> egresos) {
            this.egresos = egresos;
        }

        public double getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(double subTotal) {
            this.subTotal = subTotal;
        }
    }

    public static class MantenimientoAuto {
        private List<Egreso> egresos;
        private double subTotal;

        // Getters y setters
        public List<Egreso> getEgresos() {
            return egresos;
        }

        public void setEgresos(List<Egreso> egresos) {
            this.egresos = egresos;
        }

        public double getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(double subTotal) {
            this.subTotal = subTotal;
        }
    }

    // Clase para los subtotales
    public static class SubTotal {
        private int litros; // para gasolina
        private double monto;

        // Getters y setters
        public int getLitros() {
            return litros;
        }

        public void setLitros(int litros) {
            this.litros = litros;
        }

        public double getMonto() {
            return monto;
        }

        public void setMonto(double monto) {
            this.monto = monto;
        }
    }

    // Clase para otros egresos
    public static class Otros {
        private List<Egreso> egresos;
        private double subTotal;

        // Getters y setters
        public List<Egreso> getEgresos() {
            return egresos;
        }

        public void setEgresos(List<Egreso> egresos) {
            this.egresos = egresos;
        }

        public double getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(double subTotal) {
            this.subTotal = subTotal;
        }
    }

    // Clase para el resumen
    public static class Resumen {
        private double ingresos;
        private double egresos;
        private double diferencia;

        // Getters y setters
        public double getIngresos() {
            return ingresos;
        }

        public void setIngresos(double ingresos) {
            this.ingresos = ingresos;
        }

        public double getEgresos() {
            return egresos;
        }

        public void setEgresos(double egresos) {
            this.egresos = egresos;
        }

        public double getDiferencia() {
            return diferencia;
        }

        public void setDiferencia(double diferencia) {
            this.diferencia = diferencia;
        }
    }

    // Getters y setters para la clase principal
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

    public void setAno(int anio) {
        this.anio = anio;
    }

    public Asignaciones getAsignacionesAdministracion() {
        return asignacionesAdministracion;
    }

    public void setAsignacionesAdministracion(Asignaciones asignacionesAdministracion) {
        this.asignacionesAdministracion = asignacionesAdministracion;
    }

    public Asignaciones getAsignacionesSeguridad() {
        return asignacionesSeguridad;
    }

    public void setAsignacionesSeguridad(Asignaciones asignacionesSeguridad) {
        this.asignacionesSeguridad = asignacionesSeguridad;
    }

    public Asignaciones getAsignacionesOperacion() {
        return asignacionesOperacion;
    }

    public void setAsignacionesOperacion(Asignaciones asignacionesOperacion) {
        this.asignacionesOperacion = asignacionesOperacion;
    }

    public Asignaciones getIncidentes() {
        return incidentes;
    }

    public void setIncidentes(Asignaciones incidentes) {
        this.incidentes = incidentes;
    }

    public Asignaciones getReposicionesSemanaAnterior() {
        return reposicionesSemanaAnterior;
    }

    public void setReposicionesSemanaAnterior(Asignaciones reposicionesSemanaAnterior) {
        this.reposicionesSemanaAnterior = reposicionesSemanaAnterior;
    }

    public Gasolina getGasolina() {
        return gasolina;
    }

    public void setGasolina(Gasolina gasolina) {
        this.gasolina = gasolina;
    }

    public Casetas getCasetas() {
        return casetas;
    }

    public void setCasetas(Casetas casetas) {
        this.casetas = casetas;
    }

    public MantenimientoAuto getMantenimientoAuto() {
        return mantenimientoAuto;
    }

    public void setMantenimientoAuto(MantenimientoAuto mantenimientoAuto) {
        this.mantenimientoAuto = mantenimientoAuto;
    }

    public Otros getOtros() {
        return otros;
    }

    public void setOtros(Otros otros) {
        this.otros = otros;
    }

    public Resumen getResumen() {
        return resumen;
    }

    public void setResumen(Resumen resumen) {
        this.resumen = resumen;
    }

}
