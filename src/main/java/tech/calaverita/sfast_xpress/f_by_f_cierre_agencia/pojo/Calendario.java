package tech.calaverita.sfast_xpress.f_by_f_cierre_agencia.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Calendario {
    private Integer anio;
    private Integer semana;
    private String mes;

    public Calendario(int anio, int semana) {
        this.anio = anio;
        this.semana = semana;
    }

    public Calendario(int anio, String mes) {
        this.anio = anio;
        this.mes = mes;
    }

    public static Calendario getMesAnterior(CalendarioModel calendarioModel) {
        // To easy code
        int anio = calendarioModel.getAnio();
        String mesActual = calendarioModel.getMes();

        String[] meses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" };

        int indiceMes = 0;
        for (int i = 0; i < meses.length; i++) {
            if (mesActual.equals(meses[i])) {
                indiceMes = i - 1;
                break;
            }
        }

        // To easy code
        int anioMesAnterior = indiceMes < 0 ? anio - 1 : anio;
        String mesAnterior = meses[indiceMes < 0 ? 11 : indiceMes];

        return new Calendario(anioMesAnterior, mesAnterior);
    }
}
