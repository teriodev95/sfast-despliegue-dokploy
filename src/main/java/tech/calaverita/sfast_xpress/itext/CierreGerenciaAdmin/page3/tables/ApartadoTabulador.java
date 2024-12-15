package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables;

import java.text.DecimalFormat;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import tech.calaverita.sfast_xpress.DTOs.TablaFlujoEfectivoDTO;
import tech.calaverita.sfast_xpress.DTOs.TablaFlujoEfectivoDTO.Tabulador.Billete;
import tech.calaverita.sfast_xpress.DTOs.TablaFlujoEfectivoDTO.Tabulador.Moneda;

/**
 * Clase para generar las tablas de billetes y monedas, y para formatear
 * su contenido de manera adecuada en el documento PDF.
 */
public class ApartadoTabulador {
        DecimalFormat formatoMonto = new DecimalFormat("#,###,##0.00");
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 9f);

        public PdfPTable creaTablaTabulador(TablaFlujoEfectivoDTO data) throws DocumentException {
                PdfPTable tabla = new PdfPTable(2);
                tabla.setWidthPercentage(100);
                tabla.setKeepTogether(true);

                tabla.addCell(lineaVacia(tabla));
                PdfPCell block = new PdfPCell(new Phrase("TABULADOR", FontFactory.getFont(FontFactory.HELVETICA, 10)));
                block.setColspan(tabla.getNumberOfColumns());
                block.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                block.setBorder(0);
                tabla.addCell(block);

                PdfPTable tabuladorBilletes = encabezadosTabulador("Billetes");
                List<tech.calaverita.sfast_xpress.DTOs.TablaFlujoEfectivoDTO.Tabulador.Billete> billetes = data
                                .getTabulador().getBilletes();
                for (Billete billete : billetes) {
                        tabuladorBilletes.addCell(new PdfPCell(estiloCeldaAlineadoCentradoMonto(
                                        new Phrase(formatoMonto.format(billete.getDenominacion()), regularFont))))
                                        .setBorder(0);
                        agregarCeldaConValor(tabuladorBilletes, billete.getCantidad(), regularFont, false, false);
                        agregarCeldaConValor(tabuladorBilletes, billete.getCantidad2(), regularFont, false, false);
                        agregarCeldaConValor(tabuladorBilletes, billete.getSubTotal(), regularFont, true, true);
                }
                tabuladorBilletes.addCell(estiloCeldaAlineadoCentrado(new Phrase(" ", regularFont))).setBorder(0);
                tabuladorBilletes.addCell(creaCeldaExpansionPersonalizada(new Phrase("TOTAL EN BILLETES", regularFont),
                                Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, 1, 2, 0));
                agregarCeldaConValor(tabuladorBilletes, data.getTabulador().getTotalEnBilletes(), regularFont, true,
                                true);

                PdfPTable tabuladorMonedas = encabezadosTabulador("Monedas");
                List<Moneda> monedas = data.getTabulador().getMonedas();
                for (Moneda moneda : monedas) {
                        tabuladorMonedas.addCell(new PdfPCell(estiloCeldaAlineadoCentradoMonto(
                                        new Phrase(formatoMonto.format(moneda.getDenominacion()), regularFont))))
                                        .setBorder(0);
                        agregarCeldaConValor(tabuladorMonedas, moneda.getCantidad(), regularFont, false, false);
                        agregarCeldaConValor(tabuladorMonedas, moneda.getCantidad2(), regularFont, false, false);
                        agregarCeldaConValor(tabuladorMonedas, moneda.getSubTotal(), regularFont, true, true);
                }
                tabuladorMonedas.addCell(estiloCeldaAlineadoCentrado(new Phrase(" ", regularFont))).setBorder(0);
                tabuladorMonedas.addCell(creaCeldaExpansionPersonalizada(new Phrase("TOTAL EN MONEDAS", regularFont),
                                Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, 1, 2, 0));
                agregarCeldaConValor(tabuladorMonedas, data.getTabulador().getTotalEnMonedas(), regularFont, true,
                                true);
                tabuladorMonedas.addCell(lineaVacia(tabuladorMonedas));

                PdfPCell celdaBilletes = new PdfPCell(tabuladorBilletes);
                PdfPCell celdaMonedas = new PdfPCell(tabuladorMonedas);

                celdaBilletes.setPadding(15);
                celdaBilletes.setPaddingTop(0);
                celdaMonedas.setPadding(15);
                celdaMonedas.setPaddingTop(0);

                tabla.addCell(celdaBilletes).setBorder(0);
                tabla.addCell(celdaMonedas).setBorder(0);

                return tabla;
        }

        private PdfPCell creaCeldaExpansionPersonalizada(Phrase phrase, Integer alignH, Integer alignV, Integer rowspan,
                        Integer colspan, Integer border) {
                PdfPCell celda = new PdfPCell(phrase);
                celda.setHorizontalAlignment(alignH);
                celda.setVerticalAlignment(alignV);
                celda.setColspan(colspan);
                celda.setRowspan(rowspan);
                celda.setBorder(border);
                return celda;
        }

        /**
         * Crea encabezados para la tabla de tabulador.
         *
         * @param tipo Tipo de tabulador (Billetes o Monedas).
         * @return Tabla con los encabezados.
         * @throws DocumentException Si ocurre un error al crear la tabla.
         */
        private PdfPTable encabezadosTabulador(String tipo) throws DocumentException {
                PdfPTable tabla = new PdfPTable(4);
                tabla.setWidths(new float[] { 2, 1, 1, 2 });
                tabla.addCell(creaCeldaExpansionPersonalizada(new Phrase(tipo, boldFont), Element.ALIGN_CENTER,
                                Element.ALIGN_MIDDLE, 1, tabla.getNumberOfColumns(), 0));
                tabla.addCell(creaCeldaExpansionPersonalizada(new Phrase("Denominacion", boldFont),
                                Element.ALIGN_CENTER,
                                Element.ALIGN_MIDDLE, 2, 1, 15));
                tabla.addCell(creaCeldaExpansionPersonalizada(new Phrase("Cantidad", boldFont), Element.ALIGN_CENTER,
                                Element.ALIGN_MIDDLE, 1, 2, 15));
                tabla.addCell(creaCeldaExpansionPersonalizada(new Phrase("Sub-total", boldFont), Element.ALIGN_CENTER,
                                Element.ALIGN_MIDDLE, 2, 1, 15));
                tabla.addCell(creaCeldaExpansionPersonalizada(new Phrase("1", boldFont), Element.ALIGN_CENTER,
                                Element.ALIGN_MIDDLE, 1, 1, 15));
                tabla.addCell(creaCeldaExpansionPersonalizada(new Phrase("2", boldFont), Element.ALIGN_CENTER,
                                Element.ALIGN_MIDDLE, 1, 1, 15));
                return tabla;
        }

        private PdfPCell lineaVacia(PdfPTable table) {
                PdfPCell celda = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 1)));
                celda.setBorder(0);
                celda.setColspan(table.getNumberOfColumns());
                return celda;
        }

        private PdfPCell estiloCeldaAlineadoCentrado(Phrase phrase) {
                PdfPCell celda = new PdfPCell(phrase);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(4);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
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
        private PdfPTable estiloCeldaAlineadoCentradoMonto(Phrase phrase) throws DocumentException {
                PdfPTable tablaContenedora = new PdfPTable(2);
                tablaContenedora.setWidths(new float[] { 1, 2 });
                PdfPCell celda = new PdfPCell(new Phrase(" $", regularFont));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(PdfPCell.LEFT | PdfPCell.TOP | PdfPCell.BOTTOM);
                tablaContenedora.addCell(celda);
                celda = new PdfPCell(phrase);
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBorder(PdfPCell.RIGHT | PdfPCell.TOP | PdfPCell.BOTTOM);
                tablaContenedora.addCell(celda);
                return tablaContenedora;
        }

        /**
         * Define el estilo de la celda dependiendo si es una cantidad monetaria o
         * entera, normal o total
         * 
         * @param tabla       donde se añadira la celda
         * @param valor       cantidad a evaluar
         * @param font        fuente para estilizar la presentacion del valor
         * @param esMonetario booleano
         * @param esTotal     booleano
         * @throws DocumentException
         */
        private void agregarCeldaConValor(PdfPTable tabla, double valor, Font font, boolean esMonetario,
                        boolean esTotal)
                        throws DocumentException {
                PdfPCell celda;
                if (valor == 0) {
                        celda = esTotal
                                        ? esMonetario ? new PdfPCell(estiloCeldaAlineadoCentradoMonto(new Phrase("-")))
                                                        : estiloCeldaAlineadoCentrado(new Phrase("0", regularFont))
                                        : esMonetario ? new PdfPCell(new Phrase("")) : new PdfPCell(new Phrase(""));
                        // cell.setBorder(0);
                } else {
                        Phrase frase = esMonetario ? new Phrase(formatoMonto.format(valor), font)
                                        : new Phrase(String.valueOf(((int) valor)), font);
                        celda = esTotal
                                        ? esMonetario ? new PdfPCell(estiloCeldaAlineadoCentradoMonto(frase))
                                                        : new PdfPCell(estiloCeldaAlineadoCentrado(frase))
                                        : esMonetario ? new PdfPCell(estiloCeldaAlineadoCentradoMonto(frase))
                                                        : new PdfPCell(estiloCeldaAlineadoCentrado(frase));
                }
                tabla.addCell(celda);
        }

}
