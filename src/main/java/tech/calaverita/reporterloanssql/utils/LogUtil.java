package tech.calaverita.reporterloanssql.utils;

import com.google.gson.Gson;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.BalanceAgenciaDTO;

import java.util.HashMap;

public class LogUtil {
    public static String getLogCierreSemanal(
            BalanceAgenciaDTO balanceAgenciaDTO
    ) {
        HashMap<String, String> log = new HashMap<>();
        log.put("nivelAgencia", balanceAgenciaDTO.getNivel());
        log.put("nivelAgenciaCalculado", balanceAgenciaDTO.getNivelCalculado());

        return new Gson().toJson(log);
    }
}
