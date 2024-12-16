package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.data.JsonData;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.ApartadoBloquesDeFirmas;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.ApartadoCierreSemanalGerencia;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.ApartadoDetallesDeCierreDeAgencias;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.ApartadoEncabezadoHoja1;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.ApartadoNumerosDeLaGerencia;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaCierreSemanalGerencia;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaDetallesCierreAgencias;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaNumerosGerencia;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page2.data.JsonDataPage2;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page2.tables.ApartadoEncabezadoHoja2;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page2.tables.ApartadoResumenDeVentas;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page2.tables.classes.TablaResumenDeVentas;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.data.JsonDataPage3;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables.ApartadoComisionesYAsignaciones;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables.ApartadoEncabezadoHoja3;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables.ApartadoFlujoDeEfectivoCierreSemanal;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables.ApartadoResumen;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables.ApartadoTabulador;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables.classes.TablaFlujoEfectivo;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.data.JsonDataPage4;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.ApartadoAsignacionesAdministracion;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.ApartadoAsignacionesOperacion;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.ApartadoAsignacionesSeguridad;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.ApartadoCasetas;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.ApartadoEncabezadoHoja4;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.ApartadoGasolinaYTotales;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.ApartadoIncidentes;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.ApartadoMantenimientoAuto;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.ApartadoOtros;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.ApartadoReposicionesSemanales;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.classes.TablaFlujoEfectivoGerente;

