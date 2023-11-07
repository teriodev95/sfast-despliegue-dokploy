package tech.calaverita.reporterloanssql.utils;

public class BalanceAgenciaUtil {
    public static String strGetNivelAgente(
            int intClientes,
            double numPorCobroJueves,
            int intAntiguedad
    ) {
        if (
                intClientes >= 0 && intClientes <= 29
        ) {
            return "SILVER";
        }

        if (
                (intClientes >= 30) && (intClientes <= 49)
        ) {
            if (
                    numPorCobroJueves < 0.7
            ) {
                return "SILVER";
            }

            if (
                    (numPorCobroJueves >= 0.7) && (numPorCobroJueves <= 0.799999)
            ) {
                if (
                        intAntiguedad < 13
                ) {
                    return "SILVER";
                }

                if (
                        (intAntiguedad >= 13) && (intAntiguedad <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (intAntiguedad >= 27) && (intAntiguedad <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        intAntiguedad > 52
                ) {
                    return "GOLD";
                }
            }

            if (
                    (numPorCobroJueves >= 0.8) && (numPorCobroJueves <= 0.899999)
            ) {
                if (
                        intAntiguedad < 13
                ) {
                    return "SILVER";
                }

                if (
                        (intAntiguedad >= 13) && (intAntiguedad <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (intAntiguedad >= 27) && (intAntiguedad <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        intAntiguedad > 52
                ) {
                    return "GOLD";
                }
            }

            if (
                    numPorCobroJueves >= 0.9
            ) {
                if (
                        intAntiguedad < 13
                ) {
                    return "SILVER";
                }

                if (
                        (intAntiguedad >= 13) && (intAntiguedad <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (intAntiguedad >= 27) && (intAntiguedad <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        intAntiguedad > 52
                ) {
                    return "GOLD";
                }
            }
        }

        if (
                (intClientes >= 50) && (intClientes <= 79)
        ) {
            if (
                    numPorCobroJueves < 0.7
            ) {
                return "SILVER";
            }

            if (
                    (numPorCobroJueves >= 0.7) && (numPorCobroJueves <= 0.799999)
            ) {
                if (
                        intAntiguedad < 13
                ) {
                    return "SILVER";
                }

                if (
                        (intAntiguedad >= 13) && (intAntiguedad <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (intAntiguedad >= 27) && (intAntiguedad <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        intAntiguedad > 52
                ) {
                    return "GOLD";
                }
            }

            if (
                    (numPorCobroJueves >= 0.8) && (numPorCobroJueves <= 0.899999)
            ) {
                if (
                        intAntiguedad < 13
                ) {
                    return "SILVER";
                }

                if (
                        (intAntiguedad >= 13) && (intAntiguedad <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (intAntiguedad >= 27) && (intAntiguedad <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        intAntiguedad > 52
                ) {
                    return "GOLD";
                }
            }

            if (
                    numPorCobroJueves >= 0.9
            ) {
                if (
                        intAntiguedad < 13
                ) {
                    return "SILVER";
                }

                if (
                        (intAntiguedad >= 13) && (intAntiguedad <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (intAntiguedad >= 27) && (intAntiguedad <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        intAntiguedad > 52
                ) {
                    return "GOLD";
                }
            }
        }

        if (
                (intClientes >= 80) && (intClientes <= 10000)
        ) {
            if (
                    numPorCobroJueves < 0.7
            ) {
                return "SILVER";
            }

            if (
                    (numPorCobroJueves >= 0.7) && (numPorCobroJueves <= 0.799999)
            ) {
                if (
                        intAntiguedad < 13
                ) {
                    return "SILVER";
                }

                if (
                        (intAntiguedad >= 13) && (intAntiguedad <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (intAntiguedad >= 27) && (intAntiguedad <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        intAntiguedad > 52
                ) {
                    return "GOLD";
                }
            }
            if (
                    (numPorCobroJueves >= 0.8) && (numPorCobroJueves <= 0.899999)
            ) {
                if (
                        intAntiguedad < 13
                ) {
                    return "SILVER";
                }

                if (
                        (intAntiguedad >= 13) && (intAntiguedad <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (intAntiguedad >= 27) && (intAntiguedad <= 52)
                ) {
                    return "PLATINUM";
                }

                if (
                        intAntiguedad > 52
                ) {
                    return "PLATINUM";
                }
            }

            if (
                    numPorCobroJueves >= 0.9
            ) {
                if (
                        intAntiguedad < 13
                ) {
                    return "SILVER";
                }

                if (
                        (intAntiguedad >= 13) && (intAntiguedad <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (intAntiguedad >= 27) && (intAntiguedad <= 52)
                ) {
                    return "PLATINUM";
                }

                if (
                        intAntiguedad > 52
                ) {
                    return "DIAMOND";
                }
            }
        }

        return null;
    }
}
