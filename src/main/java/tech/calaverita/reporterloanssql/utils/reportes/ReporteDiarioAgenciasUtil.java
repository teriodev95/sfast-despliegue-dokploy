package tech.calaverita.reporterloanssql.utils.reportes;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.dto.reporte_diario_agencias.AgenciaReporteDiarioAgenciasDTO;
import tech.calaverita.reporterloanssql.dto.reporte_diario_agencias.DashboardSemanaActualReporteDiarioAgenciasDTO;
import tech.calaverita.reporterloanssql.dto.reporte_diario_agencias.DashboardSemanaAnteriorReporteDiarioAgenciasDTO;
import tech.calaverita.reporterloanssql.dto.reporte_diario_agencias.EncabezadoReporteDiarioAgenciasDTO;
import tech.calaverita.reporterloanssql.models.mariaDB.*;
import tech.calaverita.reporterloanssql.models.mongoDB.ReporteDiarioAgenciasDocument;
import tech.calaverita.reporterloanssql.services.*;
import tech.calaverita.reporterloanssql.services.reportes.ReporteDiarioAgenciasService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public final class ReporteDiarioAgenciasUtil {
    private static AgenciaService agenciaService;
    private static UsuarioService usuarioService;
    private static CalendarioService calendarioService;
    private static PagoService pagoService;
    private static SucursalService sucursalService;
    private static ReporteDiarioAgenciasService reporteDiarioAgenciasService;

    public ReporteDiarioAgenciasUtil(
            AgenciaService agenciaService,
            UsuarioService usuarioService,
            CalendarioService calendarioService,
            PagoService pagoService,
            SucursalService sucursalService,
            ReporteDiarioAgenciasService reporteDiarioAgenciasService
    ) {
        ReporteDiarioAgenciasUtil.agenciaService = agenciaService;
        ReporteDiarioAgenciasUtil.usuarioService = usuarioService;
        ReporteDiarioAgenciasUtil.calendarioService = calendarioService;
        ReporteDiarioAgenciasUtil.pagoService = pagoService;
        ReporteDiarioAgenciasUtil.sucursalService = sucursalService;
        ReporteDiarioAgenciasUtil.reporteDiarioAgenciasService = reporteDiarioAgenciasService;
    }

    public static ReporteDiarioAgenciasDocument getReporte(
            GerenciaModel gerenciaModel
    ) throws ExecutionException, InterruptedException {
        ReporteDiarioAgenciasDocument reporte = new ReporteDiarioAgenciasDocument();
        {
            HashMap<String, Integer> anioAndSemana = ReporteDiarioAgenciasUtil.getAnioAndSemana();

            reporte.setId(ReporteDiarioAgenciasUtil.getId(gerenciaModel));
            reporte.setEncabezado(ReporteDiarioAgenciasUtil.getEncabezado(gerenciaModel));
            reporte.setAgencias(ReporteDiarioAgenciasUtil.getAgencias(gerenciaModel, anioAndSemana));
        }

        return reporte;
    }

    private static HashMap<String, Integer> getAnioAndSemana() throws ExecutionException, InterruptedException {
        LocalDate date = LocalDate.now();

        CompletableFuture<CalendarioModel> calendarioEntity = ReporteDiarioAgenciasUtil.calendarioService
                .findByFechaActualAsync(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        calendarioEntity.join();
        HashMap<String, Integer> anioAndSemana = new HashMap<>();
        anioAndSemana.put("anio", calendarioEntity.get().getAnio());
        anioAndSemana.put("semana", calendarioEntity.get().getSemana());

        return anioAndSemana;
    }

    private static String getId(
            GerenciaModel gerenciaModel
    ) throws ExecutionException, InterruptedException {
        LocalDate date = LocalDate.now();

        CompletableFuture<CalendarioModel> calendarioEntity = ReporteDiarioAgenciasUtil.calendarioService
                .findByFechaActualAsync(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        date = date.plusDays(1);
        String diaSemana = date.format(DateTimeFormatter.ofPattern("EEEE", new Locale("es",
                "ES")));

        calendarioEntity.join();
        int anioActual = calendarioEntity.get().getAnio();
        int semanaActual = calendarioEntity.get().getSemana();

        return String.format("%s-%d-%d-%s", gerenciaModel.getGerenciaId(), anioActual, semanaActual, diaSemana);
    }

    private static EncabezadoReporteDiarioAgenciasDTO getEncabezado(
            GerenciaModel gerenciaModel
    ) throws ExecutionException, InterruptedException {
        EncabezadoReporteDiarioAgenciasDTO encabezado = new EncabezadoReporteDiarioAgenciasDTO();
        {
            CompletableFuture<UsuarioModel> usuarioEntityGerente = ReporteDiarioAgenciasUtil.usuarioService
                    .findByUsuarioAsync(gerenciaModel.getGerenciaId());
            CompletableFuture<UsuarioModel> usuarioEntitySeguridad = ReporteDiarioAgenciasUtil.usuarioService
                    .findByIdAsync(gerenciaModel.getSeguridadId());
            CompletableFuture<SucursalModel> sucursalEntity = ReporteDiarioAgenciasUtil.sucursalService
                    .findBySucursalIdAsync(gerenciaModel.getSucursalId());

            encabezado.setZona(gerenciaModel.getGerenciaId());
            encabezado.setFecha(ReporteDiarioAgenciasUtil.getFecha());
            encabezado.setSemana(ReporteDiarioAgenciasUtil.getSemana());
            encabezado.setHora("8:00");

            CompletableFuture.allOf(usuarioEntityGerente, usuarioEntitySeguridad, sucursalEntity).join();

            encabezado.setSucursal(sucursalEntity.get().getNombre());

            String nombreCompletoGerente;
            {
                nombreCompletoGerente = usuarioEntityGerente.get() != null
                        ? usuarioEntityGerente.get().getNombre() : null;
                nombreCompletoGerente += " ";
                nombreCompletoGerente += usuarioEntityGerente.get() != null
                        ? usuarioEntityGerente.get().getApellidoPaterno() : null;
                nombreCompletoGerente += " ";
                nombreCompletoGerente += usuarioEntityGerente.get() != null
                        ? usuarioEntityGerente.get().getApellidoMaterno() : null;
            }
            encabezado.setGerente(nombreCompletoGerente);

            String nombreCompletoSeguridad;
            {
                nombreCompletoSeguridad = usuarioEntitySeguridad.get().getNombre();
                nombreCompletoSeguridad += " ";
                nombreCompletoSeguridad += usuarioEntitySeguridad.get().getApellidoPaterno();
                nombreCompletoSeguridad += " ";
                nombreCompletoSeguridad += usuarioEntitySeguridad.get().getApellidoMaterno();
            }
            encabezado.setSeguridad(nombreCompletoSeguridad);
        }
        return encabezado;
    }

    private static String getFecha() {
        LocalDate date = LocalDate.now().plusDays(1);
        String diaSemana = date.format(DateTimeFormatter.ofPattern("EEEE", new Locale("es",
                "ES")));

        int dia = date.getDayOfMonth();
        String mes = date.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        int anio = date.getYear();

        return String.format("%s, %d de %s de %d", diaSemana, dia, mes, anio);
    }

    private static String getSemana() throws ExecutionException, InterruptedException {
        LocalDate date = LocalDate.now();

        CompletableFuture<CalendarioModel> calendarioEntity = ReporteDiarioAgenciasUtil.calendarioService
                .findByFechaActualAsync(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        date = date.plusDays(1);
        String diaSemana = date.format(DateTimeFormatter.ofPattern("EEEE", new Locale("es",
                "ES")));

        calendarioEntity.join();
        int anioActual = calendarioEntity.get().getAnio();
        int semanaActual = calendarioEntity.get().getSemana();

        int anioAnterior;
        int semanaAnterior;
        {
            if (
                    semanaActual == 1
            ) {
                anioAnterior = anioActual - 1;
                semanaAnterior = ReporteDiarioAgenciasUtil.calendarioService
                        .existsByAnioAndSemana(anioAnterior, 53) ? 53 : 52;
            } else {
                anioAnterior = anioActual;
                semanaAnterior = semanaActual - 1;
            }
        }

        return String.format("%s del SEM. %d %d al SEM. %d %d", diaSemana, semanaActual, anioActual,
                semanaAnterior, anioAnterior);
    }

    private static ArrayList<AgenciaReporteDiarioAgenciasDTO> getAgencias(
            GerenciaModel gerenciaModel,
            HashMap<String, Integer> anioAndSemana
    ) throws ExecutionException, InterruptedException {
        CompletableFuture<ArrayList<AgenciaModel>> agenciaEntities = ReporteDiarioAgenciasUtil.agenciaService
                .findByGerenciaIdAsync(gerenciaModel.getGerenciaId());

        agenciaEntities.join();

        ArrayList<AgenciaReporteDiarioAgenciasDTO> agencias = new ArrayList<>();

        for (AgenciaModel agenciaModel : agenciaEntities.get()) {
            CompletableFuture<UsuarioModel> usuarioEntityAgente = ReporteDiarioAgenciasUtil
                    .usuarioService.findByUsuarioAsync(agenciaModel.getId());

            AgenciaReporteDiarioAgenciasDTO agencia = new AgenciaReporteDiarioAgenciasDTO();
            {
                agencia.setAgencia(agenciaModel.getId());

                String nombreCompletoAgente;
                {
                    usuarioEntityAgente.join();
                    nombreCompletoAgente = usuarioEntityAgente.get() != null
                            ? usuarioEntityAgente.get().getNombre() : null;
                    nombreCompletoAgente += " ";
                    nombreCompletoAgente += usuarioEntityAgente.get() != null
                            ? usuarioEntityAgente.get().getApellidoPaterno() : null;
                    nombreCompletoAgente += " ";
                    nombreCompletoAgente += usuarioEntityAgente.get() != null
                            ? usuarioEntityAgente.get().getApellidoMaterno() : null;
                }
                agencia.setAgente(nombreCompletoAgente);

                agencia.setEfectivoActualAgente(0.00);
                agencia.setIsAgenciaCerrada(false);

                // To easy code
                int anio = anioAndSemana.get("anio");
                int semana = anioAndSemana.get("semana");

                agencia.setSemanaActual(ReporteDiarioAgenciasUtil.getDashboardSemanaActual(agencia.getAgencia(),
                        anio, semana));

                int semanaAnterior;
                int anioAnterior;
                {
                    if (
                            semana == 1
                    ) {
                        anioAnterior = anio - 1;
                        semanaAnterior = ReporteDiarioAgenciasUtil.calendarioService
                                .existsByAnioAndSemana(anioAnterior, 53) ? 53 : 52;
                    } else {
                        anioAnterior = anio;
                        semanaAnterior = semana - 1;
                    }
                }

                agencia.setSemanaAnterior(ReporteDiarioAgenciasUtil.getDashboardSemanaAnterior(agencia.getAgencia(),
                        anioAnterior, semanaAnterior));
            }
            agencias.add(agencia);
        }

        return agencias;
    }

    private static DashboardSemanaActualReporteDiarioAgenciasDTO getDashboardSemanaActual(
            String agencia,
            int anio,
            int semana
    ) throws ExecutionException, InterruptedException {
        DashboardSemanaActualReporteDiarioAgenciasDTO semanaActual =
                new DashboardSemanaActualReporteDiarioAgenciasDTO();

        int semanaAnterior;
        int anioAnterior;
        {
            if (
                    semana == 1
            ) {
                anioAnterior = anio - 1;
                semanaAnterior = ReporteDiarioAgenciasUtil.calendarioService
                        .existsByAnioAndSemana(anioAnterior, 53) ? 53 : 52;
            } else {
                anioAnterior = anio;
                semanaAnterior = semana - 1;
            }
        }

        CompletableFuture<Double> debitoTotal = ReporteDiarioAgenciasUtil.pagoService
                .getDebitoTotalByAgenciaAnioAndSemanaAsync(agencia, anio, semana);
        CompletableFuture<Double> excedente = ReporteDiarioAgenciasUtil.pagoService
                .getExcedenteByAgenciaAnioAndSemanaAsync(agencia, anio, semana);
        CompletableFuture<Double> cobranzaTotal = ReporteDiarioAgenciasUtil.pagoService
                .getCobranzaTotalByAgenciaAnioAndSemanaAsync(agencia, anio, semana);
        CompletableFuture<Integer> clientesTotalesCobrados = ReporteDiarioAgenciasUtil.pagoService
                .getClientesCobradosByAgenciaAnioAndSemanaAsync(agencia, anio, semana);
        CompletableFuture<Integer> clientesTotales = ReporteDiarioAgenciasUtil.pagoService
                .getClientesTotalesByAgenciaAnioAndSemanaAsync(agencia, anioAnterior, semanaAnterior);

        semanaActual.setDiferenciaFaltanteActualVsFaltanteAnterior(null);
        semanaActual.setDiferenciaAcumuladaFaltantes(null);
        semanaActual.setClientesCobradosMiercoles(null);
        semanaActual.setClientesCobradosJueves(null);
        semanaActual.setVentasTotales(null);
        semanaActual.setTotalVentas(null);

        CompletableFuture.allOf(debitoTotal, excedente, cobranzaTotal, clientesTotalesCobrados, clientesTotales);
        semanaActual.setDebitoTotal(debitoTotal.get());
        semanaActual.setExcedente(excedente.get());
        semanaActual.setCobranzaTotal(cobranzaTotal.get());
        semanaActual.setClientesTotalesCobrados(clientesTotalesCobrados.get());
        semanaActual.setClientesTotales(clientesTotales.get());

        semanaActual.setCobranzaPura(semanaActual.getDebitoTotal() - semanaActual.getExcedente());

        double porcentajeCobranzaPura = (100 / semanaActual.getDebitoTotal() * semanaActual.getCobranzaPura());
        semanaActual.setPorcentajeCobranzaPura(porcentajeCobranzaPura);

        semanaActual.setFaltante(semanaActual.getDebitoTotal() - semanaActual.getCobranzaPura());

        return semanaActual;
    }

    private static DashboardSemanaAnteriorReporteDiarioAgenciasDTO getDashboardSemanaAnterior(
            String agencia,
            int anio,
            int semana
    ) throws ExecutionException, InterruptedException {
        DashboardSemanaAnteriorReporteDiarioAgenciasDTO semanaAnterior =
                new DashboardSemanaAnteriorReporteDiarioAgenciasDTO();

        CompletableFuture<Double> debitoTotal = ReporteDiarioAgenciasUtil.pagoService
                .getDebitoTotalByAgenciaAnioAndSemanaAsync(agencia, anio, semana);
        CompletableFuture<Double> excedente = ReporteDiarioAgenciasUtil.pagoService
                .getExcedenteByAgenciaAnioAndSemanaAsync(agencia, anio, semana);
        CompletableFuture<Double> cobranzaTotal = ReporteDiarioAgenciasUtil.pagoService
                .getCobranzaTotalByAgenciaAnioAndSemanaAsync(agencia, anio, semana);
        CompletableFuture<Integer> clientesTotalesCobrados = ReporteDiarioAgenciasUtil.pagoService
                .getClientesCobradosByAgenciaAnioAndSemanaAsync(agencia, anio, semana);

        CompletableFuture.allOf(debitoTotal, excedente, cobranzaTotal, clientesTotalesCobrados);
        semanaAnterior.setDebitoTotal(debitoTotal.get());
        semanaAnterior.setExcedente(excedente.get());
        semanaAnterior.setCobranzaTotal(cobranzaTotal.get());
        semanaAnterior.setClientesTotalesCobrados(clientesTotalesCobrados.get());

        semanaAnterior.setCobranzaPura(semanaAnterior.getDebitoTotal() - semanaAnterior.getExcedente());

        semanaAnterior.setFaltante(null);
        semanaAnterior.setDiferenciaAcumuladaFaltantes(null);
        semanaAnterior.setClientesCobradosMiercoles(null);
        semanaAnterior.setClientesCobradosJueves(null);
        semanaAnterior.setClientesTotales(null);
        semanaAnterior.setTotalVentas(null);

        return semanaAnterior;
    }
}
