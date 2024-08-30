package tech.calaverita.sfast_xpress.utils;

import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.services.CalendarioService;

@Component
public class MyUtil {
    private static CalendarioService calendarioService;

    public MyUtil(CalendarioService calendarioService) {
        MyUtil.calendarioService = calendarioService;
    }

    public static Double getDouble(Double valor) {
        DecimalFormat df = new DecimalFormat("#.00");
        String valorFormateado = df.format(valor);

        return Double.parseDouble(valorFormateado);
    }

    public static void funSemanaAnterior(CalendarioModel calendarioModel) {
        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        if (semana == 1) {
            anio = anio - 1;
            semana = MyUtil.calendarioService
                    .existsByAnioAndSemana(anio, 53) ? 53 : 52;
        } else {
            semana = semana - 1;
        }

        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);
    }
}
