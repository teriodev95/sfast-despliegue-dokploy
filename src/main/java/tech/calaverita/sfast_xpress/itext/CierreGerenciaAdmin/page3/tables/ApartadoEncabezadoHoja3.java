package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import tech.calaverita.sfast_xpress.DTOs.TablaFlujoEfectivoDTO;

public class ApartadoEncabezadoHoja3 {
        public PdfPTable creaEncabezado(TablaFlujoEfectivoDTO data) throws DocumentException {
                PdfPTable table = new PdfPTable(12); // Número de columnas visibles en la tabla.
                table.setWidthPercentage(100);
                table.addCell(estilizarCelda(
                                new Phrase("FLUJO DE EFECTIVO", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                                Element.ALIGN_CENTER, 2, 6, 1)).setBorder(0);
                table.addCell(estilizarCelda(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                                Element.ALIGN_CENTER, 2, 6, 1)).setBorder(0);
                table.addCell(lineaVacia(table));

                PdfPTable managerContainer = combinarCeldaConceptoYValor(2, new float[] { 1, 3 },
                                estilizarCelda(new Phrase("Gerente: ", FontFactory.getFont(FontFactory.HELVETICA, 8)),
                                                Element.ALIGN_RIGHT, 2, 1, 1),
                                estilizarCelda(new Phrase(data.getGerente(),
                                                FontFactory.getFont(FontFactory.HELVETICA, 8)),
                                                Element.ALIGN_CENTER, 2, 1, 1));
                PdfPTable tablaContenedoraZona = combinarCeldaConceptoYValor(2, new float[] { 1, 1 },
                                estilizarCelda(new Phrase("Zona: ", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                                                Element.ALIGN_RIGHT, 2, 1, 1),
                                estilizarCelda(new Phrase(data.getZona(),
                                                FontFactory.getFont(FontFactory.HELVETICA, 10)),
                                                Element.ALIGN_CENTER, 3, 1, 1));
                PdfPTable tablaContenedoraSemana = combinarCeldaConceptoYValor(2, new float[] { 1, 1 },
                                estilizarCelda(new Phrase("Sem: ", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                                                Element.ALIGN_RIGHT,
                                                2, 1, 1),
                                estilizarCelda(
                                                new Phrase(String.valueOf(data.getSemana()),
                                                                FontFactory.getFont(FontFactory.HELVETICA, 10)),
                                                Element.ALIGN_CENTER, 3, 1, 1));
                PdfPTable tablaContenedoraAnio = combinarCeldaConceptoYValor(2, new float[] { 1, 1 },
                                estilizarCelda(new Phrase("Año: ", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                                                Element.ALIGN_RIGHT,
                                                2, 1, 1),
                                estilizarCelda(
                                                new Phrase(String.valueOf(data.getAnio()),
                                                                FontFactory.getFont(FontFactory.HELVETICA, 10)),
                                                Element.ALIGN_CENTER, 3, 1, 1));

                table.addCell(convertirTablaACelda(managerContainer, 2, 6)).setBorder(0);
                table.addCell(convertirTablaACelda(tablaContenedoraZona, 2, 2)).setBorder(0);
                table.addCell(convertirTablaACelda(tablaContenedoraSemana, 2, 2)).setBorder(0);
                table.addCell(convertirTablaACelda(tablaContenedoraAnio, 2, 2)).setBorder(0);

                table.setSpacingAfter(10);

                return table;

        }

        private PdfPCell lineaVacia(PdfPTable tabla) {
                PdfPCell celda = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 1)));
                celda.setBorder(0);
                celda.setColspan(tabla.getNumberOfColumns());
                return celda;
        }

        private PdfPTable combinarCeldaConceptoYValor(Integer columnas, float[] anchoColumnas, PdfPCell celdaConcepto,
                        PdfPCell celdaValor) throws DocumentException {
                PdfPTable tabla = new PdfPTable(columnas);
                tabla.setWidths(anchoColumnas);
                tabla.addCell(celdaConcepto).setBorder(0);
                tabla.addCell(celdaValor).setVerticalAlignment(Element.ALIGN_MIDDLE);
                return tabla;
        }

        private PdfPCell estilizarCelda(Phrase frase, Integer align, Integer paddingBottom, Integer colspan,
                        Integer rowspan) {
                PdfPCell celda = new PdfPCell(frase);
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
