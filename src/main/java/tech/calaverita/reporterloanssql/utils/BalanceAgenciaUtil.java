package tech.calaverita.reporterloanssql.utils;

import tech.calaverita.reporterloanssql.persistence.entities.UsuarioEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BalanceAgenciaUtil {
    public static String getNivelAgente(
            int clientes,
            double porCobroJueves,
            UsuarioEntity agenteUsuarioEntity
    ) {
        int antiguedadEnSemanas = BalanceAgenciaUtil.getAntiguedadAgenteEnSemanas(agenteUsuarioEntity);

        if (
                clientes >= 0 && clientes <= 29
        ) {
            return "SILVER";
        }

        if (
                (clientes >= 30) && (clientes <= 49)
        ) {
            if (
                    porCobroJueves < 0.7
            ) {
                return "SILVER";
            }

            if (
                    (porCobroJueves >= 0.7) && (porCobroJueves <= 0.799999)
            ) {
                if (
                        antiguedadEnSemanas < 13
                ) {
                    return "SILVER";
                }

                if (
                        (antiguedadEnSemanas >= 13) && (antiguedadEnSemanas <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (antiguedadEnSemanas >= 27) && (antiguedadEnSemanas <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        antiguedadEnSemanas > 52
                ) {
                    return "GOLD";
                }
            }

            if (
                    (porCobroJueves >= 0.8) && (porCobroJueves <= 0.899999)
            ) {
                if (
                        antiguedadEnSemanas < 13
                ) {
                    return "SILVER";
                }

                if (
                        (antiguedadEnSemanas >= 13) && (antiguedadEnSemanas <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (antiguedadEnSemanas >= 27) && (antiguedadEnSemanas <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        antiguedadEnSemanas > 52
                ) {
                    return "GOLD";
                }
            }

            if (
                    porCobroJueves >= 0.9
            ) {
                if (
                        antiguedadEnSemanas < 13
                ) {
                    return "SILVER";
                }

                if (
                        (antiguedadEnSemanas >= 13) && (antiguedadEnSemanas <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (antiguedadEnSemanas >= 27) && (antiguedadEnSemanas <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        antiguedadEnSemanas > 52
                ) {
                    return "GOLD";
                }
            }
        }

        if (
                (clientes >= 50) && (clientes <= 79)
        ) {
            if (
                    porCobroJueves < 0.7
            ) {
                return "SILVER";
            }

            if (
                    (porCobroJueves >= 0.7) && (porCobroJueves <= 0.799999)
            ) {
                if (
                        antiguedadEnSemanas < 13
                ) {
                    return "SILVER";
                }

                if (
                        (antiguedadEnSemanas >= 13) && (antiguedadEnSemanas <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (antiguedadEnSemanas >= 27) && (antiguedadEnSemanas <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        antiguedadEnSemanas > 52
                ) {
                    return "GOLD";
                }
            }

            if (
                    (porCobroJueves >= 0.8) && (porCobroJueves <= 0.899999)
            ) {
                if (
                        antiguedadEnSemanas < 13
                ) {
                    return "SILVER";
                }

                if (
                        (antiguedadEnSemanas >= 13) && (antiguedadEnSemanas <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (antiguedadEnSemanas >= 27) && (antiguedadEnSemanas <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        antiguedadEnSemanas > 52
                ) {
                    return "GOLD";
                }
            }

            if (
                    porCobroJueves >= 0.9
            ) {
                if (
                        antiguedadEnSemanas < 13
                ) {
                    return "SILVER";
                }

                if (
                        (antiguedadEnSemanas >= 13) && (antiguedadEnSemanas <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (antiguedadEnSemanas >= 27) && (antiguedadEnSemanas <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        antiguedadEnSemanas > 52
                ) {
                    return "GOLD";
                }
            }
        }

        if (
                (clientes >= 80) && (clientes <= 10000)
        ) {
            if (
                    porCobroJueves < 0.7
            ) {
                return "SILVER";
            }

            if (
                    (porCobroJueves >= 0.7) && (porCobroJueves <= 0.799999)
            ) {
                if (
                        antiguedadEnSemanas < 13
                ) {
                    return "SILVER";
                }

                if (
                        (antiguedadEnSemanas >= 13) && (antiguedadEnSemanas <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (antiguedadEnSemanas >= 27) && (antiguedadEnSemanas <= 52)
                ) {
                    return "GOLD";
                }

                if (
                        antiguedadEnSemanas > 52
                ) {
                    return "GOLD";
                }
            }
            if (
                    (porCobroJueves >= 0.8) && (porCobroJueves <= 0.899999)
            ) {
                if (
                        antiguedadEnSemanas < 13
                ) {
                    return "SILVER";
                }

                if (
                        (antiguedadEnSemanas >= 13) && (antiguedadEnSemanas <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (antiguedadEnSemanas >= 27) && (antiguedadEnSemanas <= 52)
                ) {
                    return "PLATINUM";
                }

                if (
                        antiguedadEnSemanas > 52
                ) {
                    return "PLATINUM";
                }
            }

            if (
                    porCobroJueves >= 0.9
            ) {
                if (
                        antiguedadEnSemanas < 13
                ) {
                    return "SILVER";
                }

                if (
                        (antiguedadEnSemanas >= 13) && (antiguedadEnSemanas <= 26)
                ) {
                    return "GOLD";
                }

                if (
                        (antiguedadEnSemanas >= 27) && (antiguedadEnSemanas <= 52)
                ) {
                    return "PLATINUM";
                }

                if (
                        antiguedadEnSemanas > 52
                ) {
                    return "DIAMOND";
                }
            }
        }

        return null;
    }

    public static int getPorcentajeComisionCobranza(
            String nivel
    ) {
        return switch (nivel) {
            case "SILVER" -> 7;
            case "GOLD" -> 8;
            case "PLATINUM" -> 9;
            case "DIAMOND" -> 10;
            default -> 0;
        };
    }

    public static int getPorcentajeBonoMensual(
            int clientesPagoCompleto,
            double rendimiento,
            UsuarioEntity agenteUsuarioEntity
    ) {
        int porcentajeBonoMensual = 0;

        int antiguedadEnMeses = BalanceAgenciaUtil.getAntiguedadAgenteEnMeses(agenteUsuarioEntity);

        if (
                clientesPagoCompleto >= 90 && rendimiento >= 95 && antiguedadEnMeses >= 12
        ) {
            porcentajeBonoMensual = 3;
        } else if (
                clientesPagoCompleto >= 60 && rendimiento >= 90 && antiguedadEnMeses >= 6
        ) {
            porcentajeBonoMensual = 2;
        } else if (
                clientesPagoCompleto >= 30 && rendimiento >= 80 && antiguedadEnMeses >= 3
        ) {
            porcentajeBonoMensual = 1;
        }

        return porcentajeBonoMensual;
    }

    private static int getAntiguedadAgenteEnSemanas(
            UsuarioEntity agenteUsuarioEntity
    ) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaIngresoAgente;

        try {
            fechaIngresoAgente = format.parse(agenteUsuarioEntity.getFechaIngreso());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 24 hr/dia x 60 min/hr x 60 seg/min x 1000 ms/seg
        return (int) ((new Date().getTime() - fechaIngresoAgente.getTime()) / (1000 * 60 * 60 * 24 * 7));
    }

    private static int getAntiguedadAgenteEnMeses(
            UsuarioEntity agenteUsuarioEntity
    ) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaIngresoAgente;

        try {
            fechaIngresoAgente = format.parse(agenteUsuarioEntity.getFechaIngreso());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 30 dia/mes x 24 hr/dia x 60 min/hr x 60 seg/min x 1000 ms/seg
        return (int) ((new Date().getTime() - fechaIngresoAgente.getTime()) / (1000L * 60 * 60 * 24 * 30));
    }
}
