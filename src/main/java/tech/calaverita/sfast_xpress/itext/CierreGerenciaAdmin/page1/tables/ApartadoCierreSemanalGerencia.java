package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaCierreSemanalGerencia;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaCierreSemanalGerencia.Egresos.EgrAdministrativos;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaCierreSemanalGerencia.Egresos.EgrOperativos;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaCierreSemanalGerencia.Egresos.EgrOperativos.ComisionesYBonos;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaCierreSemanalGerencia.Egresos.EgrOperativos.GastosOperativos;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaCierreSemanalGerencia.Egresos.EgrOperativos.Ventas;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaCierreSemanalGerencia.Ingresos.IngAdministrativos;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaCierreSemanalGerencia.Ingresos.IngOperativos;

/**
 * La clase ApartadoCierreSemanalGerencia genera tablas de ingresos y egresos
 * a partir de datos de cierre semanal para la gerencia.
 */
public class ApartadoCierreSemanalGerencia {
    DecimalFormat formatoMonto = new DecimalFormat("#,###,##0.00");
    BaseColor azulClaro = new BaseColor(52, 113, 235);
    BaseColor rosa = new BaseColor(235, 52, 137);

    /**
     * Genera una tabla contenedora con las tablas de detalles de cierre semanal de
     * la gerencia.
     *
     * @param data Instancia de TablaCierreSemanalGerencia con los datos a mostrar.
     * @return Una tabla `PdfPTable` con el contenido de ingresos y egresos,
     *         estructurada y formateada.
     * @throws DocumentException si ocurre un error al crear la tabla.
     */
    public PdfPTable creaTablasDetallesCierreSemanalGerencia(TablaCierreSemanalGerencia data) throws DocumentException {

        // TABLA INGRESOS ADMINISTRATIVOS
        IngAdministrativos ingresosAdministrativos = data.getIngresos().getAdministrativos();
        PdfPTable tablaIngresosAdministrativos = new PdfPTable(3);
        tablaIngresosAdministrativos.setWidths(new float[] { 6, 1, 2 });

        Field[] camposIngresosAdministrativos = IngAdministrativos.class.getDeclaredFields();
        agregarFilas(camposIngresosAdministrativos, tablaIngresosAdministrativos, "totalDeAsignaciones",
                ingresosAdministrativos, azulClaro);

        // TABLA EGRESOS ADMINISTRATIVOS
        EgrAdministrativos egresosAdministrativos = data.getEgresos().getAdministrativos();
        PdfPTable tablaEgresosAdministrativos = new PdfPTable(3);
        tablaEgresosAdministrativos.setWidths(new float[] { 6, 1, 2 });

        Field[] camposEgresosAdministativos = EgrAdministrativos.class.getDeclaredFields();
        agregarFilas(camposEgresosAdministativos, tablaEgresosAdministrativos, "totalDeAsignaciones",
                egresosAdministrativos, rosa);

        // TABLA INGRESOS OPERATIVOS
        IngOperativos ingresosOperativos = data.getIngresos().getOperativos();
        PdfPTable tablaIngresosOperativos = new PdfPTable(3);
        tablaIngresosOperativos.setWidths(new float[] { 6, 1, 2 });

        Field[] camposIngresosOperativos = IngOperativos.class.getDeclaredFields();
        agregarFilas(camposIngresosOperativos, tablaIngresosOperativos, "totalDeCobranza", ingresosOperativos,
                azulClaro);

        // TABLA EGRESOS OPERATIVOS
        EgrOperativos egresosOperativos = data.getEgresos().getOperativos();

        Ventas egresosOperativosVentas = egresosOperativos.getVentas();
        ComisionesYBonos egresosOperativosCyB = data.getEgresos().getOperativos().getComisionesYBonos();
        PdfPTable tablaEgresosOperativos = new PdfPTable(3);
        tablaEgresosOperativos.setWidths(new float[] { 6, 1, 2 });

        Field[] camposEgrOp = Ventas.class.getDeclaredFields();
        agregarFilas(camposEgrOp, tablaEgresosOperativos, "totalDeVentas", egresosOperativosVentas, rosa);

        Field[] camposEgrCyB = ComisionesYBonos.class.getDeclaredFields();
        agregarFilas(camposEgrCyB, tablaEgresosOperativos, "totalDeBonosYComisiones", egresosOperativosCyB, rosa);

        // BLOQUE TOTAL DE INGRESOS
        PdfPTable bloqueTotalDeIngresos = new PdfPTable(3);
        bloqueTotalDeIngresos.setWidths(new float[] { 6, 1, 2 });
        bloqueTotalDeIngresos.addCell(estiloCeldaConcepto("TOTAL DE INGRESOS", azulClaro)).setBorder(0);
        bloqueTotalDeIngresos.addCell(estiloCeldaSimboloTotal(azulClaro));
        bloqueTotalDeIngresos.addCell(
                estiloCeldaMontoTotal(formatoMonto.format(data.getIngresos().getTotalDeIngresos()), azulClaro));
        bloqueTotalDeIngresos.addCell(renglonSeparador(bloqueTotalDeIngresos.getNumberOfColumns()));

        // TABLA GASTOS OPERATIVOS
        GastosOperativos gastosOp = egresosOperativos.getGastosOperativos();
        Field[] campos = GastosOperativos.class.getDeclaredFields();
        PdfPTable tablaGastosOperativos = new PdfPTable(3);
        tablaGastosOperativos.setWidths(new float[] { 5, 1, 3 });
        agregarFilas(campos, tablaGastosOperativos, "totalDeGastos", gastosOp, rosa);

        // BLOQUE EFECTIVO A ENTREGAR
        PdfPTable bloqueEfectivoAEntregar = new PdfPTable(3);
        bloqueEfectivoAEntregar.setWidths(new float[] { 6, 1, 4 });
        bloqueEfectivoAEntregar.addCell(estiloCeldaConceptoCursiva("EFECTIVO A ENTREGAR")).setBorder(0);
        bloqueEfectivoAEntregar.addCell(estiloCeldaSimbolo(
                new Phrase(" $", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, azulClaro)), BaseColor.BLACK));
        bloqueEfectivoAEntregar.addCell(estiloCeldaMonto(new Phrase(formatoMonto.format(data.getEfectivoAEntregar()),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, azulClaro)), BaseColor.BLACK));
        bloqueEfectivoAEntregar.addCell(renglonSeparador(bloqueEfectivoAEntregar.getNumberOfColumns()));

        // BLOQUE TOTAL DE EGRESOS
        PdfPTable bloqueTotalDeEgresos = new PdfPTable(3);
        bloqueTotalDeEgresos.setWidths(new float[] { 6, 1, 2 });
        bloqueTotalDeEgresos.addCell(estiloCeldaConcepto("TOTAL DE EGRESOS", rosa)).setBorder(0);
        bloqueTotalDeEgresos.addCell(estiloCeldaSimboloTotal(rosa));
        bloqueTotalDeEgresos
                .addCell(estiloCeldaMontoTotal(formatoMonto.format(data.getEgresos().getTotalDeEgresos()), rosa));
        bloqueTotalDeEgresos.addCell(renglonSeparador(bloqueTotalDeEgresos.getNumberOfColumns()));

        // TABLA CONTENEDORA
        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[] { 1, 4, 1, 4, 1 });
        table.setWidthPercentage(100);
        colocarEncabezado(
                new Phrase("DETALLE DE CIERRE SEMANAL DE LA GERENCIA", FontFactory.getFont(FontFactory.HELVETICA, 10)),
                table.getNumberOfColumns(), 5, BaseColor.BLACK, table);

        colocarEncabezado(new Phrase("INGRESOS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, azulClaro)), 2, 2,
                azulClaro, table);
        table.addCell(renglonSeparador(1));
        colocarEncabezado(new Phrase("EGRESOS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, rosa)), 2, 2, rosa,
                table);

        colocarTablasALaPar(new Phrase("ADMINISTRATIVOS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7)),
                table.getNumberOfColumns(), Element.ALIGN_CENTER, table, tablaIngresosAdministrativos,
                tablaEgresosAdministrativos);
        colocarTablasALaPar(new Phrase("OPERATIVOS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7)),
                table.getNumberOfColumns(), Element.ALIGN_CENTER, table, tablaIngresosOperativos,
                tablaEgresosOperativos);
        table.addCell(renglonSeparador(3));
        colocarTablasALaPar(new Phrase("GASTOS OPERATIVOS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7)), 2,
                Element.ALIGN_LEFT, table, bloqueTotalDeIngresos, tablaGastosOperativos);
        colocarTablasALaPar(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 0.1F)),
                table.getNumberOfColumns(), Element.ALIGN_CENTER, table, bloqueEfectivoAEntregar, bloqueTotalDeEgresos);

        return table;
    }

    /**
     * Coloca una celda de encabezado en la tabla principal con estilo definido.
     * 
     * @param frase         frase 'Phrase' estilizada la cual sera el encabezado
     * @param colspan       rango de columnas que abarcara la celda
     * @param paddingBottom padding que se le dara al encabezado dentro de la celda
     * @param color         'BaseColor' que se usara para colorear el borde de la
     *                      celda
     * @param tabla         de tipo 'PdfPTable' a la cual se agregara la celda
     */
    private void colocarEncabezado(Phrase frase, Integer colspan, Integer paddingBottom, BaseColor color,
            PdfPTable tabla) {
        PdfPCell encabezado = new PdfPCell(frase);
        encabezado.setColspan(colspan);
        encabezado.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        encabezado.setBorderColor(color);
        encabezado.setPaddingBottom(paddingBottom);
        tabla.addCell(encabezado);
    }

    /**
     * Alinea dos tablas en paralelo bajo una frase de encabezado en la tabla
     * principal.
     * 
     * @param frase           que actuara como el concepto de las dos tablas
     * @param colspanConcepto tamaño de columnas que se le dara a la celda del
     *                        concepto
     * @param alineadoFrase   alineado de la frase concepto
     * @param tablaPrincipal  tabla a la cual se agregaran las celdas generadas
     * @param tablaIngresos
     * @param tablaEgresos
     */
    private void colocarTablasALaPar(Phrase frase, Integer colspanConcepto, Integer alineadoFrase,
            PdfPTable tablaPrincipal, PdfPTable tablaIngresos, PdfPTable tablaEgresos) {
        PdfPCell aspecto = new PdfPCell(frase);
        aspecto.setColspan(colspanConcepto);
        aspecto.setHorizontalAlignment(alineadoFrase);
        aspecto.setBorder(0);
        tablaPrincipal.addCell(aspecto);
        tablaPrincipal.addCell(renglonSeparador(1));
        tablaPrincipal.addCell(tablaSinBorde(tablaIngresos));
        tablaPrincipal.addCell(renglonSeparador(1));
        tablaPrincipal.addCell(tablaSinBorde(tablaEgresos));
        tablaPrincipal.addCell(renglonSeparador(1));
    }

    private PdfPCell tablaSinBorde(PdfPTable table) {
        PdfPCell tablecell = new PdfPCell(table);
        tablecell.setBorder(0);
        return tablecell;
    }

    /**
     * Formatea y agrega las filas de datos a la tabla.
     * 
     * @param campos    campos de la clase cuyos datos se agregan
     * @param tabla     'PdfPTable' donde se agregaran las filas de datos
     * @param oddString campo el cual recibe un formateo y estilizado cursivo y sin
     *                  borde (generalmente los TOTALES)
     * @param data      objeto que contiene la informacion
     * @param color     'BaseColor' para el borde dela tabla
     */
    private void agregarFilas(Field[] campos, PdfPTable tabla, String oddString, Object data, BaseColor color) {
        for (Field campo : campos) {
            campo.setAccessible(true);
            try {
                String formattedName = formateoNombreCampo(campo.getName());
                if (campo.getName() == oddString) {
                    tabla.addCell(new PdfPCell(estiloCeldaConceptoCursiva(formattedName)));
                } else {
                    tabla.addCell(new PdfPCell(estiloCeldaConcepto(formattedName, color)));
                }
                tabla.addCell(new PdfPCell(estiloCeldaSimbolo(
                        new Phrase(" $", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, color)), color)));
                tabla.addCell(new PdfPCell(estiloCeldaMonto(new Phrase(formatoMonto.format((Double) campo.get(data)),
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, color)), color)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private PdfPCell renglonSeparador(Integer cols) {
        PdfPCell cell = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 2)));
        cell.setBorder(0);
        cell.setColspan(cols);
        return cell;
    }

    private PdfPCell estiloCeldaConcepto(String text, BaseColor color) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9)));
        cell.setBorderColor(color);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    /**
     * Celda con estilo personalizado para mostrar el simbolo $ a la izquierda de la
     * celda
     * 
     * @param colorBorde
     * @param simbolo    frase estilizada donde se añade el simbolo $
     * @return cell
     */
    private PdfPCell estiloCeldaSimbolo(Phrase simbolo, BaseColor colorBorde) {
        PdfPCell cell = new PdfPCell(simbolo);
        cell.setBorderColor(colorBorde);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(PdfPCell.LEFT | PdfPCell.TOP | PdfPCell.BOTTOM);
        return cell;
    }

    /**
     * Celda con estilo personalizado para mostrar el total a la derecha de la celda
     * 
     * @param colorBorde
     * @param value      frase estilizada donde se añade la cantidad
     * @return cell
     */
    private PdfPCell estiloCeldaMonto(Phrase value, BaseColor colorBorde) {
        PdfPCell cell = new PdfPCell(value);
        cell.setBorderColor(colorBorde);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(PdfPCell.RIGHT | PdfPCell.TOP | PdfPCell.BOTTOM);
        return cell;
    }

    /**
     * Celda con estilo personalizado para mostrar el simbolo $ a la izquierda de la
     * celda
     * 
     * @param colorCelda color que se usara con el borde y elfondo de la celda
     * @return cell
     */
    private PdfPCell estiloCeldaSimboloTotal(BaseColor colorCelda) {
        PdfPCell cell = new PdfPCell(
                new Phrase(" $", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE)));
        cell.setBorderColor(colorCelda);
        cell.setBackgroundColor(colorCelda);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(PdfPCell.LEFT | PdfPCell.TOP | PdfPCell.BOTTOM);
        return cell;
    }

    /**
     * Celda con estilo personalizado para mostrar el total a la derecha de la celda
     * 
     * @param colorCelda color que se usara con el borde y elfondo de la celda
     * @return cell
     */
    private PdfPCell estiloCeldaMontoTotal(String value, BaseColor color) {
        PdfPCell cell = new PdfPCell(
                new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE)));
        cell.setBorderColor(color);
        cell.setBackgroundColor(color);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(PdfPCell.RIGHT | PdfPCell.TOP | PdfPCell.BOTTOM);
        return cell;
    }

    private PdfPCell estiloCeldaConceptoCursiva(String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text.toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 8)));
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    /**
     * obtiene el nombre del campo de la clase en camelcase y la fromatea separando
     * las palabras
     * 
     * @param fieldName nombre del campo de la clase
     * @return formattedName nombre del campo formateado
     */
    public static String formateoNombreCampo(String fieldName) {
        StringBuilder formattedName = new StringBuilder();
        formattedName.append(Character.toUpperCase(fieldName.charAt(0)));
        for (int i = 1; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            if (Character.isUpperCase(c)) {
                formattedName.append(" ");
            }
            formattedName.append(c);
        }
        return formattedName.toString().trim();
    }
}
