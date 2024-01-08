package tech.calaverita.reporterloanssql.itext.fonts;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;


public class Fonts {
    private final String BASE_PATH = "src/main/java/tech/calaverita/reporterloanssql/itext/fonts/";

    public Paragraph bold(String text, int size, int alignment) {
        Font font = createFont(BASE_PATH + "SourceCodePro-Bold.ttf", size);
        return createParagraph(text, font, alignment);
    }

    public Paragraph bold(String text, int size, BaseColor baseColor) {
        Font font = createFont(BASE_PATH + "SourceCodePro-Bold.ttf", size);
        font.setColor(baseColor);
        return createParagraph(text, font);
    }

    public Paragraph bold(String text, int size, int alignment, BaseColor baseColor) {
        Font font = createFont(BASE_PATH + "SourceCodePro-Bold.ttf", size);
        font.setColor(baseColor);
        return createParagraph(text, font, alignment);
    }

    public Paragraph regular(String text, int size, int alignment) {
        Font font = createFont(BASE_PATH + "SourceCodePro-Regular.ttf", size);
        return createParagraph(text, font, alignment);
    }

    public Paragraph regular(String text, int size, BaseColor baseColor) {
        Font font = createFont(BASE_PATH + "SourceCodePro-Regular.ttf", size);
        font.setColor(baseColor);
        return createParagraph(text, font);
    }

    public Paragraph regular(String text, int size, int alignment, BaseColor baseColor) {
        Font font = createFont(BASE_PATH + "SourceCodePro-Regular.ttf", size);
        font.setColor(baseColor);
        return createParagraph(text, font, alignment);
    }

    private Font createFont(String fontFileName, int size) {
        try {
            BaseFont baseFont = BaseFont.createFont(
                    fontFileName,
                    "CP1251",
                    BaseFont.EMBEDDED
            );
            return new Font(baseFont, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Paragraph createParagraph(String text, Font font) {
        Paragraph p = new Paragraph();
        try {
            Chunk c = new Chunk();
            c.append(text);
            c.setFont(font);
            p.add(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    public Paragraph createParagraph(String text, Font font, int alignment) {
        Paragraph p = new Paragraph();
        try {
            Chunk c = new Chunk();
            p.setAlignment(alignment);
            c.append(text);
            c.setFont(font);
            p.add(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
}
