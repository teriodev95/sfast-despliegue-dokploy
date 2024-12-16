package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaDetallesCierreAgencias;

public class ApartadoEncabezadoHoja1 {

    BaseColor azulClaro = new BaseColor(52, 113, 235);

    public PdfPTable creaEncabezado(TablaDetallesCierreAgencias data) throws DocumentException {
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);

        PdfPCell celdaTituloReporte = new PdfPCell(
                new Phrase(data.getTituloReporte(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
        celdaTituloReporte.setPaddingBottom(5);
        celdaTituloReporte.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaTituloReporte.setVerticalAlignment(Element.ALIGN_CENTER);
        celdaTituloReporte.setColspan(4);
        table.addCell(celdaTituloReporte);

        PdfPCell celdaEmpresa = new PdfPCell(
                new Phrase(data.getEmpresa(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
        PdfPTable tablaEmpresa = new PdfPTable(1);
        celdaEmpresa.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaEmpresa.setPaddingBottom(5);
        tablaEmpresa.addCell(celdaEmpresa);
        table.addCell(transformaTablaEmpresaACelda(tablaEmpresa, 2, 2)).setBorder(0);

        agregarCeldaConceptoYValor(new Phrase("Sem: ", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                new Phrase(data.getSemana(), FontFactory.getFont(FontFactory.HELVETICA, 10, azulClaro)),
                new float[] { 1, 1 }, table, 2, 1);
        agregarCeldaConceptoYValor(new Phrase("Año: ", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                new Phrase(data.getAnio(), FontFactory.getFont(FontFactory.HELVETICA, 10, azulClaro)),
                new float[] { 1, 1 }, table, 2, 1);
        agregarCeldaConceptoYValor(new Phrase("Gerente: ", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                new Phrase(data.getGerente(), FontFactory.getFont(FontFactory.HELVETICA, 10, azulClaro)),
                new float[] { 1, 3 }, table, 3, 4);
        agregarCeldaConceptoYValor(new Phrase("Zona: ", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                new Phrase(data.getZona(), FontFactory.getFont(FontFactory.HELVETICA, 10, azulClaro)),
                new float[] { 1, 1 }, table, 2, 1);
        agregarCeldaConceptoYValor(new Phrase("# Emp: ", FontFactory.getFont(FontFactory.HELVETICA, 9)),
                new Phrase(data.getIdEmpleado(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, azulClaro)),
                new float[] { 1, 1 }, table, 2, 1);
        agregarCeldaConceptoYValor(new Phrase("Fecha y Hora: ", FontFactory.getFont(FontFactory.HELVETICA, 7)),
                new Phrase(data.getFechaHora(), FontFactory.getFont(FontFactory.HELVETICA, 10)), new float[] { 1, 3 },
                table, 2, 2);

        table.setSpacingAfter(10);
        return table;
    }

    private void agregarCeldaConceptoYValor(Phrase concepto, Phrase valor, float[] anchoCeldas, PdfPTable tabla,
            Integer padddingCelda, Integer colspanCelda) throws DocumentException {
        PdfPCell celdaConcepto = new PdfPCell(concepto);
        PdfPTable celdaCompleta = new PdfPTable(2);
        celdaCompleta.setWidths(anchoCeldas);
        celdaConcepto.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaConcepto.setBorder(PdfPCell.NO_BORDER);
        celdaCompleta.addCell(celdaConcepto);
        PdfPCell celdaValor = new PdfPCell(valor);
        celdaValor.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaCompleta.addCell(celdaValor);
        tabla.addCell(transformaTablaACelda(celdaCompleta, padddingCelda, colspanCelda)).setBorder(0);
    }

    private PdfPCell transformaTablaACelda(PdfPTable table, Integer padding, Integer colspan) {
        PdfPCell cell = new PdfPCell(table);
        cell.setPadding(padding);
        cell.setColspan(colspan);
        return cell;
    }

    private PdfPCell transformaTablaEmpresaACelda(PdfPTable table, Integer padding, Integer colspan) {
        PdfPCell cell = new PdfPCell(table);
        cell.setPadding(padding);
        cell.setPaddingLeft(padding * 2);
        cell.setColspan(colspan);
        return cell;
    }
}
