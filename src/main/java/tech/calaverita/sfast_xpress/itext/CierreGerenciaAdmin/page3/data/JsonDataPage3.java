package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.data;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page3.tables.classes.TablaFlujoEfectivo;

public class JsonDataPage3 {

    public TablaFlujoEfectivo makeData() {
        Gson gson = new Gson();

        try {
            FileReader reader = new FileReader("src\\main\\java\\com\\clvrt\\pdf\\page3\\data\\data.json");
            TablaFlujoEfectivo reporte = gson.fromJson(reader, TablaFlujoEfectivo.class);
            reader.close();
            System.out.println("Título Flujo Blob: " + reporte.getTitulo());
            return reporte;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public TablaFlujoEfectivo makeDataFromBlob() { // No Blob real, mas bien array de objetos
        Gson gson = new Gson();

        try {
            FileReader reader = new FileReader("src\\main\\java\\com\\clvrt\\pdf\\page1\\data\\dataBlob.json");
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            JsonElement jsonReporteBalance = jsonArray.get(4);
            TablaFlujoEfectivo reporteBalance = gson.fromJson(jsonReporteBalance, TablaFlujoEfectivo.class);
            // System.out.println("ReporteBalance:");
            // System.out.println(reporteBalance);
            reader.close();
            // Usa el objeto reporte
            System.out.println("Título Flujo: " + reporteBalance.getTitulo());
            // Puedes imprimir más detalles de reporte aquí...
            return reporteBalance;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
