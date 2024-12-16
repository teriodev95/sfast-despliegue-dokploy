package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page2.tables;

import java.text.DecimalFormat;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page2.tables.classes.TablaResumenDeVentas;

public class ApartadoEncabezadoHoja2 {
    DecimalFormat formatoMonto = new DecimalFormat("#,###,##0.00");

    public PdfPTable creaEncabezado(TablaResumenDeVentas data) throws DocumentException {
        PdfPTable table = new PdfPTable(10); // Número de columnas visibles en la tabla.
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 3, 1, 2, 2, 1, 1, 1, 1, 1, 1 });

        table.addCell(estilizarCeldaCentrada(
                new Phrase(formatearNombreCampo(data.getTitulo()), FontFactory.getFont(FontFactory.HELVETICA, 10)), 2,
                2, 1)).setBorder(0);
        table.addCell(estilizarCeldaCentrada(
                new Phrase(data.getVendedor(), FontFactory.getFont(FontFactory.HELVETICA, 8)), 2, 2, 1))
                .setVerticalAlignment(Element.ALIGN_MIDDLE);

        agregarCeldaConceptoYValor(new Phrase("Zona: ", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                new Phrase(data.getZona(), FontFactory.getFont(FontFactory.HELVETICA, 10)), table, 2, 2);
        agregarCeldaConceptoYValor(new Phrase("Sem: ", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                new Phrase(String.valueOf(data.getSemana()), FontFactory.getFont(FontFactory.HELVETICA, 10)), table, 2,
                2);
        agregarCeldaConceptoYValor(new Phrase("Año: ", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                new Phrase(String.valueOf(data.getAnio()), FontFactory.getFont(FontFactory.HELVETICA, 10)), table, 2,
                2);

        table.addCell(lineaVacia(table));

        // PARTE BAJA DETALLES
        PdfPTable parteClientesYRenovaciones = new PdfPTable(2);
        parteClientesYRenovaciones.setWidths(new float[] { 3, 1 });
        parteClientesYRenovaciones.addCell(estilizarCeldaConceptoALaDerecha(
                new Phrase("Clientes Nuevos #", FontFactory.getFont(FontFactory.HELVETICA, 7)), 2, 1, 1));
        parteClientesYRenovaciones.addCell(
                estilizarCeldaCentrada(new Phrase(String.valueOf(data.getResumen().getClientesNuevos().getCantidad()),
                        FontFactory.getFont(FontFactory.HELVETICA, 7)), 2, 1, 1));
        parteClientesYRenovaciones.addCell(lineaVacia(parteClientesYRenovaciones));
        parteClientesYRenovaciones.addCell(estilizarCeldaConceptoALaDerecha(
                new Phrase("Renovaciones #", FontFactory.getFont(FontFactory.HELVETICA, 7)), 2, 1, 1));
        parteClientesYRenovaciones.addCell(
                estilizarCeldaCentrada(new Phrase(String.valueOf(data.getResumen().getRenovaciones().getCantidad()),
                        FontFactory.getFont(FontFactory.HELVETICA, 7)), 2, 1, 1))
                .setPadding(4);
        parteClientesYRenovaciones.addCell(lineaVacia(parteClientesYRenovaciones));
        table.addCell(transformaTablaACelda(parteClientesYRenovaciones, 2, 1)).setBorder(0);

        PdfPTable parteMontoClientesYRenovaciones = new PdfPTable(3);
        parteMontoClientesYRenovaciones.setWidths(new float[] { 4, 1, 3 });
        parteMontoClientesYRenovaciones.addCell(estilizarCeldaConceptoALaDerecha(
                new Phrase("Monto:", FontFactory.getFont(FontFactory.HELVETICA, 7)), 2, 1, 1));
        parteMontoClientesYRenovaciones.addCell(celdaSimbolo(7));
        parteMontoClientesYRenovaciones
                .addCell(celdaMonto(new Phrase(formatoMonto.format(data.getResumen().getClientesNuevos().getMonto()),
                        FontFactory.getFont(FontFactory.HELVETICA, 7)), 1, 1));
        parteMontoClientesYRenovaciones.addCell(lineaVacia(parteMontoClientesYRenovaciones));
        parteMontoClientesYRenovaciones.addCell(estilizarCeldaConceptoALaDerecha(
                new Phrase("Monto:", FontFactory.getFont(FontFactory.HELVETICA, 7)), 2, 1, 1));
        parteMontoClientesYRenovaciones.addCell(celdaSimbolo(7));
        parteMontoClientesYRenovaciones
                .addCell(celdaMonto(new Phrase(formatoMonto.format(data.getResumen().getRenovaciones().getMonto()),
                        FontFactory.getFont(FontFactory.HELVETICA, 7)), 1, 1))
                .setPadding(4);
        parteMontoClientesYRenovaciones.addCell(lineaVacia(parteMontoClientesYRenovaciones));
        table.addCell(transformaTablaACelda(parteMontoClientesYRenovaciones, 2, 2)).setBorder(0);

        agregarCeldaMultiFila(2, new float[] { 1, 1 },
                new Phrase("Total # de Ventas:", FontFactory.getFont(FontFactory.HELVETICA, 7)),
                new Phrase(String.valueOf(data.getResumen().getTotalVentas().getCantidad()),
                        FontFactory.getFont(FontFactory.HELVETICA, 7)),
                table, 2, 1);
        agregarCeldaMultiFila(3, new float[] { 4, 1, 4 },
                new Phrase("Primeros Pagos:", FontFactory.getFont(FontFactory.HELVETICA, 9)),
                new Phrase(formatoMonto.format(data.getResumen().getPrimerosPagos()),
                        FontFactory.getFont(FontFactory.HELVETICA, 8)),
                table, 2, 3);
        agregarCeldaMultiFila(3, new float[] { 4, 1, 3 },
                new Phrase("Total Vendido:", FontFactory.getFont(FontFactory.HELVETICA, 9)),
                new Phrase(formatoMonto.format(data.getResumen().getTotalVentas().getMonto()),
                        FontFactory.getFont(FontFactory.HELVETICA, 8)),
                table, 2, 3);

        table.setSpacingAfter(10);
        return table;
    }

    private PdfPCell lineaVacia(PdfPTable tabla) {
        PdfPCell celda = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 1)));
        celda.setBorder(0);
        celda.setColspan(tabla.getNumberOfColumns());
        return celda;
    }

    private void agregarCeldaMultiFila(Integer columnas, float[] anchoColumnas, Phrase concepto, Phrase monto,
            PdfPTable tablaPrincipal, Integer padding, Integer colspan) throws DocumentException {
        PdfPTable tablaContenedora = new PdfPTable(columnas);
        tablaContenedora.setWidths(anchoColumnas);
        PdfPCell celda = estilizarCeldaCentrada(concepto, 2, 1, 1);
        celda.setBorder(0);
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaContenedora.addCell(lineaVacia(tablaContenedora));
        tablaContenedora.addCell(celda);
        if (columnas == 3) {
            tablaContenedora.addCell(celdaSimbolo(8));
            tablaContenedora.addCell(celdaMonto(monto, 1, 1));
        } else {
            tablaContenedora.addCell(estilizarCeldaCentrada(monto, 2, 1, 1));
        }
        tablaContenedora.addCell(lineaVacia(tablaContenedora));
        tablaPrincipal.addCell(transformaTablaACelda(tablaContenedora, padding, colspan)).setBorder(0);

    }

    private void agregarCeldaConceptoYValor(Phrase concepto, Phrase valor, PdfPTable tabla, Integer padddingCelda,
            Integer colspanCelda) throws DocumentException {
        PdfPTable celdaContenedora = new PdfPTable(2);
        celdaContenedora.addCell(estilizarCeldaCentrada(concepto, 4, 1, 1)).setBorder(0);
        celdaContenedora.addCell(estilizarCeldaCentrada(valor, 4, 1, 1));
        tabla.addCell(transformaTablaACelda(celdaContenedora, 2, 2)).setBorder(0);

    }

    public static String formatearNombreCampo(String fieldName) {
        StringBuilder nombreFormateado = new StringBuilder();
        nombreFormateado.append(Character.toUpperCase(fieldName.charAt(0)));
        for (int i = 1; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            if (Character.isUpperCase(c)) {
                nombreFormateado.append(" ");
            }
            nombreFormateado.append(c);
        }
        return nombreFormateado.toString().trim().toUpperCase();
    }

    private PdfPCell estilizarCeldaCentrada(Phrase frase, Integer paddingBottom, Integer colspan, Integer rowspan) {
        PdfPCell celda = new PdfPCell(frase);
        celda.setPaddingBottom(paddingBottom);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setColspan(colspan);
        celda.setRowspan(rowspan);
        return celda;
    }

    private PdfPCell estilizarCeldaConceptoALaDerecha(Phrase frase, Integer paddingBottom, Integer colspan,
            Integer rowspan) {
        PdfPCell celda = new PdfPCell(frase);
        celda.setPaddingBottom(paddingBottom);
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setColspan(colspan);
        celda.setRowspan(rowspan);
        celda.setBorder(0);
        return celda;
    }

    private PdfPCell celdaMonto(Phrase phrase, Integer colspan, Integer rowspan) {
        PdfPCell celda = new PdfPCell(phrase);
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(PdfPCell.RIGHT | PdfPCell.TOP | PdfPCell.BOTTOM);
        celda.setColspan(colspan);
        celda.setRowspan(rowspan);
        return celda;
    }

    private PdfPCell celdaSimbolo(Integer size) {
        PdfPCell celda = new PdfPCell(new Phrase(" $", FontFactory.getFont(FontFactory.HELVETICA_BOLD, size)));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(PdfPCell.LEFT | PdfPCell.TOP | PdfPCell.BOTTOM);
        return celda;
    }

    private PdfPCell transformaTablaACelda(PdfPTable tabla, Integer padding, Integer colspan) {
        PdfPCell celda = new PdfPCell(tabla);
        celda.setPadding(padding);
        celda.setColspan(colspan);
        return celda;
    }
}
