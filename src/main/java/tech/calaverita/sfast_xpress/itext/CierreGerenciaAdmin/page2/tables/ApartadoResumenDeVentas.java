package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page2.tables;

import java.text.DecimalFormat;
import java.util.List;

//import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page2.tables.classes.TablaResumenDeVentas;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page2.tables.classes.TablaResumenDeVentas.DetalleVenta;

/**
 * Esta clase es responsable de crear una tabla que presenta el resumen de las
 * ventas,
 * incluyendo detalles como fecha, agente, cliente, tipo, nivel, plazo, monto y
 * primer pago.
 */
public class ApartadoResumenDeVentas {
    DecimalFormat formatoMonto = new DecimalFormat("#,###,##0.00");
    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
    Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 7.5f);

    public PdfPTable creaTablaResumenVentas(TablaResumenDeVentas data) throws DocumentException {
        PdfPTable tabla = new PdfPTable(9);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[] { 0.25f, 1.2f, 0.5f, 2.2f, 0.7f, 0.7f, 0.8f, 0.7f, 0.7f });

        String[] conceptos = { "#", "FECHA", "# AG", "NOMBRE DEL CLIENTE", "TIPO", "NIVEL", "PLAZO", "MONTO",
                "1er. PAGO" };

        for (String header : conceptos) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, boldFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(headerCell);
        }
        List<DetalleVenta> detalleVentas = data.getDetalleVentas();
        for (DetalleVenta venta : detalleVentas) {
            tabla.addCell(estiloCeldaAlineada(new Phrase(String.valueOf(venta.getNumero()), regularFont)));
            tabla.addCell(estiloCeldaAlineada(new Phrase(venta.getFecha(), regularFont)));
            tabla.addCell(estiloCeldaAlineada(new Phrase(venta.getAgente(), regularFont)));
            tabla.addCell(estiloCeldaAlineada(new Phrase(venta.getNombreCliente(), regularFont)));
            tabla.addCell(estiloCeldaAlineada(new Phrase(venta.getTipo(), regularFont)));
            tabla.addCell(estiloCeldaAlineada(new Phrase(venta.getNivel(), regularFont)));
            tabla.addCell(estiloCeldaAlineada(new Phrase(venta.getPlazo(), regularFont)));
            // tabla.addCell(estiloCeldaAlineada(new
            // Phrase(formatoMonto.format(venta.getMonto()), regularFont)));
            tabla.addCell(new PdfPCell(
                    estiloCeldaAlineadaMonto(new Phrase(formatoMonto.format(venta.getMonto()), regularFont))))
                    .setBorder(0);
            tabla.addCell(new PdfPCell(
                    estiloCeldaAlineadaMonto(new Phrase(formatoMonto.format(venta.getPrimerPago()), regularFont))))
                    .setBorder(0);
        }

        rellenoVacioFilas(detalleVentas, tabla);

        return tabla;
    }

    private PdfPCell estiloCeldaAlineada(Phrase phrase) {
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(4);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    /**
     * Crea una celda para el monto con un formato específico.
     *
     * @param phrase Contenido del monto.
     * @return PdfPTable que contiene la celda del monto y el simbolo $.
     * @throws DocumentException Si ocurre un error al crear la tabla interna.
     */
    private PdfPTable estiloCeldaAlineadaMonto(Phrase phrase) throws DocumentException {
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidths(new float[] { 1, 2 });
        PdfPCell celda = new PdfPCell(new Phrase(" $", regularFont));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(PdfPCell.LEFT | PdfPCell.TOP | PdfPCell.BOTTOM);
        tabla.addCell(celda);
        celda = new PdfPCell(phrase);
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(PdfPCell.RIGHT | PdfPCell.TOP | PdfPCell.BOTTOM);
        tabla.addCell(celda);
        return tabla;
    }

    /**
     * Rellena filas vacías en la tabla si hay menos de 30 ventas.
     *
     * @param ventas Lista de ventas para verificar su tamaño.
     * @param tabla  Tabla a la que se agregarán las filas vacías.
     */
    private void rellenoVacioFilas(List<DetalleVenta> ventas, PdfPTable tabla) {
        if (ventas.size() < 30) {
            for (Integer i = 0; i < (30 - ventas.size()); i++) {
                tabla.addCell(estiloCeldaAlineada(new Phrase(String.valueOf(i + ventas.size() + 1), regularFont)));
                PdfPCell nullCellID = new PdfPCell(new Phrase(" ", regularFont));
                nullCellID.setHorizontalAlignment(Element.ALIGN_CENTER);
                // nullCellID.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
                nullCellID.setColspan(tabla.getNumberOfColumns() - 1);
                nullCellID.setBorder(0);
                tabla.addCell(nullCellID);
            }
        }
    }
}
