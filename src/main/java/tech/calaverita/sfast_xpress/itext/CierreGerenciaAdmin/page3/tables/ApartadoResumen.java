package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables;

import java.text.DecimalFormat;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import tech.calaverita.sfast_xpress.DTOs.TablaFlujoEfectivoDTO;

public class ApartadoResumen {
        DecimalFormat formatoMonto = new DecimalFormat("#,###,##0.00");
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 9f);

        public PdfPTable creaTablaResumen(TablaFlujoEfectivoDTO data) throws DocumentException {
                // PARTE RESUMEN
                PdfPTable tabla = new PdfPTable(6);
                tabla.setWidthPercentage(100);

                PdfPTable tablaContenedoraTotalEfectivo = combinarCeldaConceptoYValor(2, new float[] { 2, 1.5f },
                                estilizarCelda(new Phrase("TOTAL EFECTIVO: ",
                                                FontFactory.getFont(FontFactory.HELVETICA, 10)),
                                                Element.ALIGN_RIGHT, 4, 1, 1),
                                new PdfPCell(estiloCeldaAlineadoCentradoCash(
                                                new Phrase(formatoMonto.format(data.getResumen().getTotalEfectivo()),
                                                                regularFont))));
                PdfPTable tablaContenedoraBalance = combinarCeldaConceptoYValor(2, new float[] { 1, 1 },
                                estilizarCelda(new Phrase("BALANCE: ", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                                                Element.ALIGN_RIGHT, 4, 1, 1),
                                new PdfPCell(estiloCeldaAlineadoCentradoCash(
                                                new Phrase(formatoMonto.format(data.getResumen().getBalance()),
                                                                regularFont))));
                PdfPTable tablaContenedoraDiferencia = new PdfPTable(2);
                tablaContenedoraDiferencia
                                .addCell(estilizarCelda(
                                                new Phrase("DIFERENCIA: ",
                                                                FontFactory.getFont(FontFactory.HELVETICA, 10)),
                                                Element.ALIGN_RIGHT, 4, 1, 1))
                                .setBorder(0);
                tablaContenedoraDiferencia.addCell(new PdfPCell(estiloCeldaAlineadoCentradoCash(
                                new Phrase(formatoMonto.format(data.getResumen().getDiferencia()), regularFont))));
                tablaContenedoraDiferencia
                                .addCell(estilizarCelda(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                                                Element.ALIGN_RIGHT, 4, 1, 1))
                                .setBorder(0);
                tablaContenedoraDiferencia.addCell(estilizarCelda(
                                new Phrase(data.getResumen().getEstado(),
                                                FontFactory.getFont(FontFactory.HELVETICA, 10)),
                                Element.ALIGN_CENTER, 4, 1, 1));

                tabla.addCell(tablaCelda(tablaContenedoraTotalEfectivo, 2, 2)).setBorder(0);
                tabla.addCell(tablaCelda(tablaContenedoraBalance, 2, 2)).setBorder(0);
                tabla.addCell(tablaCelda(tablaContenedoraDiferencia, 2, 2)).setBorder(0);

                return tabla;
        }

        private PdfPCell lineaVacia(PdfPTable tabla) {
                PdfPCell celda = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 1)));
                celda.setBorder(0);
                celda.setColspan(tabla.getNumberOfColumns());
                return celda;
        }

        /**
         * Generacion de una tabla para estilizarlo a una sola celda, juntando un valor
         * y el simbolo $
         * 
         * @param phrase
         * @return tablaContenedora para ser convertida a una celda
         * @throws DocumentException
         */
        private PdfPTable estiloCeldaAlineadoCentradoCash(Phrase phrase) throws DocumentException {
                PdfPTable tablaContenedora = new PdfPTable(2);
                tablaContenedora.setWidths(new float[] { 1, 2 });
                PdfPCell celda = new PdfPCell(new Phrase(" $", regularFont));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setPaddingBottom(4);
                celda.setBorder(PdfPCell.LEFT | PdfPCell.TOP | PdfPCell.BOTTOM);
                tablaContenedora.addCell(celda);
                celda = new PdfPCell(phrase);
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setPaddingBottom(4);
                celda.setBorder(PdfPCell.RIGHT | PdfPCell.TOP | PdfPCell.BOTTOM);
                tablaContenedora.addCell(celda);
                return tablaContenedora;
        }

        private PdfPCell estilizarCelda(Phrase phrase, Integer align, Integer paddingBottom, Integer colspan,
                        Integer rowspan) {
                PdfPCell headerCell = new PdfPCell(phrase);
                headerCell.setPaddingBottom(paddingBottom);
                headerCell.setHorizontalAlignment(align);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerCell.setColspan(colspan);
                headerCell.setRowspan(rowspan);
                return headerCell;
        }

        private PdfPCell tablaCelda(PdfPTable table, Integer padding, Integer colspan) {
                PdfPCell cell = new PdfPCell(table);
                cell.setPadding(padding);
                cell.setColspan(colspan);
                return cell;
        }

        /**
         * funcion para estilizar el concepto y su valor dentro de una tabla, haciendolo
         * mas manejable
         * 
         * @param columnas
         * @param anchoColumnas
         * @param celdaConcepto
         * @param celdaValor
         * @return tabla ya estilizada para poder crear una celda
         * @throws DocumentException
         */
        private PdfPTable combinarCeldaConceptoYValor(Integer columnas, float[] anchoColumnas, PdfPCell celdaConcepto,
                        PdfPCell celdaValor) throws DocumentException {
                PdfPTable tabla = new PdfPTable(columnas);
                tabla.setWidths(anchoColumnas);
                tabla.addCell(celdaConcepto).setBorder(0);
                tabla.addCell(celdaValor).setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(lineaVacia(tabla));
                return tabla;
        }
}
