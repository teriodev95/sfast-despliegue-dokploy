package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.data;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaNumerosGerencia;

public class TestGsonData {

    public static void main(String[] args) {
        // Instancia de Gson
        Gson gson = new Gson();

        try {
            // Lee el archivo JSON
            FileReader reader = new FileReader("src\\main\\java\\com\\clvrt\\pdf\\data\\data2.json");

            // Convierte el JSON en una instancia de la clase Reporte
            TablaNumerosGerencia reporte = gson.fromJson(reader, TablaNumerosGerencia.class);

            // Cierra el lector
            reader.close();

            // Usa el objeto reporte
            System.out.println("Título: " + reporte.getTitulo());
            // Puedes imprimir más detalles de reporte aquí...

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
