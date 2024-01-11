package tech.calaverita.reporterloanssql.utils;

import java.text.DecimalFormat;

public class MyUtil {
    public static Double getDouble(
            Double valor
    ) {
        DecimalFormat df = new DecimalFormat("#.00");
        String valorFormateado = df.format(valor);

        return Double.parseDouble(valorFormateado);
    }
}
