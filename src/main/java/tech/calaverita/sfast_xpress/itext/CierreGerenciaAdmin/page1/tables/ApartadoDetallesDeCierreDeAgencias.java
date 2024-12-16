package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables;

import java.text.DecimalFormat;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaDetallesCierreAgencias;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaDetallesCierreAgencias.Agencia;

/**
 * Esta clase genera tablas en PDF que detallan el cierre de agencias,
 * incluyendo la información de cada agencia y sus métricas financieras y de
 * clientes.
 */
public class ApartadoDetallesDeCierreDeAgencias {
    DecimalFormat formatoMonto = new DecimalFormat("#,###,##0.00");
    BaseColor azulClaro = new BaseColor(52, 113, 235);
    BaseColor rosa = new BaseColor(235, 52, 137);

    /**
     * Crea una tabla con detalles de cierre de agencias.
     *
     * @param data Objeto TablaDetallesCierreAgencias que contiene información de
     *             las agencias.
     * @return PdfPTable Tabla con los detalles del cierre de agencias.
     * @throws DocumentException en caso de error durante la creación de la tabla.
     */
    public PdfPTable creaParteInformacionAgencia(TablaDetallesCierreAgencias data) throws DocumentException {
        List<Agencia> agencias = (List<Agencia>) data.getAgencias();
        PdfPTable table = new PdfPTable(14);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });

        String[] conceptos = { "AGENCIAS: ", "AGENTES: ", "DÉBITO: ", "Cob. Puro: ", "Faltante: ", "Eficiencia: ",
                "Ventas: " };

        PdfPCell encabezadoTabla = new PdfPCell(
                new Phrase("DETALLE DE CIERRE DE AGENCIAS", FontFactory.getFont(FontFactory.HELVETICA, 10)));
        encabezadoTabla.setColspan(table.getNumberOfColumns());
        encabezadoTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        encabezadoTabla.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.TOP);
        encabezadoTabla.setPaddingBottom(5);
        table.addCell(encabezadoTabla);

        PdfPCell celdaAgencias = new PdfPCell(
                new Phrase(data.getTotalAgencias(), FontFactory.getFont(FontFactory.HELVETICA, 7)));
        celdaAgencias.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celdaAgencias);

        agregaCeldaConcepto(new Phrase(conceptos[0], FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 6)), 1,
                table, 15);
        for (Agencia agencia : agencias) {
            table.addCell(new PdfPCell(new Phrase((String) agencia.getId() != null ? agencia.getId().toString() : "-",
                    FontFactory.getFont(FontFactory.HELVETICA, 9, azulClaro))))
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
        }
        rellenoCeldasVaciasEnAgencias(agencias, table, azulClaro, "0");

        celdaAgencias = new PdfPCell(
                new Phrase(data.getTotalAgentes(), FontFactory.getFont(FontFactory.HELVETICA, 7, rosa)));
        celdaAgencias.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celdaAgencias);

        agregaCeldaConcepto(new Phrase(conceptos[1], FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 6)), 1,
                table, 15); // 15 es el valor de todos los bordes 1,2,4,8
        for (Agencia agencia : agencias) {
            table.addCell(
                    new PdfPCell(new Phrase((String) agencia.getAgente() != null ? agencia.getAgente().toString() : "-",
                            FontFactory.getFont(FontFactory.HELVETICA, 5, azulClaro))))
                    .setBorder(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.BOTTOM);
            ;
        }
        rellenoCeldasVaciasEnAgencias(agencias, table, azulClaro, "-");

        agregaCeldaConcepto(new Phrase(conceptos[2], FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 6)), 2,
                table, PdfPCell.NO_BORDER);
        for (Agencia agencia : agencias) {
            table.addCell(new PdfPCell(new Phrase("$ " + formatoMonto.format(agencia.getDebito()),
                    FontFactory.getFont(FontFactory.HELVETICA, 6))));
        }
        rellenoCeldasVaciasEnAgencias(agencias, table, BaseColor.BLACK, "-");

        agregaCeldaConcepto(
                new Phrase(conceptos[3], FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 7, BaseColor.GRAY)), 2,
                table, PdfPCell.NO_BORDER);
        for (Agencia agencia : agencias) {
            table.addCell(new PdfPCell(new Phrase("$ " + formatoMonto.format(agencia.getCobPuro()),
                    FontFactory.getFont(FontFactory.HELVETICA, 6))));
        }
        rellenoCeldasVaciasEnAgencias(agencias, table, BaseColor.BLACK, "-");

        agregaCeldaConcepto(new Phrase(conceptos[4], FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 7, rosa)),
                2, table, PdfPCell.NO_BORDER);
        for (Agencia agencia : agencias) {
            table.addCell(new PdfPCell(new Phrase("$ " + formatoMonto.format(agencia.getFaltante()),
                    FontFactory.getFont(FontFactory.HELVETICA, 6))));
        }
        rellenoCeldasVaciasEnAgencias(agencias, table, BaseColor.BLACK, "-");

        agregaCeldaConcepto(new Phrase(conceptos[5], FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 7)), 2,
                table, PdfPCell.NO_BORDER);
        for (Agencia agencia : agencias) {
            table.addCell(new PdfPCell(new Phrase(agencia.getEficiencia() + "%",
                    FontFactory.getFont(FontFactory.HELVETICA, 7, azulClaro))));
        }
        rellenoCeldasVaciasEnAgencias(agencias, table, BaseColor.BLACK, "-");

        agregaCeldaConcepto(
                new Phrase(conceptos[6], FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 7, azulClaro)), 2,
                table, PdfPCell.NO_BORDER);
        for (Agencia agencia : agencias) {
            table.addCell(new PdfPCell(
                    new Phrase(agencia.getVentas() != null ? "$ " + formatoMonto.format(agencia.getVentas()) : "-",
                            FontFactory.getFont(FontFactory.HELVETICA, 6))));
        }
        rellenoCeldasVaciasEnAgencias(agencias, table, BaseColor.BLACK, "-");

        return table;
    }

    /**
     * Genera una tabla con el estado de clientes 'S. ant' y 'S. act'.
     *
     * @param data Objeto TablaDetallesCierreAgencias con datos de agencias y
     *             clientes.
     * @return PdfPTable con la información de clientes de cada agencia.
     * @throws DocumentException en caso de error durante la creación de la tabla.
     */
    public PdfPTable creaParteClientesSantSact(TablaDetallesCierreAgencias data) throws DocumentException {
        List<Agencia> agencias = (List<Agencia>) data.getAgencias();
        PdfPTable table = new PdfPTable(28); // Número de columnas visibles en la tabla.
        table.setWidthPercentage(100);
        table.setWidths(
                new float[] { 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });

        PdfPCell encabezadoTabla = new PdfPCell(new Phrase(" "));
        encabezadoTabla.setColspan(4);
        encabezadoTabla.setBorder(PdfPCell.NO_BORDER);
        table.addCell(encabezadoTabla);

        for (Integer i = 0; i < 12; i++) {
            PdfPCell celdaSant = new PdfPCell(new Phrase("S.ant", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8)));
            PdfPCell celdaSact = new PdfPCell(new Phrase("S.act", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8)));
            agregaCeldasSantSact(celdaSant, 0, celdaSact, 0, table);

        }

        agregaCeldaConcepto(new Phrase("# Clientes", FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 7)), 4,
                table, 0);
        for (Agencia agencia : agencias) {
            PdfPCell celdaSant = new PdfPCell(
                    new Phrase(agencia.getClientes() != null ? String.valueOf(agencia.getClientes().getSant()) : "-",
                            FontFactory.getFont(FontFactory.HELVETICA, 7)));
            PdfPCell celdaSact = new PdfPCell(
                    new Phrase(agencia.getClientes() != null ? String.valueOf(agencia.getClientes().getSact()) : "-",
                            FontFactory.getFont(FontFactory.HELVETICA, 7)));
            agregaCeldasSantSact(celdaSant, PdfPCell.LEFT | PdfPCell.TOP, celdaSact, PdfPCell.RIGHT | PdfPCell.TOP,
                    table);

        }
        rellenoCeldasVaciasClientesSantSact(agencias, table, PdfPCell.TOP | PdfPCell.LEFT,
                PdfPCell.TOP | PdfPCell.RIGHT);

        agregaCeldaConcepto(
                new Phrase("# de NO pagos", FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 7, rosa)), 4, table,
                0);
        for (Agencia agencia : agencias) {
            PdfPCell celdaSant = new PdfPCell(
                    new Phrase(agencia.getNoPagos() != null ? String.valueOf(agencia.getNoPagos().getSant()) : "-",
                            FontFactory.getFont(FontFactory.HELVETICA, 7)));
            PdfPCell celdaSact = new PdfPCell(
                    new Phrase(agencia.getNoPagos() != null ? String.valueOf(agencia.getNoPagos().getSact()) : "-",
                            FontFactory.getFont(FontFactory.HELVETICA, 7)));
            agregaCeldasSantSact(celdaSant, PdfPCell.LEFT, celdaSact, PdfPCell.RIGHT, table);

        }
        rellenoCeldasVaciasClientesSantSact(agencias, table, PdfPCell.LEFT, PdfPCell.RIGHT);

        agregaCeldaConcepto(
                new Phrase("# P. Reducidos", FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 7, BaseColor.GRAY)),
                4, table, 0);
        for (Agencia agencia : agencias) {
            PdfPCell celdaSant = new PdfPCell(new Phrase(
                    agencia.getPagosReducidos() != null ? String.valueOf(agencia.getPagosReducidos().getSant()) : "-",
                    FontFactory.getFont(FontFactory.HELVETICA, 7)));
            PdfPCell celdaSact = new PdfPCell(new Phrase(
                    agencia.getPagosReducidos() != null ? String.valueOf(agencia.getPagosReducidos().getSact()) : "-",
                    FontFactory.getFont(FontFactory.HELVETICA, 7)));
            agregaCeldasSantSact(celdaSant, PdfPCell.LEFT, celdaSact, PdfPCell.RIGHT, table);
        }
        rellenoCeldasVaciasClientesSantSact(agencias, table, PdfPCell.LEFT, PdfPCell.RIGHT);

        agregaCeldaConcepto(
                new Phrase("# Clts con LIQUID", FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 6.5F)), 4, table,
                0);
        for (Agencia agencia : agencias) {
            PdfPCell celdaSant = new PdfPCell(new Phrase(agencia.getClientesConLiquidacion() != null
                    ? String.valueOf(agencia.getClientesConLiquidacion().getSant())
                    : "-", FontFactory.getFont(FontFactory.HELVETICA, 7)));
            PdfPCell celdaSact = new PdfPCell(new Phrase(agencia.getClientesConLiquidacion() != null
                    ? String.valueOf(agencia.getClientesConLiquidacion().getSact())
                    : "-", FontFactory.getFont(FontFactory.HELVETICA, 7)));
            agregaCeldasSantSact(celdaSant, PdfPCell.LEFT | PdfPCell.BOTTOM, celdaSact,
                    PdfPCell.RIGHT | PdfPCell.BOTTOM, table);
        }
        rellenoCeldasVaciasClientesSantSact(agencias, table, PdfPCell.BOTTOM | PdfPCell.LEFT,
                PdfPCell.BOTTOM | PdfPCell.RIGHT);
        table.setSpacingAfter(10);

        return table;

    }

    /**
     * Agrega una celda con un concepto en la tabla.
     *
     * @param frase   Texto del concepto.
     * @param colspan Número de columnas que ocupa la celda.
     * @param tabla   Tabla a la que se añadirá la celda.
     * @param border  Bordes de la celda.
     */
    private void agregaCeldaConcepto(Phrase frase, Integer colspan, PdfPTable tabla, Integer border) {
        PdfPCell celdaAspecto = new PdfPCell(frase);
        celdaAspecto.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaAspecto.setColspan(colspan);
        celdaAspecto.setBorder(border);
        tabla.addCell(celdaAspecto);
    }

    private void rellenoCeldasVaciasEnAgencias(List<Agencia> agencias, PdfPTable table, BaseColor color,
            String relleno) {
        if (agencias.size() < 12) {
            for (Integer i = 0; i < (12 - agencias.size()); i++) {
                PdfPCell headerNullCellID = new PdfPCell(
                        new Phrase(relleno, FontFactory.getFont(FontFactory.HELVETICA, 9, color)));
                headerNullCellID.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerNullCellID.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
                table.addCell(headerNullCellID);
            }
        }
    }

    private void rellenoCeldasVaciasClientesSantSact(List<Agencia> agencias, PdfPTable table, Integer bordesCeldaSant,
            Integer bordesCeldaSact) {
        if (agencias.size() < 12) {
            for (Integer i = 0; i < (12 - agencias.size()); i++) {
                PdfPCell headerNullCellID = new PdfPCell(new Phrase("-"));
                headerNullCellID.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerNullCellID.setBorder(bordesCeldaSant);
                table.addCell(headerNullCellID);
                headerNullCellID.setBorder(bordesCeldaSact);
                table.addCell(headerNullCellID);
            }
        }
    }

    /**
     * Agrega una pareja de celdas 'S.ant' y 'S.act' en la tabla.
     *
     * @param celdaSant      Celda con informacion 'S.ant'.
     * @param celdaSantBorde Bordes de la celda 'S.ant'.
     * @param celdaSact      Celda con informacion 'S.act'.
     * @param celdaSactBorde Bordes de la celda 'S.act'.
     * @param tabla          Tabla a la que se añaden las celdas.
     */
    private void agregaCeldasSantSact(PdfPCell celdaSant, Integer celdaSantBorde, PdfPCell celdaSact,
            Integer celdaSactBorde, PdfPTable tabla) {
        celdaSant.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaSant.setBorder(celdaSantBorde);
        celdaSact.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaSact.setBorder(celdaSactBorde);
        tabla.addCell(celdaSant);
        tabla.addCell(celdaSact);
    }
}
