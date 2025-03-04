package tech.calaverita.sfast_xpress.utils;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import tech.calaverita.sfast_xpress.models.mariaDB.TabulacionDineroModel;
import tech.calaverita.sfast_xpress.services.TabulacionDineroService;

@Component
public class TabulacionDineroUtil {
    private static TabulacionDineroService tabulacionDineroService;
    private static String gerencia;
    private static Integer anio;
    private static Integer semana;

    public TabulacionDineroUtil(TabulacionDineroService tabulacionDineroService) {
        TabulacionDineroUtil.tabulacionDineroService = tabulacionDineroService;
    }

    public static HashMap<String, Object> validateTabulacionDinero(TabulacionDineroModel tabulacionDineroModel) {
        TabulacionDineroUtil.gerencia = tabulacionDineroModel.getGerencia();
        TabulacionDineroUtil.anio = tabulacionDineroModel.getAnio();
        TabulacionDineroUtil.semana = tabulacionDineroModel.getSemana();

        HashMap<String, Object> resultValidation = new HashMap<>();

        if (isDuplicated()) {
            resultValidation.put("result", "Ya existe un registro para este usuario, año y semana.");
        } else if (!isValidAnio()) {
            resultValidation.put("result", "El año es inválido");
        } else if (!isValidSemana()) {
            resultValidation.put("result", "La semana es inválida");
        }

        return resultValidation;
    }

    private static Boolean isDuplicated() {
        return TabulacionDineroUtil.tabulacionDineroService.existsByGerenciaAnioAndSemana(gerencia, anio, semana);
    }

    private static Boolean isValidAnio() {
        return (TabulacionDineroUtil.anio >= 2000) ? true : false;
    }

    private static Boolean isValidSemana() {
        return (TabulacionDineroUtil.semana >= 1 && TabulacionDineroUtil.semana <= 53) ? true : false;
    }
}