public class MaquetadoPdf {
    public static void createPDF() throws FileNotFoundException, DocumentException {
        ;
        String dest = Constants.RUTA_PDF_PRODUCCION + "cierre_semanal_v5.pdf"; // El destino del archivo PDF.
        Rectangle ticket = new Rectangle(PageSize.A4);
        Document doc = new Document(ticket, 15f, 15f, 20f, 20f);
        PdfWriter.getInstance(doc, new FileOutputStream(dest));
        doc.open();
        JsonData jsonData = new JsonData();
        TablaDetallesCierreAgencias data = jsonData.dataBalance(); // Data original, en un json separado
        // TablaDetallesCierreAgencias data = jsonData.dataBalanceDesdeArray(); //Data
        // con 12 agencias, en un array de json

        TablaNumerosGerencia data2 = jsonData.dataNumerosGerencia(); // Data original
        // TablaNumerosGerencia data2 = jsonData.dataNumerosDesdeArray(); //Data de
        // array

        TablaCierreSemanalGerencia data3 = jsonData.dataDetallesCierre();

        ApartadoEncabezadoHoja1 tablaDetalles = new ApartadoEncabezadoHoja1();
        ApartadoDetallesDeCierreDeAgencias tablaBalance = new ApartadoDetallesDeCierreDeAgencias();
        ApartadoNumerosDeLaGerencia tablaNumeros = new ApartadoNumerosDeLaGerencia();
        ApartadoCierreSemanalGerencia tablaDetallesCierre = new ApartadoCierreSemanalGerencia();
        ApartadoBloquesDeFirmas bloqueFirmas = new ApartadoBloquesDeFirmas();

        doc.add(tablaDetalles.creaEncabezado(data));
        doc.add(tablaBalance.creaParteInformacionAgencia(data));
        doc.add(tablaBalance.creaParteClientesSantSact(data));
        doc.add(tablaNumeros.creaTablaNumerosDeLaGerencia(data2));
        doc.add(tablaDetallesCierre.creaTablasDetallesCierreSemanalGerencia(data3));
        doc.add(bloqueFirmas.creaBloquesDeFirmas());

        // HOJA 2
        doc.newPage();

        JsonDataPage2 jsonDataPage2 = new JsonDataPage2();
        TablaResumenDeVentas dataResume = jsonDataPage2.makeDataBlob(); // Data original
        // TablaResumenDeVentas dataResume = jsonDataPage2.makeDataFromBlob(); //Data
        // desde el array de json, tiene 30 agencias

        ApartadoEncabezadoHoja2 tablaDetallesResumenVentas = new ApartadoEncabezadoHoja2();
        ApartadoResumenDeVentas tablaResumen = new ApartadoResumenDeVentas();

        doc.add(tablaDetallesResumenVentas.creaEncabezado(dataResume));
        doc.add(tablaResumen.creaTablaResumenVentas(dataResume));

        // HOJA 3
        doc.newPage();

        JsonDataPage3 jsonDataPage3 = new JsonDataPage3();
        TablaFlujoEfectivo dataFlow = jsonDataPage3.makeData(); // Data original
        // TablaFlujoEfectivo dataFlow = jsonDataPage3.makeDataFromBlob(); //Data desde
        // el array de json, tiene datos random

        ApartadoEncabezadoHoja3 tablaFlujoEfectivo = new ApartadoEncabezadoHoja3();
        ApartadoFlujoDeEfectivoCierreSemanal tablaCobranzaAgencias = new ApartadoFlujoDeEfectivoCierreSemanal();
        ApartadoComisionesYAsignaciones tablaComisionesYasignaciones = new ApartadoComisionesYAsignaciones();
        ApartadoResumen tablaResumenFlujo = new ApartadoResumen();
        ApartadoTabulador tablaTabulador = new ApartadoTabulador();

        doc.add(tablaFlujoEfectivo.creaEncabezado(dataFlow));
        doc.add(tablaCobranzaAgencias.creaTablasFlujoDeEfectivo(dataFlow));
        doc.add(tablaComisionesYasignaciones.creaTablaComisionesYAsignaciones(dataFlow));
        doc.add(tablaTabulador.creaTablaTabulador(dataFlow));
        doc.add(tablaResumenFlujo.creaTablaResumen(dataFlow));

        // HOJA 4
        doc.newPage();
        JsonDataPage4 jsonDataPage4 = new JsonDataPage4();
        ApartadoEncabezadoHoja4 tablaDetallesHoja4 = new ApartadoEncabezadoHoja4();
        TablaFlujoEfectivoGerente dataFlowManager = jsonDataPage4.makeData();
        // TablaFlujoEfectivoGerente dataFlowManager =
        // jsonDataPage4.makeDataFromArray(); //data del array

        ApartadoAsignacionesAdministracion tablaAsignacionesAdmin = new ApartadoAsignacionesAdministracion();
        ApartadoAsignacionesSeguridad tablaAsignacionesSeguridad = new ApartadoAsignacionesSeguridad();
        ApartadoAsignacionesOperacion tablaAsignacionesOperacion = new ApartadoAsignacionesOperacion();
        ApartadoIncidentes tablaIncidentes = new ApartadoIncidentes();
        ApartadoReposicionesSemanales tablaReposiciones = new ApartadoReposicionesSemanales();
        ApartadoGasolinaYTotales tablaGasolinaTotales = new ApartadoGasolinaYTotales();
        ApartadoCasetas tablaCasetas = new ApartadoCasetas();
        ApartadoMantenimientoAuto tablaMantAuto = new ApartadoMantenimientoAuto();
        ApartadoOtros tablaOtros = new ApartadoOtros();

        doc.add(tablaDetallesHoja4.creaEncabezado(dataFlowManager));
        doc.add(tablaAsignacionesAdmin.creaTablasAsignacionesAdimnistracion(dataFlowManager));
        doc.add(tablaAsignacionesSeguridad.creaTablasAsignacionesSeguridad(dataFlowManager));
        doc.add(tablaAsignacionesOperacion.creaTablasAsignacionesOperacion(dataFlowManager));
        doc.add(tablaIncidentes.creaTablaIncidentes(dataFlowManager));
        doc.add(tablaReposiciones.creaTablaReposicionesSemanales(dataFlowManager));
        doc.add(tablaGasolinaTotales.creaTablaGasolinaYTotales(dataFlowManager));
        doc.add(tablaCasetas.creaTablaCasetas(dataFlowManager));
        doc.add(tablaMantAuto.creaTablaMantenimientoAuto(dataFlowManager));
        doc.add(tablaOtros.creaTablaOtros(dataFlowManager));

        doc.close();

    }

}
