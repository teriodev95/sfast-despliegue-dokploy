package tech.calaverita.sfast_xpress.utils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static Double monetaryToDouble(Object valor) {
        if (valor == null || valor.toString().isEmpty()) {
            valor = "0.00";
        }

        return Double.parseDouble(valor.toString().replace("$", "").replace(",", ""));
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

    public static CalendarioModel getSemanaActual() {
        return calendarioService
                .findByFechaActualAsync(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).join();
    }

    public static LocalDate getLocalDateFromStringLocalDate(String fecha) {
        return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static LocalDate getLocalDateFromStringLocalDateTime(String fecha) {
        return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static LocalDateTime getLocalDateTimeFromString(String fecha) {
        return LocalDateTime.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
