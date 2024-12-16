package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables;

import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * La clase ApartadoBloquesDeFirmas genera una tabla de firmas para el PDF.
 * Crea dos bloques de firma: uno para el responsable del cierre y otro para la
 * administración.
 * Cada bloque incluye un campo de firma y un campo de nombre y firma.
 */
public class ApartadoBloquesDeFirmas {

    /**
     * Crea y devuelve una tabla de bloques de firmas para el PDF.
     * Esta tabla contiene dos columnas, una para el responsable de cierre y otra
     * para la administración.
     *
     * @return PdfPTable La tabla de bloques de firmas lista para añadirse al PDF.
     */
    public PdfPTable creaBloquesDeFirmas() {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(90);
        PdfPTable bloqueFirmaAdministracion = new PdfPTable(1);
        PdfPTable bloqueFirmaResponsableCierre = new PdfPTable(1);
        agregaCampoDeFirma(new PdfPCell(new Phrase(
                "Certifico que la Información aquí mencionada y los lineamiento de efectivo recibidos y pagados por mi, son correctos y ciertos con los lineamientos establecidos en las transacciones de la semana.",
                FontFactory.getFont(FontFactory.HELVETICA, 8))), bloqueFirmaResponsableCierre);
        agregaCampoDeFirma(new PdfPCell(new Phrase(
                "Confirmo que los aumentos y deducciones arriba mencionados son correctos y que he recibido el efectivo neto, mencionado anteriormente.",
                FontFactory.getFont(FontFactory.HELVETICA, 8))), bloqueFirmaAdministracion);
        agregaCampoQuienFirma(new PdfPCell(
                new Phrase("Nombre y firma de quien realiza el cierre", FontFactory.getFont(FontFactory.HELVETICA, 8))),
                bloqueFirmaResponsableCierre);
        agregaCampoQuienFirma(
                new PdfPCell(
                        new Phrase("Nombre y firma de administración", FontFactory.getFont(FontFactory.HELVETICA, 8))),
                bloqueFirmaAdministracion);

        PdfPCell celdaCierre = new PdfPCell(bloqueFirmaResponsableCierre);
        PdfPCell celdaAdmin = new PdfPCell(bloqueFirmaAdministracion);

        celdaCierre.setPadding(4);
        celdaAdmin.setPadding(4);

        table.addCell(celdaCierre).setBorder(0);
        table.addCell(celdaAdmin).setBorder(0);

        return table;
    }

    /**
     * Agrega un campo de firma a la tabla especificada, con alineación centrada y
     * altura fija.
     *
     * @param celda La celda que contiene el texto del campo de firma.
     * @param tabla La tabla a la que se añadirá el campo de firma.
     */
    private void agregaCampoDeFirma(PdfPCell celda, PdfPTable tabla) {
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setFixedHeight(100);
        tabla.addCell(celda);
    }

    /**
     * Agrega un campo de texto indicando el nombre y firma a la tabla especificada.
     * La celda se alinea centrada horizontalmente y alineada en la parte superior.
     *
     * @param celda La celda que contiene el texto del campo de nombre y firma.
     * @param tabla La tabla a la que se añadirá el campo.
     */
    private void agregaCampoQuienFirma(PdfPCell celda, PdfPTable tabla) {
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_TOP);
        tabla.addCell(celda);
    }
}
