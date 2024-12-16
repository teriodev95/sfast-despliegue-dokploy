package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables;

import java.text.DecimalFormat;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.classes.TablaFlujoEfectivoGerente;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.classes.TablaFlujoEfectivoGerente.Egreso;

public class ApartadoOtros extends GeneradoraTablasIngresosEgresos {
    DecimalFormat formatoMonto = new DecimalFormat("#,###,###.00");
    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
    Font miniBoldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7);
    Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 8f);
    BaseColor azulClaro = new BaseColor(52, 113, 235);
    BaseColor rosa = new BaseColor(235, 52, 137);

    public PdfPTable creaTablaOtros(TablaFlujoEfectivoGerente data) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setKeepTogether(true);

        table.addCell(lineaVacia(table));
        table.addCell(
                generarEncabezadoIngresosConceptoEgresos("OTROS (Celular, viaticos, etc.)", table.getNumberOfColumns()))
                .setBorder(0);

        // BLOQUE VACIO
        PdfPTable bloqueVacio = new PdfPTable(2);
        bloqueVacio.addCell(lineaVacia(bloqueVacio));

        // TABLA EGRESOS
        List<Egreso> egresosOtros = data.getOtros().getEgresos();
        PdfPTable tablaEgresos = new PdfPTable(3);
        tablaEgresos.setWidths(new float[] { 1, 1.2f, 0.8f });
        String[] headersE = { "FECHA: ", "CONCEPTO", "MONTO $" };
        for (String header : headersE) {
            PdfPCell celdaEncabezado = new PdfPCell(new Phrase(header, boldFont));
            celdaEncabezado.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaEncabezado.setBorderColor(rosa);
            tablaEgresos.addCell(celdaEncabezado);
        }
        for (Egreso egreso : egresosOtros) {
            PdfPCell celda = estiloCeldaCentrado(new Phrase(egreso.getFecha(), regularFont));
            celda.setBorderColor(rosa);
            tablaEgresos.addCell(celda);
            celda = estiloCeldaCentrado(new Phrase(egreso.getConcepto(), regularFont));
            celda.setBorderColor(rosa);
            tablaEgresos.addCell(celda);
            celda = new PdfPCell(estiloCeldaMonto(new Phrase(formatoMonto.format(egreso.getMonto()), regularFont)));
            celda.setBorderColor(rosa);
            tablaEgresos.addCell(celda);

        }
        rellenoFilasNulas(egresosOtros, tablaEgresos, 2, rosa);
        tablaEgresos.addCell(estiloCeldaCentrado(new Phrase(" ", regularFont))).setBorder(0);
        tablaEgresos.addCell(estiloCeldaAlineadoDerecha(new Phrase("Sub Total", regularFont))).setBorder(0);
        if (data.getOtros().getSubTotal() == 0) {
            tablaEgresos.addCell(new PdfPCell(estiloCeldaMonto(new Phrase("-", regularFont)))).setBorderColor(rosa);
        } else {
            tablaEgresos
                    .addCell(new PdfPCell(estiloCeldaMonto(
                            new Phrase(formatoMonto.format(data.getOtros().getSubTotal()), regularFont))))
                    .setBorderColor(rosa);
        }
        tablaEgresos.addCell(lineaVacia(tablaEgresos));

        PdfPCell cellIngresos = new PdfPCell(bloqueVacio);
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
}
