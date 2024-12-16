package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.data;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page4.tables.classes.TablaFlujoEfectivoGerente;

public class JsonDataPage4 {

    public TablaFlujoEfectivoGerente makeData() {
        Gson gson = new Gson();

        try {
            FileReader reader = new FileReader(
                    "src/main/resources/CierreGerenciaAdmin/page4/data/data.json");
            // FileReader reader = new
            // FileReader("src/main/java/com/clvrt/pdf/page2/data/dataFull.json");
            TablaFlujoEfectivoGerente reporte = gson.fromJson(reader, TablaFlujoEfectivoGerente.class);
            reader.close();
            System.out.println("Título Flujo Gerente: " + reporte.getTitulo());
            return reporte;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public TablaFlujoEfectivoGerente makeDataFromArray() { // No Blob real, mas bien array de objetos
        Gson gson = new Gson();

        try {
            FileReader reader = new FileReader(
                    "src/main/resources/CierreGerenciaAdmin/page1/data/dataBlob.json");
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            JsonElement jsonReporteBalance = jsonArray.get(5);
            TablaFlujoEfectivoGerente reporteBalance = gson.fromJson(jsonReporteBalance,
                    TablaFlujoEfectivoGerente.class);
            // System.out.println("ReporteBalance:");
            // System.out.println(reporteBalance);
            reader.close();
            // Usa el objeto reporte
            System.out.println("Título Flujo de Gerente (Array): " + reporteBalance.getTitulo());
            // Puedes imprimir más detalles de reporte aquí...
            return reporteBalance;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
