package tech.calaverita.sfast_xpress.utils;

import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BalanceAgenciaUtil {
    public static String getNivelAgente(int clientesPagoCompleto, double cobroAlJueves,
                                        UsuarioModel agenteUsuarioModel) {
        String nivel;
        int antiguedadEnSemanas = BalanceAgenciaUtil.getAntiguedadAgenteEnSemanas(agenteUsuarioModel);

        if (antiguedadEnSemanas >= 52 && clientesPagoCompleto >= 80 && cobroAlJueves > .9) {
            nivel = "DIAMOND";
        } else if (antiguedadEnSemanas >= 26 && clientesPagoCompleto >= 50 && cobroAlJueves > .8) {
            nivel = "PLATINUM";
        } else if (antiguedadEnSemanas >= 13 && clientesPagoCompleto >= 30 && cobroAlJueves > .7) {
            nivel = "GOLD";
        } else {
            nivel = "SILVER";
        }

        return nivel;
    }

    public static int getPorcentajeComisionCobranza(String nivel) {
        return switch (nivel) {
            case "SILVER" -> 7;
            case "GOLD" -> 8;
            case "PLATINUM" -> 9;
            case "DIAMOND" -> 10;
            default -> 0;
        };
    }

    public static int getPorcentajeBonoMensual(int clientesPagoCompleto, double rendimiento,
                                               UsuarioModel agenteUsuarioModel) {
        int porcentajeBonoMensual = 0;

        int antiguedadEnMeses = BalanceAgenciaUtil.getAntiguedadAgenteEnMeses(agenteUsuarioModel);

        if (clientesPagoCompleto >= 90 && rendimiento >= 95 && antiguedadEnMeses >= 12) {
            porcentajeBonoMensual = 3;
        } else if (clientesPagoCompleto >= 60 && rendimiento >= 90 && antiguedadEnMeses >= 6) {
            porcentajeBonoMensual = 2;
        } else if (clientesPagoCompleto >= 30 && rendimiento >= 80 && antiguedadEnMeses >= 3) {
            porcentajeBonoMensual = 1;
        }

        return porcentajeBonoMensual;
    }

    public static int getAntiguedadAgenteEnSemanas(UsuarioModel agenteUsuarioModel) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaIngresoAgente;

        try {
            fechaIngresoAgente = format.parse(agenteUsuarioModel.getFechaIngreso());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 24 hr/dia x 60 min/hr x 60 seg/min x 1000 ms/seg
        return (int) ((new Date().getTime() - fechaIngresoAgente.getTime()) / (1000 * 60 * 60 * 24 * 7));
    }

    public static int getAntiguedadAgenteEnMeses(UsuarioModel agenteUsuarioModel) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaIngresoAgente;

        try {
            fechaIngresoAgente = format.parse(agenteUsuarioModel.getFechaIngreso());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 30 dia/mes x 24 hr/dia x 60 min/hr x 60 seg/min x 1000 ms/seg
        return (int) ((new Date().getTime() - fechaIngresoAgente.getTime()) / (1000L * 60 * 60 * 24 * 30));
    }
}
