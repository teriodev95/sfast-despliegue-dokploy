package tech.calaverita.sfast_xpress.DTOs;

import lombok.Data;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaCierreSemanalGerencia;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaDetallesCierreAgencias;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaNumerosGerencia;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page2.tables.classes.TablaResumenDeVentas;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables.classes.TablaFlujoEfectivo;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.classes.TablaFlujoEfectivoGerente;

@Data
public class DataCierreDTO {
    private String cierreRealizadoPor;
    private TablaDetallesCierreAgencias tablaDetallesCierreAgencias;
    private TablaNumerosGerencia tablaNumerosGerencia;
    private TablaCierreSemanalGerencia tablaCierreSemanalGerencia;
    private TablaResumenDeVentas tablaResumenDeVentas;
    private TablaFlujoEfectivo tablaFlujoEfectivo;
    private TablaFlujoEfectivoGerente tablaFlujoEfectivoGerente;
}
