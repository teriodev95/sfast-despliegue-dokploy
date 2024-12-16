package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page2.data;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
// import com.google.gson.JsonArray;
// import com.google.gson.JsonElement;
// import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page2.tables.classes.TablaResumenDeVentas;

public class JsonDataPage2 {

    public TablaResumenDeVentas makeDataBlob() {
        Gson gson = new Gson();

        try {
            FileReader reader = new FileReader(
                    "src/main/resources/CierreGerenciaAdmin/page2/data/data.json");
            // FileReader reader = new
            // FileReader("src/main/java/com/clvrt/pdf/page2/data/dataFull.json");
            TablaResumenDeVentas reporte = gson.fromJson(reader, TablaResumenDeVentas.class);
            reader.close();
            System.out.println("Título: " + reporte.getTitulo());
            return reporte;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public TablaResumenDeVentas makeDataFromBlob() { // No Blob real, mas bien array de objetos
        Gson gson = new Gson();

        try {
            FileReader reader = new FileReader(
                    "src/main/resources/CierreGerenciaAdmin/page1/data/dataBlob.json");
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            JsonElement jsonReporteBalance = jsonArray.get(3);
            TablaResumenDeVentas reporteBalance = gson.fromJson(jsonReporteBalance, TablaResumenDeVentas.class);
            // System.out.println("ReporteBalance:");
            // System.out.println(reporteBalance);
            reader.close();
            // Usa el objeto reporte
            System.out.println("Título: " + reporteBalance.getTitulo());
            // Puedes imprimir más detalles de reporte aquí...
            return reporteBalance;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
