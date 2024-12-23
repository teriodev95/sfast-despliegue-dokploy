package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables;

import java.text.DecimalFormat;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.ClienteMetrica;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.Metrica;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.MetricasClientes;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.MetricasFinancieras;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.ObjetivosVentas;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaNumerosGerencia;

/**
 * Clase que representa la sección de "Números de la Gerencia" en el documento
 * PDF.
 * Esta clase contiene métodos para crear tablas que muestran métricas
 * financieras,
 * métricas de clientes y objetivos de ventas.
 */
public class ApartadoNumerosDeLaGerencia {

        static DecimalFormat formatoMonto = new DecimalFormat("#,###,##0.00");

        public PdfPTable creaTablaNumerosDeLaGerencia(TablaNumerosGerencia data) throws DocumentException {

                BaseColor azulClaro = new BaseColor(52, 113, 235);
                // TABLA METRICAS FINANCIERAS
                PdfPTable tablaMetricasFinancieras = new PdfPTable(4); // 4 columnas: Concepto, Semana Anterior, Actual,
                                                                       // Diferencia
                tablaMetricasFinancieras
                                .addCell(new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 7))))
                                .setBorder(0);
                tablaMetricasFinancieras
                                .addCell(new PdfPCell(new Phrase("Semana Ant.",
                                                FontFactory.getFont(FontFactory.HELVETICA, 7))))
                                .setBorder(0);
                tablaMetricasFinancieras
                                .addCell(new PdfPCell(
                                                new Phrase("Actual", FontFactory.getFont(FontFactory.HELVETICA, 7))))
                                .setBorder(0);
                tablaMetricasFinancieras
                                .addCell(new PdfPCell(new Phrase("Diferencia",
                                                FontFactory.getFont(FontFactory.HELVETICA, 7))))
                                .setBorder(0);

                MetricasFinancieras metricas = data.getMetricasFinancieras();

                agregaFilaMetricasFinancieras(tablaMetricasFinancieras, "DEBIT", metricas.getDebit());
                agregaFilaMetricasFinancieras(tablaMetricasFinancieras, "Cob. PURA", metricas.getCobPura());
                agregaFilaMetricasFinancieras(tablaMetricasFinancieras, "Faltante", metricas.getFaltante());
                agregaFilaMetricasFinancieras(tablaMetricasFinancieras, "Eficiencia", metricas.getEficiencia());
                tablaMetricasFinancieras.addCell(bloqueVacioRelleno(tablaMetricasFinancieras.getNumberOfColumns()));

                // TABLA METRICAS CLIENTES
                PdfPTable tablaMetricasClientes = new PdfPTable(5); // 4 columnas: Concepto, Anterior, Actual,
                                                                    // Diferencia
                tablaMetricasClientes.setWidths(new float[] { 1, 2, 1, 1, 1 });
                tablaMetricasClientes.addCell(bloqueVacioRelleno(2));
                tablaMetricasClientes
                                .addCell(new PdfPCell(
                                                new Phrase("Ant.", FontFactory.getFont(FontFactory.HELVETICA, 7))))
                                .setBorder(0);
                tablaMetricasClientes
                                .addCell(new PdfPCell(
                                                new Phrase("Actual", FontFactory.getFont(FontFactory.HELVETICA, 7))))
                                .setBorder(0);
                tablaMetricasClientes
                                .addCell(new PdfPCell(
                                                new Phrase("Dif.", FontFactory.getFont(FontFactory.HELVETICA, 7))))
                                .setBorder(0);

                MetricasClientes clientes = data.getMetricasClientes();

                agregaFilaMetricasClientes(tablaMetricasClientes, "# de Clientes", clientes.getClientes());
                agregaFilaMetricasClientes(tablaMetricasClientes, "# de NO pagos", clientes.getNoPagos());
                agregaFilaMetricasClientes(tablaMetricasClientes, "# P.Reducidos", clientes.getpReducidos());
                agregaFilaMetricasClientes(tablaMetricasClientes, "# Clts LIQUID.", clientes.getCltsLiquid());
                tablaMetricasClientes.addCell(bloqueVacioRelleno(tablaMetricasClientes.getNumberOfColumns()));

                tablaMetricasClientes.addCell(bloqueVacioRelleno(2));
                PdfPCell conceptoDesc = new PdfPCell(
                                new Phrase("Desq. x Liq.", FontFactory.getFont(FontFactory.HELVETICA, 7)));
                conceptoDesc.setColspan(2);
                conceptoDesc.setHorizontalAlignment(Element.ALIGN_RIGHT);
                conceptoDesc.setBorder(PdfPCell.NO_BORDER);
                tablaMetricasClientes.addCell(conceptoDesc);
                tablaMetricasClientes
                                .addCell(new PdfPCell(new Phrase(String.valueOf(clientes.getDescuentosPorLiquidacion()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 7))))
                                .setHorizontalAlignment(Element.ALIGN_CENTER);

                // TABLA OBJETIVOS VENTAS
                PdfPTable tablaObjetivosVentas = new PdfPTable(3); // 5 columnas: Objetivo, Actual, Ventas Nuevas,
                                                                   // Renovaciones,
                                                                   // Total
                tablaObjetivosVentas.setWidths(new float[] { 3, 2, 2 });

                ObjetivosVentas ventas = data.getObjetivosVentas();
                tablaObjetivosVentas.addCell(bloqueVacioRelleno(tablaObjetivosVentas.getNumberOfColumns()));
                tablaObjetivosVentas.addCell(bloqueVacioRelleno(tablaObjetivosVentas.getNumberOfColumns()));

                agregarFilaObjetivosVentas(tablaObjetivosVentas,
                                new Phrase("Objet. VENTAS", FontFactory.getFont(FontFactory.HELVETICA, 7)),
                                new Phrase("$ " + formatoMonto.format(ventas.getObjetivo()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 6, azulClaro)),
                                new Phrase(String.valueOf(ventas.getActual()) + "%",
                                                FontFactory.getFont(FontFactory.HELVETICA, 7, azulClaro)));

                agregarFilaObjetivosVentas(tablaObjetivosVentas,
                                new Phrase("Ventas Nuevas", FontFactory.getFont(FontFactory.HELVETICA, 7)),
                                new Phrase("$ " + formatoMonto.format(ventas.getVentasNuevas()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 6)),
                                new Phrase(String.valueOf(ventas.getClientesNuevos()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 7, azulClaro)));

                agregarFilaObjetivosVentas(tablaObjetivosVentas,
                                new Phrase("Renovaciones", FontFactory.getFont(FontFactory.HELVETICA, 7)),
                                new Phrase("$ " + formatoMonto.format(ventas.getRenovaciones()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 6)),
                                new Phrase(String.valueOf(ventas.getCantidadRenovaciones()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 7, azulClaro)));

                agregarFilaObjetivosVentas(tablaObjetivosVentas,
                                new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA, 7)),
                                new Phrase("$ " + formatoMonto.format(ventas.getTotal()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 6)),
                                new Phrase(String.valueOf(ventas.getCantidadTotal()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 7, azulClaro)));

                tablaObjetivosVentas.addCell(bloqueVacioRelleno(tablaObjetivosVentas.getNumberOfColumns()));

                // TABLA CONTENEDORA
                PdfPTable tabla = new PdfPTable(3);
                tabla.setWidthPercentage(100);
                PdfPCell block = new PdfPCell(
                                new Phrase("NUMEROS DE LA GERENCIA", FontFactory.getFont(FontFactory.HELVETICA, 10)));
                block.setColspan(tabla.getNumberOfColumns());
                block.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                block.setPaddingBottom(5);
                tabla.addCell(block);
                tabla.addCell(celdaConTablaSinBorde(tablaMetricasFinancieras));
                tabla.addCell(celdaConTablaSinBorde(tablaMetricasClientes));
                tabla.addCell(celdaConTablaSinBorde(tablaObjetivosVentas));
                tabla.setSpacingAfter(10);
                return tabla;
        }

        private PdfPCell celdaConTablaSinBorde(PdfPTable table) {
                PdfPCell tablecell = new PdfPCell(table);
                tablecell.setBorder(0);
                return tablecell;
        }

        private PdfPCell bloqueVacioRelleno(Integer colspan) {
                PdfPCell cell = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 2)));
                cell.setBorder(0);
                cell.setColspan(colspan);
                return cell;
        }

        /**
         * Agrega una fila a la tabla de métricas de clientes con los datos
         * especificados.
         *
         * @param tabla    Tabla a la que se agregará las filas.
         * @param concepto Concepto que se mostrará en la primera columna.
         * @param metrica  Metrica a agregar.
         */
        private static void agregaFilaMetricasClientes(PdfPTable tabla, String concepto, ClienteMetrica metrica) {
                PdfPCell celdaConcepto = new PdfPCell(
                                new Phrase(concepto, FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 7)));
                celdaConcepto.setColspan(2);
                celdaConcepto.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celdaConcepto).setBorder(0);
                if (concepto == "# P.Reducidos") {
                        if (metrica.getAnterior() == 0) {
                                tabla.addCell(new PdfPCell(
                                                new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 7))))
                                                .setBorder(0);
                                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(metrica.getActual()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 7))));
                                tabla.addCell(new PdfPCell(
                                                new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 7))))
                                                .setBorder(0);
                        } else {
                                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(metrica.getAnterior()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 7))));
                                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(metrica.getActual()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 7))));
                                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(metrica.getDiferencia()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 7))));
                        }
                } else if (concepto == "# Clts LIQUID.") {
                        if (metrica.getDiferencia() == 0) {
                                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(metrica.getAnterior()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 7))));
                                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(metrica.getActual()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 7))));
                                tabla.addCell(new PdfPCell(
                                                new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 7))))
                                                .setBorder(0);
                        } else {
                                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(metrica.getAnterior()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 7))));
                                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(metrica.getActual()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 7))));
                                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(metrica.getDiferencia()),
                                                FontFactory.getFont(FontFactory.HELVETICA, 7))));
                        }
                } else {
                        tabla.addCell(new PdfPCell(
                                        new Phrase(String.valueOf(metrica.getAnterior()),
                                                        FontFactory.getFont(FontFactory.HELVETICA, 7))));
                        tabla.addCell(new PdfPCell(
                                        new Phrase(String.valueOf(metrica.getActual()),
                                                        FontFactory.getFont(FontFactory.HELVETICA, 7))));
                        tabla.addCell(new PdfPCell(new Phrase(String.valueOf(metrica.getDiferencia()),
                                        FontFactory.getFont(FontFactory.HELVETICA, 7))));
                }
        }

        /**
         * Agrega una fila a la tabla de métricas financieras con los datos
         * especificados.
         *
         * @param tabla    Tabla a la que se agregará la fila.
         * @param concepto Concepto que se mostrará en la primera columna.
         * @param metrica  Metrica a agregar.
         */
        private static void agregaFilaMetricasFinancieras(PdfPTable tabla, String concepto, Metrica metrica) {
                BaseColor azulClaro = new BaseColor(52, 113, 235);
                BaseColor azulMasClaro = new BaseColor(86, 133, 227);
                // BaseColor rosa = new BaseColor(235, 52, 137);
                PdfPCell celdaConcepto = new PdfPCell(
                                new Phrase(concepto, FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 7)));
                celdaConcepto.setBorder(0);
                celdaConcepto.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celdaConcepto);
                if (concepto == "Eficiencia") {
                        tabla.addCell(new PdfPCell(new Phrase(String.valueOf(metrica.getSemanaAnterior()) + "%",
                                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, BaseColor.WHITE))))
                                        .setBackgroundColor(BaseColor.RED);
                        tabla.addCell(new PdfPCell(new Phrase(String.valueOf(metrica.getActual()) + "%",
                                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, BaseColor.WHITE))))
                                        .setBackgroundColor(BaseColor.RED);
                        tabla.addCell(new Phrase(String.valueOf(metrica.getDiferencia()) + "%",
                                        FontFactory.getFont(FontFactory.HELVETICA, 7)));
                } else {
                        tabla.addCell(new PdfPCell(new Phrase(
                                        metrica.getSemanaAnterior() != 0
                                                        ? "$ " + formatoMonto.format(metrica.getSemanaAnterior())
                                                        : "$ - ",
                                        FontFactory.getFont(FontFactory.HELVETICA, 6))))
                                        .setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(new PdfPCell(
                                        new Phrase(metrica.getActual() != 0
                                                        ? "$ " + formatoMonto.format(metrica.getActual())
                                                        : "$ - ",
                                                        FontFactory.getFont(FontFactory.HELVETICA, 6, azulClaro))))
                                        .setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(new PdfPCell(new Phrase(
                                        metrica.getDiferencia() != 0
                                                        ? "$ " + formatoMonto.format(metrica.getDiferencia())
                                                        : "$ - ",
                                        FontFactory.getFont(FontFactory.HELVETICA, 6, azulMasClaro))))
                                        .setHorizontalAlignment(Element.ALIGN_RIGHT);
                }
        }

        /**
         * Agrega una fila a la tabla de objetivos de ventas con los datos
         * especificados.
         *
         * @param tabla  Tabla a la que se agregará la fila.
         * @param frase1 Concepto que se mostrará en la primera columna.
         * @param frase2 Metrica relacionada al concepto que se mostrará en la segunda
         *               columna.
         * @param frase3 otra Metrica nueva que se mostrará en la tercera columna.
         */
        private void agregarFilaObjetivosVentas(PdfPTable tabla, Phrase frase1, Phrase frase2, Phrase frase3) {
                PdfPCell celda = new PdfPCell(frase1);
                celda.setBorder(PdfPCell.NO_BORDER);
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celda);
                celda = new PdfPCell(frase2);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
                celda = new PdfPCell(frase3);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
        }
}
