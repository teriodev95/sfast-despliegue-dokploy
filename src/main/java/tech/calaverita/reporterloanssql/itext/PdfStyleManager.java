package tech.calaverita.reporterloanssql.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;

public class PdfStyleManager {

    public static void setStyleForCellTitles(PdfPCell cell) {
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(getPrimaryBasecolor());
        cell.setPadding(4f);
        cell.setBorder(0);
    }

    public static void setStyleNormalCell(PdfPCell cell) {
        cell.setHorizontalAlignment(2);
        cell.setPadding(4f);
        cell.setBorder(0);
    }

    public static void setStyleFillColorCell(PdfPCell cell) {
        cell.setHorizontalAlignment(2);
        cell.setBackgroundColor(getColorBackground());
        cell.setPadding(4f);
        cell.setBorder(0);
    }

    private static void commonCellStyle(PdfPCell cell) {
        cell.setPadding(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(0.1f);
        cell.setBorderColor(BaseColor.GRAY);
    }

    public static BaseColor getPrimaryBasecolor() {
        return new BaseColor(9, 53, 76);
    }

    public static BaseColor getColorBackground() {
        return new BaseColor(188, 200, 206);
    }

    public static BaseColor getWhiteBaseColor() {
        return new BaseColor(255, 255, 255);
    }

}