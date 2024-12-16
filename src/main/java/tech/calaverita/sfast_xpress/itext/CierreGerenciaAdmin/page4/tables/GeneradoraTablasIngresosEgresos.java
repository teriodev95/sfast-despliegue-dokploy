package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables;

import java.text.DecimalFormat;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * La clase GeneradoraTablasIngresosEgresos es responsable de almacenar las
 * funciones
 * para la creacion de las tablas de ingresos y egresos.
 * Algunas Clases sobreescriben ciertas funciones por pequeños detalles
 */
public class GeneradoraTablasIngresosEgresos {
    DecimalFormat formatoMonto = new DecimalFormat("#,###,###.00");
    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
    Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 8f);
    BaseColor azulClaro = new BaseColor(52, 113, 235);
    BaseColor rosa = new BaseColor(235, 52, 137);

    /**
     * Genera un encabezado para el par de tablas de ingresos y egresos.
     *
     * @param frase   Frase personalizada para el encabezado de la tabla.
     * @param colspan Cantidad de columnas que ocupará la celda del encabezado.
     * @return PdfPCell que contiene el encabezado de la tabla.
     * @throws DocumentException si hay un problema al crear la celda.
     */
    public PdfPCell generarEncabezadoIngresosConceptoEgresos(String frase, Integer colspan) throws DocumentException {
        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidths(new float[] { 1.5f, 2, 1.5f });
        PdfPCell celda = new PdfPCell(new Phrase("INGRESOS", boldFont));
        celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        celda.setBorderColor(azulClaro);
        tabla.addCell(celda);
        celda = new PdfPCell(new Phrase(frase, boldFont));
        celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        celda.setBorder(0);
        tabla.addCell(celda);
        celda = new PdfPCell(new Phrase("EGRESOS", boldFont));
        celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        celda.setBorderColor(rosa);
        tabla.addCell(celda);
        PdfPCell celdaContenedora = new PdfPCell(tabla);
        celdaContenedora.setColspan(colspan);
        return celdaContenedora;
    }

    /**
     * Genera una tabla con los encabezados especificados y un color para las
     * celdas.
     *
     * @param encabezados Array de cadenas que representan los encabezados de la
     *                    tabla.
     * @param color       Color de los bordes de las celdas.
     * @return PdfPTable generada con los encabezados.
     * @throws DocumentException si hay un problema al crear la tabla.
     */
    public PdfPTable generarTablaYEncabezados(String[] encabezados, BaseColor color) throws DocumentException {
        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidths(new float[] { 1, 1.2f, 0.8f });
        for (String encabezado : encabezados) {
            PdfPCell celdaEncabezado = new PdfPCell(new Phrase(encabezado, boldFont));
            celdaEncabezado.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaEncabezado.setBorderColor(color);
            tabla.addCell(celdaEncabezado);
        }

        return tabla;
    }

    /**
     * Agrega valores a la tabla, se utilizan frases por si se necesita estilizar
     * antes.
     *
     * @param frase1 Frase para la celda de la columna 1.
     * @param frase2 Frase para la celda de la columna 2.
     * @param frase3 Frase para la celda de la columna 3.
     * @param color  Color de los bordes de las celdas, ya sea ingresos o egresos.
     * @param tabla  Tabla a la que se agregarán los valores.
     * @throws DocumentException si hay un problema al crear las celdas.
     */
    public void agregarValoresATabla(Phrase frase1, Phrase frase2, Phrase frase3, BaseColor color, PdfPTable tabla)
            throws DocumentException {
        PdfPCell celda = estiloCeldaCentrado(frase1);
        celda.setBorderColor(color);
        tabla.addCell(celda);
        celda = estiloCeldaCentrado(frase2);
        celda.setBorderColor(color);
        tabla.addCell(celda);
        celda = new PdfPCell(estiloCeldaMonto(frase3));
        celda.setBorderColor(color);
        tabla.addCell(celda);
    }

    /**
     * Agrega filas vacías y un subtotal a la tabla.
     *
     * @param data   Lista de datos para la tabla.
     * @param tabla  Tabla a la que se agregarán los datos.
     * @param limite Número mínimo de filas esperadas en la tabla.
     * @param monto  Monto total a mostrar como subtotal.
     * @param color  Color de los bordes de las celdas.
     * @throws DocumentException si hay un problema al crear las celdas.
     */
    public <T> void agregarRellenoYSubtotal(List<T> data, PdfPTable tabla, Integer limite, double monto,
            BaseColor color) throws DocumentException {
        rellenoFilasNulas(data, tabla, limite, color);
        tabla.addCell(estiloCeldaCentrado(new Phrase(" ", regularFont))).setBorder(0);
        tabla.addCell(estiloCeldaAlineadoDerecha(new Phrase("Sub Total", regularFont))).setBorder(0);
        if (monto == 0) {
            tabla.addCell(new PdfPCell(estiloCeldaMonto(new Phrase("-", regularFont)))).setBorderColor(color);
        } else {
            tabla.addCell(new PdfPCell(estiloCeldaMonto(new Phrase(formatoMonto.format(monto), regularFont))))
                    .setBorderColor(color);
        }
        tabla.addCell(lineaVacia(tabla));
    }

    /**
     * Rellena filas nulas en la tabla si hay menos datos de los esperados.
     *
     * @param data   Lista de datos para verificar.
     * @param tabla  Tabla a la que se agregarán filas vacías.
     * @param limite Número mínimo de filas esperadas en la tabla.
     * @param color  Color de los bordes de las celdas.
     */
    public <T> void rellenoFilasNulas(List<T> data, PdfPTable tabla, Integer limite, BaseColor color) {
        if (data.size() < limite) {
            for (Integer i = 0; i < (limite - data.size()); i++) {
                PdfPCell celda = new PdfPCell(new Phrase(" ", regularFont));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setBorderColor(color);
                tabla.addCell(celda);
                tabla.addCell(celda);
                tabla.addCell(celda);
            }
        }
    }

    public PdfPCell lineaVacia(PdfPTable table) {
        PdfPCell cell = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 1)));
        cell.setBorder(0);
        cell.setColspan(table.getNumberOfColumns());
        return cell;
    }

    public PdfPCell estiloCeldaCentrado(Phrase phrase) {
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    public PdfPCell estiloCeldaAlineadoDerecha(Phrase phrase) {
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(2);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    /**
     * Generacion de una tabla para estilizarlo a una sola celda, juntando un valor
     * y el simbolo $
     * 
     * @param phrase Frase que contiene el monto.
     * @return tablaContenedora para ser convertida a una celda
     * @throws DocumentException
     */
    public PdfPTable estiloCeldaMonto(Phrase phrase) throws DocumentException {
        PdfPTable tablaContenedora = new PdfPTable(2);
        tablaContenedora.setWidths(new float[] { 1, 2 });
        PdfPCell celda = new PdfPCell(new Phrase(" $", regularFont));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(0);
        tablaContenedora.addCell(celda);
        celda = new PdfPCell(phrase);
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(0);
        tablaContenedora.addCell(celda);
        return tablaContenedora;
    }
}
