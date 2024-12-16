package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.data;

import java.io.FileReader;
import java.io.IOException;
// import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaCierreSemanalGerencia;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaDetallesCierreAgencias;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaNumerosGerencia;

public class JsonData {

    // public static void main(String[] args) {
    public TablaDetallesCierreAgencias dataBalance() {
        Gson gson = new Gson();

        try {
            // Lee el archivo JSON
            FileReader reader = new FileReader(
                    System.getProperty("user.dir") + "/src/main/resources/CierreGerenciaAdmin/page1/data/data.json");

            // Convierte el JSON en una instancia de la clase Reporte
            TablaDetallesCierreAgencias reporte = gson.fromJson(reader, TablaDetallesCierreAgencias.class);

            // Cierra el lector
            reader.close();

            // Usa el objeto reporte
            // System.out.println("Título: " + reporte.getTituloReporte());
            return reporte;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public TablaNumerosGerencia dataNumerosGerencia() {
        Gson gson = new Gson();

        try {
            FileReader reader = new FileReader(
                    "src/main/resources/CierreGerenciaAdmin/page1/data/data2.json");
            TablaNumerosGerencia reporte = gson.fromJson(reader, TablaNumerosGerencia.class);
            reader.close();
            System.out.println("Título: " + reporte.getTitulo());
            return reporte;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public TablaNumerosGerencia dataNumerosDesdeArray() {
        Gson gson = new Gson();

        try {
            FileReader reader = new FileReader(
                    "src/main/resources/CierreGerenciaAdmin/page1/data/dataBlob.json");
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            JsonElement jsonReporteBalance = jsonArray.get(1);
            TablaNumerosGerencia reporteBalance = gson.fromJson(jsonReporteBalance, TablaNumerosGerencia.class);
            System.out.println("ReporteBalance:");
            System.out.println(reporteBalance);
            reader.close();
            // Usa el objeto reporte
            System.out.println("Título: " + reporteBalance.getTitulo());
            return reporteBalance;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TablaCierreSemanalGerencia dataDetallesCierre() {
        Gson gson = new Gson();

        try {
            FileReader reader = new FileReader(
                    "src/main/resources/CierreGerenciaAdmin/page1/data/data3.json");
            TablaCierreSemanalGerencia reporte = gson.fromJson(reader, TablaCierreSemanalGerencia.class);

            reader.close();
            System.out.println("Titulo: " + reporte.getTitulo());
            System.out.println("Total de ingresos: " + reporte.getIngresos().getTotalDeIngresos());
            System.out.println("Total de egresos: " + reporte.getEgresos().getTotalDeEgresos());
            System.out.println("Efectivo a entregar: " + reporte.getEfectivoAEntregar());
            // dataBlob();
            // makeDataBlob();
            return reporte;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // public List<Object> dataBlob() {
    // Gson gson = new Gson();

    // try {
    // FileReader reader = new
    // FileReader("src/main/java/com/clvrt/pdf/page1/data/dataBlob.json");
    // //JsonArray jsonArray = JsonParser.parseString("reader").getAsJsonArray();
    // JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
    // JsonElement jsonReporteBalance = jsonArray.get(0);
    // TablaBalance reporteBalance = gson.fromJson(jsonReporteBalance,
    // TablaBalance.class);
    // System.out.println("ReporteBalance:");
    // System.out.println(reporteBalance);
    // reader.close();

    // JsonElement jsonNumerosDeLaGerencia = jsonArray.get(1);
    // TablaNumerosGerencia numerosDeLaGerencia =
    // gson.fromJson(jsonNumerosDeLaGerencia, TablaNumerosGerencia.class);
    // System.out.println("NumerosDeLaGerencia:");
    // System.out.println(numerosDeLaGerencia);

    // JsonElement jsonDetalleCierreSemanal = jsonArray.get(2);
    // TablaDetallesCierre detalleCierreSemanal =
    // gson.fromJson(jsonDetalleCierreSemanal, TablaDetallesCierre.class);
    // System.out.println("DetalleCierreSemanal:");
    // System.out.println(detalleCierreSemanal);

    // return null;
    // } catch (IOException e) {
    // e.printStackTrace();
    // return null;
    // }
    // }

    public TablaDetallesCierreAgencias dataBalanceDesdeArray() {
        Gson gson = new Gson();

        try {
            FileReader reader = new FileReader(
                    "src/main/resources/CierreGerenciaAdmin/page1/data/dataBlob.json");
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            JsonElement jsonReporteBalance = jsonArray.get(0);
            TablaDetallesCierreAgencias reporteBalance = gson.fromJson(jsonReporteBalance,
                    TablaDetallesCierreAgencias.class);
            System.out.println("ReporteBalance:");
            System.out.println(reporteBalance);
            reader.close();
            // Usa el objeto reporte
            System.out.println("Título: " + reporteBalance.getTituloReporte());
            return reporteBalance;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
