package tech.calaverita.reporterloanssql.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import tech.calaverita.reporterloanssql.models.UsuarioModel;
import tech.calaverita.reporterloanssql.pojos.BalanceAgencia;
import tech.calaverita.reporterloanssql.pojos.Dashboard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class BalanceAgenciaScript {
    public static final Rectangle personalizado = new RectangleReadOnly(800.0F, 400.0F);

    public static BalanceAgencia balanceAgencia;

    public BalanceAgenciaScript(BalanceAgencia balanceAgencia) {
        this.balanceAgencia = balanceAgencia;
    }

    public static void main(String[] args) {
        writePdf();
    }

    public static void writePdf() {
        Document document = new Document();
        document.setPageSize(personalizado);

        Dashboard dashboard = balanceAgencia.getDashboard().getBody();
        UsuarioModel agente = balanceAgencia.getAgente();
        UsuarioModel gerente = balanceAgencia.getGerente();
        Double asignaciones = balanceAgencia.getAsignaciones();

        Calendar calendar = Calendar.getInstance();

        String agencia = dashboard.getAgencia();
        int anio = dashboard.getAnio();
        int semana = dashboard.getSemana();

        try {
            String path = new File("src\\main\\java\\tech\\calaverita\\reporterloanssql\\resources\\balancesDeAgencias").getCanonicalPath();
            String fileName = path + "\\balance-de-agencia.pdf";

            PdfWriter.getInstance(document, new FileOutputStream(new File(fileName)));

            document.open();

            Paragraph cellLeft1P = new Paragraph();
            cellLeft1P.getFont().setSize(16);
            cellLeft1P.getFont().setStyle(Font.BOLD);
            cellLeft1P.add("CIERRE SEMANAL");
            cellLeft1P.add("\n\n");
            cellLeft1P.add("BALANCE DE AGENCIA");

            PdfPCell cellLeft1C = new PdfPCell(cellLeft1P);
            cellLeft1C.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            Paragraph cellLeft2_1P = new Paragraph("ZONA: " + "07" + "      " + "GERENTE: " + gerente.getNombre());

            PdfPCell cellLeft2_1C = new PdfPCell(cellLeft2_1P);
            cellLeft2_1C.setBorder(PdfPCell.NO_BORDER);
            cellLeft2_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellLeft2_2P = new Paragraph("AG: " + agencia + "      " + "AGENTE: " + agente.getNombre());

            PdfPCell cellLeft2_2C = new PdfPCell(cellLeft2_2P);
            cellLeft2_2C.setBorder(PdfPCell.NO_BORDER);
            cellLeft2_2C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellLeft2_3P = new Paragraph("% DE AG: " + dashboard.getRendimiento() + "      " + "NIVEL: " + BalanceAgenciaUtil.getNivelAgente(dashboard.getClientes(), dashboard.getRendimiento(), 53));

            PdfPCell cellLeft2_3C = new PdfPCell(cellLeft2_3P);
            cellLeft2_3C.setBorder(PdfPCell.NO_BORDER);
            cellLeft2_3C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            PdfPTable cellLeft2T = new PdfPTable(1);
            cellLeft2T.setWidthPercentage(100);
            cellLeft2T.addCell(cellLeft2_1C);
            cellLeft2T.addCell(cellLeft2_2C);
            cellLeft2T.addCell(cellLeft2_3C);

            PdfPCell cellLeft2C = new PdfPCell(cellLeft2T);
            cellLeft2C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            cellLeft2C.setBorder(PdfPCell.NO_BORDER);

            Paragraph cellLeft3_1P = new Paragraph("INGRESOS DEL AGENTE");
            cellLeft3_1P.getFont().setSize(14);
            cellLeft3_1P.getFont().setStyle(Font.BOLD);

            PdfPCell cellLeft3_1C = new PdfPCell(cellLeft3_1P);
            cellLeft3_1C.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cellLeft3_1C.setColspan(2);

            Paragraph cellLeft3_2_1P = new Paragraph("COBRANZA PURA:");

            PdfPCell cellLeft3_2_1C = new PdfPCell(cellLeft3_2_1P);
            cellLeft3_2_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellLeft3_2_2P = new Paragraph("$ " + dashboard.getTotalCobranzaPura());
            cellLeft3_2_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellLeft3_2_2C = new PdfPCell(cellLeft3_2_2P);

            Paragraph cellLeft3_3_1P = new Paragraph("MONTO EXCEDENTE:");

            PdfPCell cellLeft3_3_1C = new PdfPCell(cellLeft3_3_1P);
            cellLeft3_3_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellLeft3_3_2P = new Paragraph("$ " + dashboard.getMontoExcedente());
            cellLeft3_3_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellLeft3_3_2C = new PdfPCell(cellLeft3_3_2P);

            Paragraph cellLeft3_4_1P = new Paragraph("LIQUIDACIONES:");

            PdfPCell cellLeft3_4_1C = new PdfPCell(cellLeft3_4_1P);
            cellLeft3_4_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellLeft3_4_2P = new Paragraph("$ " + dashboard.getLiquidaciones());
            cellLeft3_4_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellLeft3_4_2C = new PdfPCell(cellLeft3_4_2P);

            Paragraph cellLeft3_5_1P = new Paragraph("MULTAS:");

            PdfPCell cellLeft3_5_1C = new PdfPCell(cellLeft3_5_1P);
            cellLeft3_5_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellLeft3_5_2P = new Paragraph("$ " + dashboard.getMultas());
            cellLeft3_5_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellLeft3_5_2C = new PdfPCell(cellLeft3_5_2P);

            Paragraph cellLeft3_6_1P = new Paragraph("OTROS:(                                                )");

            PdfPCell cellLeft3_6_1C = new PdfPCell(cellLeft3_6_1P);
            cellLeft3_6_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellLeft3_6_2P = new Paragraph("$ " + "");
            cellLeft3_6_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellLeft3_6_2C = new PdfPCell(cellLeft3_6_2P);

            Paragraph cellLeft3_7_1P = new Paragraph("TOTAL DE INGRESOS:");

            PdfPCell cellLeft3_7_1C = new PdfPCell(cellLeft3_7_1P);
            cellLeft3_7_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellLeft3_7_2P = new Paragraph("$ " + dashboard.getCobranzaTotal());
            cellLeft3_7_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellLeft3_7_2C = new PdfPCell(cellLeft3_7_2P);

            PdfPTable cellLeft3T = new PdfPTable(2);
            cellLeft3T.setWidthPercentage(100);
            cellLeft3T.setWidths(new int[]{75, 25});
            cellLeft3T.addCell(cellLeft3_1C);
            cellLeft3T.addCell(cellLeft3_2_1C);
            cellLeft3T.addCell(cellLeft3_2_2C);
            cellLeft3T.addCell(cellLeft3_3_1C);
            cellLeft3T.addCell(cellLeft3_3_2C);
            cellLeft3T.addCell(cellLeft3_4_1C);
            cellLeft3T.addCell(cellLeft3_4_2C);
            cellLeft3T.addCell(cellLeft3_5_1C);
            cellLeft3T.addCell(cellLeft3_5_2C);
            cellLeft3T.addCell(cellLeft3_6_1C);
            cellLeft3T.addCell(cellLeft3_6_2C);
            cellLeft3T.addCell(cellLeft3_7_1C);
            cellLeft3T.addCell(cellLeft3_7_2C);

            PdfPCell cellLeft3C = new PdfPCell(cellLeft3T);

            Paragraph cellLeft4_1_1P = new Paragraph("% DE COMISIÓN POR COBRANZA:");

            PdfPCell cellLeft4_1_1C = new PdfPCell(cellLeft4_1_1P);
            cellLeft4_1_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellLeft4_1_2P = new Paragraph("7%");
            cellLeft4_1_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellLeft4_1_2C = new PdfPCell(cellLeft4_1_2P);

            Paragraph cellLeft4_2_1P = new Paragraph("% DE BONO MENSUAL:");

            PdfPCell cellLeft4_2_1C = new PdfPCell(cellLeft4_2_1P);
            cellLeft4_2_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellLeft4_2_2P = new Paragraph("2%");
            cellLeft4_2_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellLeft4_2_2C = new PdfPCell(cellLeft4_2_2P);

            PdfPTable cellLeft4T = new PdfPTable(2);
            cellLeft4T.setWidthPercentage(100);
            cellLeft4T.setWidths(new int[]{75, 25});
            cellLeft4T.addCell(cellLeft4_1_1C);
            cellLeft4T.addCell(cellLeft4_1_2C);
            cellLeft4T.addCell(cellLeft4_2_1C);
            cellLeft4T.addCell(cellLeft4_2_2C);

            PdfPCell cellLeft4C = new PdfPCell(cellLeft4T);

            PdfPCell cellBlank = new PdfPCell(new Paragraph("\n"));
            cellBlank.setBorder(PdfPCell.NO_BORDER);

            PdfPTable tableLeft = new PdfPTable(1);
            tableLeft.setWidthPercentage(100);
            tableLeft.addCell(cellLeft1C);
            tableLeft.addCell(cellBlank);
            tableLeft.addCell(cellLeft2C);
            tableLeft.addCell(cellBlank);
            tableLeft.addCell(cellLeft3C);
            tableLeft.addCell(cellBlank);
            tableLeft.addCell(cellLeft4C);

            PdfPCell cellLeft = new PdfPCell();
            cellLeft.setBorder(PdfPCell.NO_BORDER);
            cellLeft.addElement(tableLeft);

            PdfPCell cellVoid = new PdfPCell();
            cellVoid.setBorder(PdfPCell.NO_BORDER);

            Paragraph cellRight1_1P = new Paragraph();
            cellRight1_1P.add("SEM.: " + dashboard.getSemana() + "      " + "AÑO: " + dashboard.getAnio());
            cellRight1_1P.add("\n");
            cellRight1_1P.add("FECHA: D        " + calendar.get(Calendar.DATE) + "        /M        " + (calendar.get(Calendar.MONTH) + 1));

            PdfPCell cellRight1_1C = new PdfPCell(cellRight1_1P);
            cellRight1_1C.setColspan(2);
            cellRight1_1C.setBorder(PdfPCell.NO_BORDER);
            cellRight1_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellRight1_2_1P = new Paragraph("Total de clientes de la Agencia:");

            PdfPCell cellRight1_2_1C = new PdfPCell(cellRight1_2_1P);
            cellRight1_2_1C.setBorder(PdfPCell.NO_BORDER);
            cellRight1_2_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellRight1_2_2P = new Paragraph("# " + dashboard.getClientes());
            cellRight1_2_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellRight1_2_2C = new PdfPCell(cellRight1_2_2P);

            Paragraph cellRight1_3_1P = new Paragraph("Total de clientes con 'PAGO REDUCIDO':");

            PdfPCell cellRight1_3_1C = new PdfPCell(cellRight1_3_1P);
            cellRight1_3_1C.setBorder(PdfPCell.NO_BORDER);
            cellRight1_3_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellRight1_3_2P = new Paragraph("# " + dashboard.getPagosReducidos());
            cellRight1_3_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellRight1_3_2C = new PdfPCell(cellRight1_3_2P);

            Paragraph cellRight1_4_1P = new Paragraph("Total de clientes con 'NO PAGO':");

            PdfPCell cellRight1_4_1C = new PdfPCell(cellRight1_4_1P);
            cellRight1_4_1C.setBorder(PdfPCell.NO_BORDER);
            cellRight1_4_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellRight1_4_2P = new Paragraph("# " + dashboard.getNoPagos());
            cellRight1_4_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellRight1_4_2C = new PdfPCell(cellRight1_4_2P);

            Paragraph cellRight1_5_1P = new Paragraph("Total de clientes liquidados:");

            PdfPCell cellRight1_5_1C = new PdfPCell(cellRight1_5_1P);
            cellRight1_5_1C.setBorder(PdfPCell.NO_BORDER);
            cellRight1_5_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellRight1_5_2P = new Paragraph("# " + dashboard.getNumeroLiquidaciones());
            cellRight1_5_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellRight1_5_2C = new PdfPCell(cellRight1_5_2P);

            PdfPTable cellRight1T = new PdfPTable(2);
            cellRight1T.setWidthPercentage(100);
            cellRight1T.setWidths(new int[]{85, 15});
            cellRight1T.addCell(cellRight1_1C);
            cellRight1T.addCell(cellRight1_2_1C);
            cellRight1T.addCell(cellRight1_2_2C);
            cellRight1T.addCell(cellRight1_3_1C);
            cellRight1T.addCell(cellRight1_3_2C);
            cellRight1T.addCell(cellRight1_4_1C);
            cellRight1T.addCell(cellRight1_4_2C);
            cellRight1T.addCell(cellRight1_5_1C);
            cellRight1T.addCell(cellRight1_5_2C);

            PdfPCell cellRight1C = new PdfPCell(cellRight1T);
            cellRight1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            cellRight1C.setBorder(PdfPCell.NO_BORDER);

            Paragraph cellRight2_1P = new Paragraph("EGRESOS DEL AGENTE");
            cellRight2_1P.getFont().setSize(14);
            cellRight2_1P.getFont().setStyle(Font.BOLD);

            PdfPCell cellRight2_1C = new PdfPCell(cellRight2_1P);
            cellRight2_1C.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cellRight2_1C.setColspan(2);

            Paragraph cellRight2_2_1P = new Paragraph("ASIGNACIONES PREVIAS DE EFECTIVO:");

            PdfPCell cellRight2_2_1C = new PdfPCell(cellRight2_2_1P);
            cellRight2_2_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellRight2_2_2P = new Paragraph("$ " + asignaciones);
            cellRight2_2_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellRight2_2_2C = new PdfPCell(cellRight2_2_2P);

            Paragraph cellRight2_3_1P = new Paragraph("OTROS:(                                                )");

            PdfPCell cellRight2_3_1C = new PdfPCell(cellRight2_3_1P);
            cellRight2_3_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellRight2_3_2P = new Paragraph("$ " + "");
            cellRight2_3_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellRight2_3_2C = new PdfPCell(cellRight2_3_2P);

            Paragraph cellRight2_4_1P = new Paragraph("EFECTIVO ENTREGADO EN CIERRE:");

            PdfPCell cellRight2_4_1C = new PdfPCell(cellRight2_4_1P);
            cellRight2_4_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellRight2_4_2P = new Paragraph("$ " + "8496");
            cellRight2_4_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellRight2_4_2C = new PdfPCell(cellRight2_4_2P);

            Paragraph cellRight2_5_1P = new Paragraph("TOTAL DE EGRESOS:");

            PdfPCell cellRight2_5_1C = new PdfPCell(cellRight2_5_1P);
            cellRight2_5_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellRight2_5_2P = new Paragraph("$ " + "35446");
            cellRight2_5_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellRight2_5_2C = new PdfPCell(cellRight2_5_2P);

            PdfPTable cellRight2T = new PdfPTable(2);
            cellRight2T.setWidthPercentage(100);
            cellRight2T.setWidths(new int[]{75, 25});
            cellRight2T.addCell(cellRight2_1C);
            cellRight2T.addCell(cellRight2_2_1C);
            cellRight2T.addCell(cellRight2_2_2C);
            cellRight2T.addCell(cellRight2_3_1C);
            cellRight2T.addCell(cellRight2_3_2C);
            cellRight2T.addCell(cellRight2_4_1C);
            cellRight2T.addCell(cellRight2_4_2C);
            cellRight2T.addCell(cellRight2_5_1C);
            cellRight2T.addCell(cellRight2_5_2C);

            PdfPCell cellRight2C = new PdfPCell(cellRight2T);

            Paragraph cellRight3_1P = new Paragraph("EGRESOS DEL AGENTE (Pagos a agente en CIERRE)");
            cellRight3_1P.getFont().setSize(14);
            cellRight3_1P.getFont().setStyle(Font.BOLD);

            PdfPCell cellRight3_1C = new PdfPCell(cellRight3_1P);
            cellRight3_1C.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cellRight3_1C.setColspan(2);

            Paragraph cellRight3_2_1P = new Paragraph("PAGO DE COMISIÓN POR COBRANZA:");

            PdfPCell cellRight3_2_1C = new PdfPCell(cellRight3_2_1P);
            cellRight3_2_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellRight3_2_2P = new Paragraph("$ " + "4319");
            cellRight3_2_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellRight3_2_2C = new PdfPCell(cellRight3_2_2P);

            Paragraph cellRight3_3_1P = new Paragraph("PAGO DE COMISIÓN POR VENTAS");

            PdfPCell cellRight3_3_1C = new PdfPCell(cellRight3_3_1P);
            cellRight3_3_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellRight3_3_2P = new Paragraph("$ " + "300");
            cellRight3_3_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellRight3_3_2C = new PdfPCell(cellRight3_3_2P);

            Paragraph cellRight3_4_1P = new Paragraph("BONOS:");

            PdfPCell cellRight3_4_1C = new PdfPCell(cellRight3_4_1P);
            cellRight3_4_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellRight3_4_2P = new Paragraph("$ " + "");
            cellRight3_4_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellRight3_4_2C = new PdfPCell(cellRight3_4_2P);

            Paragraph cellRight3_5_1P = new Paragraph("EFECTIVO RESTANTE DE CIERRE:");

            PdfPCell cellRight3_5_1C = new PdfPCell(cellRight3_5_1P);
            cellRight3_5_1C.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

            Paragraph cellRight3_5_2P = new Paragraph("$ " + "3877");
            cellRight3_5_2P.getFont().setStyle(Font.BOLDITALIC);

            PdfPCell cellRight3_5_2C = new PdfPCell(cellRight3_5_2P);

            PdfPTable cellRight3T = new PdfPTable(2);
            cellRight3T.setWidthPercentage(100);
            cellRight3T.setWidths(new int[]{75, 25});
            cellRight3T.addCell(cellRight3_1C);
            cellRight3T.addCell(cellRight3_2_1C);
            cellRight3T.addCell(cellRight3_2_2C);
            cellRight3T.addCell(cellRight3_3_1C);
            cellRight3T.addCell(cellRight3_3_2C);
            cellRight3T.addCell(cellRight3_4_1C);
            cellRight3T.addCell(cellRight3_4_2C);
            cellRight3T.addCell(cellRight3_5_1C);
            cellRight3T.addCell(cellRight3_5_2C);

            PdfPCell cellRight3C = new PdfPCell(cellRight3T);

            PdfPTable tableRight = new PdfPTable(1);
            tableRight.setWidthPercentage(100);
            tableRight.addCell(cellRight1C);
            tableRight.addCell(cellBlank);
            tableRight.addCell(cellRight2C);
            tableRight.addCell(cellBlank);
            tableRight.addCell(cellRight3C);

            PdfPCell cellRight = new PdfPCell();
            cellRight.setBorder(PdfPCell.NO_BORDER);
            cellRight.addElement(tableRight);

            PdfPTable tableContent = new PdfPTable(3);
            tableContent.setWidthPercentage(100);
            tableContent.setWidths(new int[]{48, 4, 48});
            tableContent.addCell(cellLeft);
            tableContent.addCell(cellVoid);
            tableContent.addCell(cellRight);

            PdfPCell cellMain = new PdfPCell();
            cellMain.setBorder(PdfPCell.NO_BORDER);
            cellMain.addElement(tableContent);

            PdfPTable tableMain = new PdfPTable(1);
            tableMain.setWidthPercentage(100);
            tableMain.addCell(cellMain);

            document.add(tableMain);

            document.close();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}
