package tech.calaverita.sfast_xpress.itext;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

public class DottedBorder implements PdfPCellEvent {

    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
        canvas.saveState();
        canvas.setLineDash(3f, 3f); // Configura el estilo de línea punteada
        canvas.rectangle(position.getLeft(), position.getBottom(), position.getWidth(), position.getHeight());
        canvas.stroke();
        canvas.restoreState();
    }

}
