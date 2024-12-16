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

public class ApartadoGasolinaYTotales extends GeneradoraTablasIngresosEgresos {
    DecimalFormat formatoMonto = new DecimalFormat("#,###,###.00");
    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
    Font miniBoldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7);
    Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 8f);
    BaseColor azulClaro = new BaseColor(52, 113, 235);
    BaseColor rosa = new BaseColor(235, 52, 137);

    public PdfPTable creaTablaGasolinaYTotales(TablaFlujoEfectivoGerente data) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(lineaVacia(table));
        table.addCell(generarEncabezadoIngresosConceptoEgresos("GASOLINA", table.getNumberOfColumns())).setBorder(0);

        // TABLA TOTALES
        PdfPTable tablaTotales = new PdfPTable(2);
        tablaTotales.setWidths(new float[] { 2, 0.8f });

        tablaTotales.addCell(lineaVacia(tablaTotales));
        tablaTotales.addCell(lineaVacia(tablaTotales));
        colocarTotal(tablaTotales, "INGRESOS: ", data.getResumen().getIngresos(), azulClaro, BaseColor.WHITE);
        tablaTotales.addCell(lineaVacia(tablaTotales));
        colocarTotal(tablaTotales, "EGRESOS: ", data.getResumen().getEgresos(), rosa, BaseColor.WHITE);
        tablaTotales.addCell(lineaVacia(tablaTotales));
        colocarTotal(tablaTotales, "DIFERENCIA: ", data.getResumen().getDiferencia(), BaseColor.BLACK, BaseColor.CYAN);
        tablaTotales.addCell(lineaVacia(tablaTotales));

        // TABLA EGRESOS
        List<Egreso> egresosGasolina = data.getGasolina().getEgresos();
        String[] headersE = { "FECHA: ", "LITROS", "MONTO $" };
        PdfPTable tablaEgresos = generarTablaYEncabezados(headersE, rosa);
        for (Egreso egreso : egresosGasolina) {
            agregarValoresATabla(new Phrase(egreso.getFecha(), regularFont),
                    new Phrase(String.valueOf(egreso.getLitros()), regularFont),
                    new Phrase(formatoMonto.format(egreso.getMonto()), regularFont), rosa, tablaEgresos);
        }
        agregarRellenoYSubtotal(egresosGasolina, tablaEgresos, data.getGasolina().getSubTotal().getLitros(),
                data.getGasolina().getSubTotal().getMonto(), rosa);

        PdfPCell cellIngresos = new PdfPCell(tablaTotales);
        PdfPCell cellEgresos = new PdfPCell(tablaEgresos);
        cellIngresos.setPaddingRight(10);
        cellEgresos.setPaddingLeft(10);
        table.addCell(cellIngresos).setBorder(0);
        table.addCell(cellEgresos).setBorder(0);

        return table;
    }

    @Override
    public PdfPCell generarEncabezadoIngresosConceptoEgresos(String frase, Integer colspan) throws DocumentException {
        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidths(new float[] { 2.5f, 1, 1.5f });
        PdfPCell celda = new PdfPCell(new Phrase(" ", boldFont));
        celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        // celda.setBorderColor(azulClaro);
        celda.setBorder(0);
        tabla.addCell(celda);
        celda = new PdfPCell(new Phrase(frase, miniBoldFont));
        celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        celda.setBorder(0);
        tabla.addCell(celda);
        celda = new PdfPCell(new Phrase("EGRESOS", boldFont));
        celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        celda.setBorderColor(rosa);
        tabla.addCell(celda);
        PdfPCell celdaContenedora = new PdfPCell(tabla);
        celdaContenedora.setColspan(colspan);
        return celdaContenedora;
    }

    private <T> void agregarRellenoYSubtotal(List<T> data, PdfPTable tabla, int litros, double monto, BaseColor color)
            throws DocumentException {
        rellenoFilasNulas(data, tabla, 5, color);
        // tabla.addCell(estiloCeldaCentrado(new Phrase(" ",
        // regularFont))).setBorder(0);
        tabla.addCell(estiloCeldaAlineadoDerecha(new Phrase("Sub Total", regularFont))).setBorder(0);
        tabla.addCell(new PdfPCell(estiloCeldaCentrado(new Phrase(String.valueOf(litros), regularFont))))
                .setBorderColor(color);
        if (monto == 0) {
            tabla.addCell(new PdfPCell(estiloCeldaMonto(new Phrase("-", regularFont)))).setBorderColor(color);
        } else {
            tabla.addCell(new PdfPCell(estiloCeldaMonto(new Phrase(formatoMonto.format(monto), regularFont))))
                    .setBorderColor(color);
        }
        tabla.addCell(lineaVacia(tabla));
    }

    private void colocarTotal(PdfPTable tabla, String concepto, double monto, BaseColor colorBorde,
            BaseColor colorFondo) throws DocumentException {
        tabla.addCell(estiloCeldaCentrado(new Phrase(concepto, boldFont))).setBorder(0);
        if (monto == 0) {
            PdfPCell celda = new PdfPCell(estiloCeldaMonto(new Phrase("-")));
            celda.setBorderColor(colorBorde);
            celda.setBackgroundColor(colorFondo);
            tabla.addCell(celda);
        } else {
            PdfPCell celda = new PdfPCell(estiloCeldaMonto(new Phrase(formatoMonto.format(monto), regularFont)));
            celda.setBorderColor(colorBorde);
            celda.setBackgroundColor(colorFondo);
            tabla.addCell(celda);
        }
    }
}
