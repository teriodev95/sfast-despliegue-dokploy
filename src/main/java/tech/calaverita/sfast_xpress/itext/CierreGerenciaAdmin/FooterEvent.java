package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class FooterEvent extends PdfPageEventHelper {
    private String cierreRealizadoPor;

    public FooterEvent(String cierreRealizadoPor) {
        this.cierreRealizadoPor = cierreRealizadoPor;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
                new Phrase("Cierre realizado por: " + this.cierreRealizadoPor,
                        FontFactory.getFont(FontFactory.HELVETICA, 8)),
                297.5f, 30, 0);
    }
}
