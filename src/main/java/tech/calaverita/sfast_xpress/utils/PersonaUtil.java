package tech.calaverita.sfast_xpress.utils;

import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;

public class PersonaUtil {
    public static boolean comparadorPersonas(PrestamoViewModel prestamoViewModel, String nombres,
            String apellidoPaterno,
            String apellidoMaterno) {
        boolean isNombresIgual = areCadenasIguales(prestamoViewModel.getNombres().split("")[0], nombres);
        boolean isApellidoPaternoIgual = areCadenasIguales(prestamoViewModel.getApellidoPaterno(), apellidoPaterno);
        boolean isApellidoMaternoIgual = areCadenasIguales(prestamoViewModel.getApellidoMaterno(), apellidoMaterno);

        return isNombresIgual && isApellidoPaternoIgual && isApellidoMaternoIgual;
    }

    private static boolean areCadenasIguales(String str1, String str2) {
        int caracteresIguales = 0;
        boolean isNombresIgual;

        if (str1.length() == str2.length()) {
            for (int i = 0; i < str1.length(); i++) {
                if (str1.charAt(i) == str2.charAt(i)) {
                    caracteresIguales++;
                }
            }
        } else {
            for (int i = 0; i < str1.length(); i++) {
                if (i < str2.length() && str1.charAt(i) == str2.charAt(i)) {
                    caracteresIguales++;
                } else {
                    break;
                }
            }
        }

        isNombresIgual = caracteresIguales >= str1.length() / 2;

        return isNombresIgual;
    }
}
