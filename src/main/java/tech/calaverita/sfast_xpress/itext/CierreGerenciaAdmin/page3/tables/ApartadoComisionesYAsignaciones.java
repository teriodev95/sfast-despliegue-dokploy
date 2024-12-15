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
import tech.calaverita.sfast_xpress.DTOs.TablaFlujoEfectivoDTO.ComisionesYAsignaciones.AgenteComision;
import tech.calaverita.sfast_xpress.DTOs.TablaFlujoEfectivoDTO.ComisionesYAsignaciones.TotalesComisiones;

public class ApartadoComisionesYAsignaciones {
    DecimalFormat formatoMonto = new DecimalFormat("#,###,##0.00");
    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
    Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 7.5f);

    public PdfPTable creaTablaComisionesYAsignaciones(TablaFlujoEfectivoDTO data) throws DocumentException {
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 1.25f, 0.7f, 1, 1, 1, 1, 1, 1 });

        String[] headers = { "AGENTE", "AGENCIA", "ASINACIONES", "COMISIONES POR COBRANZA", "COMISIONES POR VENTAS",
                "BONOS", "DESC. POR LIQUIDACION", "TOTAL" };

        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, boldFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }

        List<AgenteComision> agentes = data.getComisionesYAsignaciones().getAgentes();
        for (AgenteComision agente : agentes) {
            table.addCell(estiloCeldaAlineadoCentrado(
                    new Phrase(agente.getNombre(), FontFactory.getFont(FontFactory.HELVETICA, 6.5f))));
            table.addCell(estiloCeldaAlineadoCentrado(new Phrase(agente.getAgencia(), regularFont)));

            agregarCeldaConValor(table, agente.getAsignaciones(), regularFont, true, false);
            agregarCeldaConValor(table, agente.getComisionesPorCobranza(), regularFont, true, false);
            agregarCeldaConValor(table, agente.getComisionesPorVentas(), regularFont, true, false);
            agregarCeldaConValor(table, agente.getBonos(), regularFont, true, false);
            agregarCeldaConValor(table, agente.getDescPorLiquidacion(), regularFont, true, false);
            agregarCeldaConValor(table, agente.getTotal(), regularFont, true, true);

        }
        rellenoNuloAgentes(agentes, table);
        table.addCell(lineaVacia(table));

        TotalesComisiones totales = data.getComisionesYAsignaciones().getTotales();
        table.addCell(new PdfPCell(new Phrase("", regularFont))).setBorder(0);
        table.addCell(
                estiloCeldaAlineadoDerecho(new Phrase("TOTALES", FontFactory.getFont(FontFactory.HELVETICA, 6.5f))))
                .setBorder(0);
        agregarCeldaConValor(table, totales.getAsignaciones(), regularFont, true, true);
        agregarCeldaConValor(table, totales.getComisionesPorCobranza(), regularFont, true, true);
        agregarCeldaConValor(table, totales.getComisionesPorVentas(), regularFont, true, true);
        agregarCeldaConValor(table, totales.getBonos(), regularFont, true, true);
        agregarCeldaConValor(table, totales.getDescPorLiquidacion(), regularFont, true, true);
        agregarCeldaConValor(table, totales.getTotal(), regularFont, true, true);

        table.setSpacingAfter(5);

        return table;
    }

    private PdfPCell lineaVacia(PdfPTable tabla) {
        PdfPCell celda = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 1)));
        celda.setBorder(0);
        celda.setColspan(tabla.getNumberOfColumns());
        return celda;
    }

    private PdfPCell estiloCeldaAlineadoCentrado(Phrase phrase) {
        PdfPCell celda = new PdfPCell(phrase);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(4);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return celda;
    }

    private PdfPCell estiloCeldaAlineadoDerecho(Phrase phrase) {
        PdfPCell celda = new PdfPCell(phrase);
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setPadding(4);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return celda;
    }

    /**
     * Estiliza una tabla de 2 columnas con el simbolo $ y el monto, para la
     * creacion de una sola celda
     * 
     * @param phrase
     * @param nulo
     * @return tablaContenedora la cual se transformara a una celda
     * @throws DocumentException
     */
    private PdfPTable estiloCeldaAlineadoCentradoMonto(Phrase phrase, boolean nulo) throws DocumentException {
        PdfPTable tablaContenedora = new PdfPTable(2);
        tablaContenedora.setWidths(new float[] { 1, 2 });
        PdfPCell celda = new PdfPCell(new Phrase(" $", regularFont));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        if (nulo) {
            celda.setBorder(0);
        } else {
            celda.setBorder(PdfPCell.LEFT | PdfPCell.TOP | PdfPCell.BOTTOM);
        }
        tablaContenedora.addCell(celda);
        celda = new PdfPCell(phrase);
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        if (nulo) {
            celda.setBorder(0);
        } else {
            celda.setBorder(PdfPCell.RIGHT | PdfPCell.TOP | PdfPCell.BOTTOM);
        }
        tablaContenedora.addCell(celda);
        return tablaContenedora;
    }

    /**
     * Relleno de flias en agencias vacias, colocando una celda con '0'
     * 
     * @param agentes
     * @param tabla
     */
    private void rellenoNuloAgentes(List<AgenteComision> agentes, PdfPTable tabla) {
        if (agentes.size() < 12) {
            for (Integer i = 0; i < (12 - agentes.size()); i++) {
                tabla.addCell(estiloCeldaAlineadoCentrado(new Phrase(" ", regularFont))).setBorder(0);
                tabla.addCell(estiloCeldaAlineadoCentrado(new Phrase("0", regularFont)));
                PdfPCell celda = new PdfPCell(new Phrase(" ", regularFont));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                // nullCellID.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
                celda.setColspan(tabla.getNumberOfColumns() - 2);
                celda.setBorder(0);
                tabla.addCell(celda);
            }
        }
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
    private void agregarCeldaConValor(PdfPTable tabla, double valor, Font font, boolean esMonetario, boolean esTotal)
            throws DocumentException {
        PdfPCell celda;
        if (valor == 0) {
            celda = esTotal
                    ? esMonetario ? new PdfPCell(estiloCeldaAlineadoCentradoMonto(new Phrase("-"), true))
                            : estiloCeldaAlineadoCentrado(new Phrase("0", regularFont))
                    : esMonetario ? new PdfPCell(new Phrase("")) : new PdfPCell(new Phrase(""));
            celda.setBorder(0);
        } else {
            Phrase frase = new Phrase(formatoMonto.format(valor), font);
            celda = esTotal
                    ? esMonetario ? new PdfPCell(estiloCeldaAlineadoCentradoMonto(frase, false))
                            : new PdfPCell(estiloCeldaAlineadoCentrado(frase))
                    : esMonetario ? new PdfPCell(estiloCeldaAlineadoCentradoMonto(frase, false))
                            : new PdfPCell(estiloCeldaAlineadoCentrado(frase));
        }
        tabla.addCell(celda);
    }
}
