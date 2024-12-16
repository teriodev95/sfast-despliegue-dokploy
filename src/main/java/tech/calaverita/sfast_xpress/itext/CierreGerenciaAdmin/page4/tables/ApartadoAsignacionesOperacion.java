package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables;

import java.text.DecimalFormat;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
// import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.classes.TablaFlujoEfectivoGerente;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.classes.TablaFlujoEfectivoGerente.Egreso;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.classes.TablaFlujoEfectivoGerente.Ingreso;

public class ApartadoAsignacionesOperacion extends GeneradoraTablasIngresosEgresos {
    DecimalFormat formatoMonto = new DecimalFormat("#,###,###.00");
    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
    Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 8f);
    BaseColor azulClaro = new BaseColor(52, 113, 235);
    BaseColor rosa = new BaseColor(235, 52, 137);

    public PdfPTable creaTablasAsignacionesOperacion(TablaFlujoEfectivoGerente data) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(lineaVacia(table));
        table.addCell(generarEncabezadoIngresosConceptoEgresos("ASIGNACIONES OPERACIÓN (Gerentes)",
                table.getNumberOfColumns())).setBorder(0);
        ;

        // TABLA INGRESOS
        List<Ingreso> ingresosAsignacionesOperacion = data.getAsignacionesOperacion().getIngresos();
        String[] headers = { "FECHA: ", "RECIBI DE: ", "MONTO $" };
        PdfPTable tablaIngresos = generarTablaYEncabezados(headers, azulClaro);
        for (Ingreso ingreso : ingresosAsignacionesOperacion) {
            agregarValoresATabla(new Phrase(ingreso.getFecha(), regularFont),
                    new Phrase(ingreso.getRecibiDe(), regularFont),
                    new Phrase(formatoMonto.format(ingreso.getMonto()), regularFont), azulClaro, tablaIngresos);
        }
        agregarRellenoYSubtotal(ingresosAsignacionesOperacion, tablaIngresos, 3,
                data.getAsignacionesOperacion().getSubTotalIngresos(), azulClaro);

        // TABLA EGRESOS
        List<Egreso> egresosAsignacionesOperacion = data.getAsignacionesOperacion().getEgresos();
        String[] headersE = { "FECHA: ", "ASIGNE A: ", "MONTO $" };
        PdfPTable tablaEgresos = generarTablaYEncabezados(headersE, rosa);
        for (Egreso egreso : egresosAsignacionesOperacion) {
            agregarValoresATabla(new Phrase(egreso.getFecha(), regularFont),
                    new Phrase(egreso.getAsigneA(), regularFont),
                    new Phrase(formatoMonto.format(egreso.getMonto()), regularFont), rosa, tablaEgresos);
        }
        agregarRellenoYSubtotal(egresosAsignacionesOperacion, tablaEgresos, 3,
                data.getAsignacionesOperacion().getSubTotalEgresos(), rosa);

        PdfPCell cellIngresos = new PdfPCell(tablaIngresos);
        PdfPCell cellEgresos = new PdfPCell(tablaEgresos);
        cellIngresos.setPaddingRight(10);
        cellEgresos.setPaddingLeft(10);
        table.addCell(cellIngresos).setBorder(0);
        table.addCell(cellEgresos).setBorder(0);

        return table;
    }
}
