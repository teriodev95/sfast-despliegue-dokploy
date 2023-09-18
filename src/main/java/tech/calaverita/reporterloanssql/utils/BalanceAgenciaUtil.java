package tech.calaverita.reporterloanssql.utils;

public class BalanceAgenciaUtil {
    public static String getNivelAgente(int clientes, double porCobroJueves, int antiguedad) {
        if (clientes >= 0 && clientes <= 29)
            return "SILVER";

        if (clientes >= 30 && clientes <= 49) {
            if (porCobroJueves < 0.7)
                return "SILVER";
            if (porCobroJueves >= 0.7 && porCobroJueves <= 0.799999) {
                if (antiguedad < 13)
                    return "SILVER";
                if (antiguedad >= 13 && antiguedad <= 26)
                    return "GOLD";
                if (antiguedad >= 27 && antiguedad <= 52)
                    return "GOLD";
                if (antiguedad > 52)
                    return "GOLD";
            }
            if (porCobroJueves >= 0.8 && porCobroJueves <= 0.899999) {
                if (antiguedad < 13)
                    return "SILVER";
                if (antiguedad >= 13 && antiguedad <= 26)
                    return "GOLD";
                if (antiguedad >= 27 && antiguedad <= 52)
                    return "GOLD";
                if (antiguedad > 52)
                    return "GOLD";
            }
            if (porCobroJueves >= 0.9) {
                if (antiguedad < 13)
                    return "SILVER";
                if (antiguedad >= 13 && antiguedad <= 26)
                    return "GOLD";
                if (antiguedad >= 27 && antiguedad <= 52)
                    return "GOLD";
                if (antiguedad > 52)
                    return "GOLD";
            }
        }

        if (clientes >= 50 && clientes <= 79) {
            if (porCobroJueves < 0.7)
                return "SILVER";
            if (porCobroJueves >= 0.7 && porCobroJueves <= 0.799999) {
                if (antiguedad < 13)
                    return "SILVER";
                if (antiguedad >= 13 && antiguedad <= 26)
                    return "GOLD";
                if (antiguedad >= 27 && antiguedad <= 52)
                    return "GOLD";
                if (antiguedad > 52)
                    return "GOLD";
            }
            if (porCobroJueves >= 0.8 && porCobroJueves <= 0.899999) {
                if (antiguedad < 13)
                    return "SILVER";
                if (antiguedad >= 13 && antiguedad <= 26)
                    return "GOLD";
                if (antiguedad >= 27 && antiguedad <= 52)
                    return "GOLD";
                if (antiguedad > 52)
                    return "GOLD";
            }
            if (porCobroJueves >= 0.9) {
                if (antiguedad < 13)
                    return "SILVER";
                if (antiguedad >= 13 && antiguedad <= 26)
                    return "GOLD";
                if (antiguedad >= 27 && antiguedad <= 52)
                    return "GOLD";
                if (antiguedad > 52)
                    return "GOLD";
            }
        }

        if (clientes >= 80 && clientes <= 10000) {
            if (porCobroJueves < 0.7)
                return "SILVER";
            if (porCobroJueves >= 0.7 && porCobroJueves <= 0.799999) {
                if (antiguedad < 13)
                    return "SILVER";
                if (antiguedad >= 13 && antiguedad <= 26)
                    return "GOLD";
                if (antiguedad >= 27 && antiguedad <= 52)
                    return "GOLD";
                if (antiguedad > 52)
                    return "GOLD";
            }
            if (porCobroJueves >= 0.8 && porCobroJueves <= 0.899999) {
                if (antiguedad < 13)
                    return "SILVER";
                if (antiguedad >= 13 && antiguedad <= 26)
                    return "GOLD";
                if (antiguedad >= 27 && antiguedad <= 52)
                    return "PLATINUM";
                if (antiguedad > 52)
                    return "PLATINUM";
            }
            if (porCobroJueves >= 0.9) {
                if (antiguedad < 13)
                    return "SILVER";
                if (antiguedad >= 13 && antiguedad <= 26)
                    return "GOLD";
                if (antiguedad >= 27 && antiguedad <= 52)
                    return "PLATINUM";
                if (antiguedad > 52)
                    return "DIAMOND";
            }
        }
        return null;
    }
}
