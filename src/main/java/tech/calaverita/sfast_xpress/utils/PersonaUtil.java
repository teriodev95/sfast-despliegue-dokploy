package tech.calaverita.sfast_xpress.utils;

import java.util.HashMap;

import tech.calaverita.sfast_xpress.models.mariaDB.PersonaModel;

public class PersonaUtil {
    public static boolean comparadorPersonas(PersonaModel personaModel, String nombres,
            String apellidoPaterno,
            String apellidoMaterno) {
        int diferenciasNombres = buscarDiferencias(nombres, personaModel.getNombres().split(" ")[0]);
        int diferenciasApellidoPaterno = buscarDiferencias(apellidoPaterno, personaModel.getApellidoPaterno());
        int diferenciasApellidoMaterno = buscarDiferencias(apellidoMaterno, personaModel.getApellidoMaterno());

        return diferenciasNombres <= 2 && diferenciasApellidoPaterno <= 2 && diferenciasApellidoMaterno <= 2;
    }

    private static int buscarDiferencias(String str1, String str2) {
        int cantidadDiferencias = 0;
        boolean isCoincidencia = false;
        HashMap<String, Integer> indicesDiferencia = new HashMap<>();

        for (int i = 0; i < str1.length(); i++) {
            for (int j = 0; j < str2.length(); j++) {
                indicesDiferencia.put("indiceI", i);
                indicesDiferencia.put("indiceJ", j);

                if ((str1.charAt(i) == str2.charAt(j)) && ((i + 1 < str1.length() && j + 1 < str2.length())
                        && (str1.charAt(i + 1) == str2.charAt(j + 1)))) {
                    cantidadDiferencias += recorrerCoincidencia(str1.substring(i, str1.length()),
                            str2.substring(j, str2.length()), indicesDiferencia);
                    isCoincidencia = true;
                    break;
                } else if (i == str1.length() - 1 && j == str2.length() - 1 && str1.charAt(i) == str2.charAt(j)) {
                    cantidadDiferencias += 1;
                    isCoincidencia = true;
                }
            }

            if (isCoincidencia) {
                break;
            } else if (i == str1.length() - 1) {
                cantidadDiferencias = str1.length() > str2.length() ? str1.length() : str2.length();
            }
        }

        return cantidadDiferencias;
    }

    private static int recorrerCoincidencia(String str1, String str2, HashMap<String, Integer> indicesDiferencia) {
        int cantidadDiferencias = indicesDiferencia.get("indiceI") > indicesDiferencia.get("indiceJ")
                ? indicesDiferencia.get("indiceI")
                : 0;

        cantidadDiferencias += indicesDiferencia.get("indiceJ") > indicesDiferencia.get("indiceI")
                ? indicesDiferencia.get("indiceJ") - indicesDiferencia.get("indiceI")
                : 0;

        for (int i = 0; i < str1.length(); i++) {
            if (i < str2.length() && str1.charAt(i) != str2.charAt(i)) {
                cantidadDiferencias++;

                int indiceSubStr1 = -1;
                int indiceSubStr2 = -1;

                if ((i + 1 < str1.length() && i + 1 < str2.length()) && (str1.charAt(i + 1) == str2.charAt(i + 1))) {
                    indiceSubStr1 = i + 1;
                    indiceSubStr2 = i + 1;
                } else if (i + 1 < str1.length()) {
                    indiceSubStr1 = i + 1;
                    indiceSubStr2 = i;
                }

                if (indiceSubStr1 >= 0 && indiceSubStr2 >= 0) {
                    cantidadDiferencias += buscarDiferencias(str1.substring(indiceSubStr1, str1.length()),
                            str2.substring(indiceSubStr2, str2.length()));
                }

                break;
            } else if (i == str2.length()) {
                cantidadDiferencias += str1.length() - i;
                break;
            } else if ((i == str1.length() - 1) && str1.length() < str2.length()) {
                cantidadDiferencias += str2.length() - str1.length();
                break;
            }
        }

        return cantidadDiferencias;
    }
}
