package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.classes.TablaFlujoEfectivoGerente;

public class ApartadoEncabezadoHoja4 {
    public PdfPTable creaEncabezado(TablaFlujoEfectivoGerente data) throws DocumentException {
        PdfPTable tabla = new PdfPTable(3); // Número de columnas visibles en la tabla.
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[] { 1.1f, 0.9f, 1.1f });
        tabla.addCell(personalizarCelda(
                new Phrase("FLUJO DE EFECTIVO DEL GERENTE", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                Element.ALIGN_CENTER, 2, 1, 1)).setBorder(0);
        tabla.addCell(personalizarCelda(new Phrase(data.getGerente(), FontFactory.getFont(FontFactory.HELVETICA, 8)),
                Element.ALIGN_CENTER, 2, 1, 1));

        PdfPTable celdaZona = new PdfPTable(2);
        celdaZona.addCell(personalizarCelda(new Phrase("Zona: ", FontFactory.getFont(FontFactory.HELVETICA, 8)),
                Element.ALIGN_RIGHT, 2, 1, 1)).setBorder(0);
        celdaZona.addCell(personalizarCelda(new Phrase(data.getZona(), FontFactory.getFont(FontFactory.HELVETICA, 8)),
                Element.ALIGN_CENTER, 3, 1, 1));

        PdfPTable celdaSemana = new PdfPTable(2);
        celdaSemana.addCell(personalizarCelda(new Phrase("Sem: ", FontFactory.getFont(FontFactory.HELVETICA, 8)),
                Element.ALIGN_RIGHT, 2, 1, 1)).setBorder(0);
        celdaSemana.addCell(personalizarCelda(
                new Phrase(String.valueOf(data.getSemana()), FontFactory.getFont(FontFactory.HELVETICA, 8)),
                Element.ALIGN_CENTER, 3, 1, 1));

        PdfPTable celdaAnio = new PdfPTable(2);
        celdaAnio.addCell(personalizarCelda(new Phrase("Año: ", FontFactory.getFont(FontFactory.HELVETICA, 8)),
                Element.ALIGN_RIGHT, 2, 1, 1)).setBorder(0);
        celdaAnio.addCell(personalizarCelda(
                new Phrase(String.valueOf(data.getAnio()), FontFactory.getFont(FontFactory.HELVETICA, 8)),
                Element.ALIGN_CENTER, 3, 1, 1));

        PdfPTable containerZoneAndDate = new PdfPTable(3);
        containerZoneAndDate.addCell(convertirTablaACelda(celdaZona, 2, 1)).setBorder(0);
        containerZoneAndDate.addCell(convertirTablaACelda(celdaSemana, 2, 1)).setBorder(0);
        containerZoneAndDate.addCell(convertirTablaACelda(celdaAnio, 2, 1)).setBorder(0);

        tabla.addCell(convertirTablaACelda(containerZoneAndDate, 2, 1)).setBorder(0);
        tabla.addCell(lineaVacia(tabla));
        return tabla;

    }

    private PdfPCell lineaVacia(PdfPTable tabla) {
        PdfPCell celda = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 1)));
        celda.setBorder(0);
        celda.setColspan(tabla.getNumberOfColumns());
        return celda;
    }

    private PdfPCell personalizarCelda(Phrase phrase, Integer align, Integer paddingBottom, Integer colspan,
            Integer rowspan) {
        PdfPCell celda = new PdfPCell(phrase);
        celda.setPaddingBottom(paddingBottom);
        celda.setHorizontalAlignment(align);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setColspan(colspan);
        celda.setRowspan(rowspan);
        return celda;
    }

    private PdfPCell convertirTablaACelda(PdfPTable tabla, Integer padding, Integer colspan) {
        PdfPCell celda = new PdfPCell(tabla);
        celda.setPadding(padding);
        celda.setColspan(colspan);
        return celda;
    }
}
