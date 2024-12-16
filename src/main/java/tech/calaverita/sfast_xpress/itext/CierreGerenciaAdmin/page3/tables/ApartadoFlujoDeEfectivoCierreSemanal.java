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

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables.classes.TablaFlujoEfectivo;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables.classes.TablaFlujoEfectivo.CierreSemanalCobranzaAgencias.AgenteCobranza;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables.classes.TablaFlujoEfectivo.CierreSemanalCobranzaAgencias.TotalesCobranza;

public class ApartadoFlujoDeEfectivoCierreSemanal {
    DecimalFormat formatoMonto = new DecimalFormat("#,###,##0.00");
    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
    Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 7.5f);

    public PdfPTable creaTablasFlujoDeEfectivo(TablaFlujoEfectivo data) throws DocumentException {
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 1.25f, 1, 1, 1, 1, 1, 1, 1, 1 });

        String[] conceptos = { "AGENTE", "AGENCIA", "NO. Clientes", "NO Pagos", "No. de Clientes con Liquidacion",
                "Cobranza PURA", "Liquidaciones", "Monto Excedente", "Multas" };

        // Añadir encabezados a la tabla.
        PdfPCell encabezado = new PdfPCell(
                new Phrase("CIERRE SEMANAL DE COBRANZA DE AGENCIAS", FontFactory.getFont(FontFactory.HELVETICA, 10)));
        encabezado.setColspan(table.getNumberOfColumns());
        encabezado.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        encabezado.setBorder(0);
        encabezado.setPaddingBottom(5);
        table.addCell(encabezado);

        for (String header : conceptos) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, boldFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }
        List<AgenteCobranza> agentes = data.getCierreSemanalCobranzaAgencias().getAgentes();
        for (AgenteCobranza agente : agentes) {
            table.addCell(estiloCeldaAlineadoCentrado(
                    new Phrase(agente.getNombre(), FontFactory.getFont(FontFactory.HELVETICA, 6.5f))));
            table.addCell(estiloCeldaAlineadoCentrado(new Phrase(agente.getAgencia(), regularFont)));
            table.addCell(estiloCeldaAlineadoCentrado(new Phrase(String.valueOf(agente.getNoClientes()), regularFont)));
            agregarCeldaConValor(table, agente.getNoPagos(), regularFont, false, false);
            agregarCeldaConValor(table, agente.getNoClientesConLiquidacion(), regularFont, false, false);
            agregarCeldaConValor(table, agente.getCobranzaPura(), regularFont, true, false);
            agregarCeldaConValor(table, agente.getLiquidaciones(), regularFont, true, false);
            agregarCeldaConValor(table, agente.getMontoExcedente(), regularFont, true, false);
            agregarCeldaConValor(table, agente.getMultas(), regularFont, true, false);
        }

        rellenoNulo(agentes, table);
        table.addCell(lineaVacia(table));

        TotalesCobranza totales = data.getCierreSemanalCobranzaAgencias().getTotales();
        table.addCell(new PdfPCell(new Phrase("", regularFont))).setBorder(0);
        table.addCell(estiloCeldaAlineadoDerecho(new Phrase("TOTALES", regularFont))).setBorder(0);
        table.addCell(estiloCeldaAlineadoCentrado(new Phrase(String.valueOf(totales.getNoClientes()), regularFont)));
        agregarCeldaConValor(table, totales.getNoPagos(), regularFont, false, true);
        agregarCeldaConValor(table, totales.getNoClientesConLiquidacion(), regularFont, false, true);
        agregarCeldaConValor(table, totales.getCobranzaPura(), regularFont, true, true);
        agregarCeldaConValor(table, totales.getLiquidaciones(), regularFont, true, true);
        agregarCeldaConValor(table, totales.getMontoExcedente(), regularFont, true, true);
        agregarCeldaConValor(table, totales.getMultas(), regularFont, true, true);

        table.setSpacingAfter(10);
        return table;

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

    private void rellenoNulo(List<AgenteCobranza> agentes, PdfPTable tabla) {
        if (agentes.size() < 12) {
            for (Integer i = 0; i < (12 - agentes.size()); i++) {
                PdfPCell celda = new PdfPCell(new Phrase(" ", regularFont));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                // nullCellID.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
                celda.setColspan(tabla.getNumberOfColumns());
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
