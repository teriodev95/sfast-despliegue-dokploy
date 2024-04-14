package tech.calaverita.reporterloanssql.utils.reportes;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.dto.reporte_general_gerencia.ArrastreReporteGeneralGerenciaDTO;
import tech.calaverita.reporterloanssql.dto.reporte_general_gerencia.AvanceReporteGeneralGerenciaDTO;
import tech.calaverita.reporterloanssql.dto.reporte_general_gerencia.DashboardReporteGeneralGerenciaDTO;
import tech.calaverita.reporterloanssql.dto.reporte_general_gerencia.EncabezadoReporteGeneralGerenciaDTO;
import tech.calaverita.reporterloanssql.models.mariaDB.CalendarioModel;
import tech.calaverita.reporterloanssql.models.mariaDB.GerenciaModel;
import tech.calaverita.reporterloanssql.models.mariaDB.SucursalModel;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;
import tech.calaverita.reporterloanssql.models.mongoDB.ReporteGeneralGerenciaDocument;
import tech.calaverita.reporterloanssql.services.CalendarioService;
import tech.calaverita.reporterloanssql.services.SucursalService;
import tech.calaverita.reporterloanssql.services.UsuarioService;
import tech.calaverita.reporterloanssql.services.reportes.ReporteGeneralGerenciaService;
import tech.calaverita.reporterloanssql.services.views.PagoAgrupadoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class ReporteGeneralGerenciaUtil {
    private static UsuarioService usuarioService;
    private static CalendarioService calendarioService;
    private static PagoAgrupadoService pagoAgrupadoService;
    private static SucursalService sucursalService;
    private static ReporteGeneralGerenciaService reporteGeneralGerenciaService;

    public ReporteGeneralGerenciaUtil(UsuarioService usuarioService, CalendarioService calendarioService,
                                      PagoAgrupadoService pagoAgrupadoService, SucursalService sucursalService,
                                      ReporteGeneralGerenciaService reporteGeneralGerenciaService) {
        ReporteGeneralGerenciaUtil.usuarioService = usuarioService;
        ReporteGeneralGerenciaUtil.calendarioService = calendarioService;
        ReporteGeneralGerenciaUtil.pagoAgrupadoService = pagoAgrupadoService;
        ReporteGeneralGerenciaUtil.sucursalService = sucursalService;
        ReporteGeneralGerenciaUtil.reporteGeneralGerenciaService = reporteGeneralGerenciaService;
    }

    public static ReporteGeneralGerenciaDocument getReporte(GerenciaModel gerenciaModel)
            throws ExecutionException, InterruptedException {
        ReporteGeneralGerenciaDocument reporte = new ReporteGeneralGerenciaDocument();
        {
            CalendarioModel calendarioModel = ReporteGeneralGerenciaUtil.getCalendarioEntity();

            // To easy code
            int semana = calendarioModel.getSemana();

            reporte.setId(ReporteGeneralGerenciaUtil.getId(gerenciaModel));
            reporte.setEncabezado(ReporteGeneralGerenciaUtil.getEncabezado(gerenciaModel, semana));

            ArrayList<DashboardReporteGeneralGerenciaDTO> dashboards = new ArrayList<>();
            dashboards.add(ReporteGeneralGerenciaUtil.getDashboardSemanaActual(reporte.getEncabezado(), calendarioModel));

            ArrayList<AvanceReporteGeneralGerenciaDTO> avances = new ArrayList<>();
            avances.add(ReporteGeneralGerenciaUtil.getAvanceSemanaActual(dashboards.get(0),
                    reporte.getEncabezado(), calendarioModel));

            ArrayList<ArrastreReporteGeneralGerenciaDTO> arrastres = new ArrayList<>();
            arrastres.add(ReporteGeneralGerenciaUtil.getArrastreSemanaActual(dashboards.get(0), semana));

            ReporteGeneralGerenciaUtil.funReportesAnterioresUltimos3Meses(dashboards, avances, arrastres,
                    calendarioModel, reporte.getId());

            reporte.setDashboards(dashboards);
            reporte.setAvances(avances);
            reporte.setArrastres(arrastres);

            // Solamente calculamos la diferencia actual vs la semana anterior  y se asigna a su dashboard
            ReporteGeneralGerenciaUtil.funDiferenciaVsSemanaAnterior(dashboards);

            // To easy code
            double perdidaAcumulada = ReporteGeneralGerenciaUtil.getPerdidaAcumulada(dashboards);

            reporte.setPerdidaAcumulada(perdidaAcumulada);
            reporte.setEfectivoGerente(null);
            reporte.setEfectivoCampo(null);
            reporte.setTotalEfectivo(null);
        }

        return reporte;
    }

    public static String getId(
            GerenciaModel gerenciaModel) throws ExecutionException, InterruptedException {
        LocalDate date = LocalDate.now();

        CompletableFuture<CalendarioModel> calendarioEntity = ReporteGeneralGerenciaUtil.calendarioService
                .findByFechaActualAsync(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        String diaSemana = date.format(DateTimeFormatter.ofPattern("EEEE", new Locale("es",
                "ES")));

        calendarioEntity.join();
        int anioActual = calendarioEntity.get().getAnio();
        int semanaActual = calendarioEntity.get().getSemana();

        return String.format("%s-%d-%d-%s", gerenciaModel.getGerenciaId(), anioActual, semanaActual, diaSemana);
    }

    private static EncabezadoReporteGeneralGerenciaDTO getEncabezado(GerenciaModel gerenciaModel, int semana)
            throws ExecutionException, InterruptedException {
        EncabezadoReporteGeneralGerenciaDTO encabezado = new EncabezadoReporteGeneralGerenciaDTO();
        {
            CompletableFuture<UsuarioModel> usuarioEntityGerente = ReporteGeneralGerenciaUtil.usuarioService
                    .findByUsuarioAsync(gerenciaModel.getGerenciaId());
            CompletableFuture<UsuarioModel> usuarioEntitySeguridad = ReporteGeneralGerenciaUtil.usuarioService
                    .findByIdAsync(gerenciaModel.getSeguridadId());
            CompletableFuture<SucursalModel> sucursalEntity = ReporteGeneralGerenciaUtil.sucursalService
                    .findBySucursalIdAsync(gerenciaModel.getSucursalId());

            encabezado.setZona(gerenciaModel.getGerenciaId());
            encabezado.setFecha(ReporteGeneralGerenciaUtil.getFecha());
            encabezado.setSemana(semana);

            String hora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            encabezado.setHora(hora);

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
        LocalDate date = LocalDate.now();
        String diaSemana = date.format(DateTimeFormatter.ofPattern("EEEE", new Locale("es",
                "ES")));

        int dia = date.getDayOfMonth();
        String mes = date.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        int anio = date.getYear();

        return String.format("%s, %d de %s de %d", diaSemana, dia, mes, anio);
    }

    private static DashboardReporteGeneralGerenciaDTO getDashboardSemanaActual(
            EncabezadoReporteGeneralGerenciaDTO encabezado, CalendarioModel calendarioModel)
            throws ExecutionException, InterruptedException {
        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        DashboardReporteGeneralGerenciaDTO dashboard = new DashboardReporteGeneralGerenciaDTO();
        {
            ReporteGeneralGerenciaUtil.funEstadisticasSemanaActual(dashboard, encabezado, calendarioModel);
            dashboard.setConcepto("SEM. " + semana + " " + anio);
            dashboard.setCobranzaPura(dashboard.getCobranzaTotal() - dashboard.getExcedente());
            dashboard.setDiferenciaCobranzaPuraVsDebitoTotal(dashboard.getDebitoTotal() - dashboard.getCobranzaPura());
            dashboard.setPorcentajeCobranzaPura(100 / dashboard.getDebitoTotal() * dashboard.getCobranzaPura());
            dashboard.setDiferenciaActualVsDiferenciaAnterior(null);
            dashboard.setTotalVentas(null);
        }

        return dashboard;
    }

    private static void funEstadisticasSemanaActual(DashboardReporteGeneralGerenciaDTO dashboard,
                                                    EncabezadoReporteGeneralGerenciaDTO encabezado,
                                                    CalendarioModel calendarioModel)
            throws ExecutionException, InterruptedException {
        // To easy code
        int anio;
        int semana;
        String gerencia = String.format("Ger%s", encabezado.getZona().substring(4));
        String sucursal = encabezado.getSucursal().split(" ")[0];

        LocalDate date = LocalDate.now();
        String diaSemana = date.format(DateTimeFormatter.ofPattern("EEEE", new Locale("es",
                "ES")));

        CompletableFuture<Double> debitoTotal;
        ReporteGeneralGerenciaUtil.funSemanaAnterior(calendarioModel);
        anio = calendarioModel.getAnio();
        semana = calendarioModel.getSemana();

        if (diaSemana.equals("jueves")) {
            debitoTotal = ReporteGeneralGerenciaUtil.pagoAgrupadoService
                    .findDebitoTotalParcialByGerenciaSucursalAnioAndSemanaAsync(gerencia, sucursal, anio, semana);
        } else {
            debitoTotal = ReporteGeneralGerenciaUtil.pagoAgrupadoService
                    .findDebitoTotalSemanaByGerenciaSucursalAnioAndSemanaAsync(gerencia, sucursal, anio, semana);
        }

        boolean existsSemana53 = ReporteGeneralGerenciaUtil.calendarioService
                .existsByAnioAndSemana(anio, 53);
        ReporteGeneralGerenciaUtil.funSemanaSiguiente(calendarioModel, existsSemana53);

        anio = calendarioModel.getAnio();
        semana = calendarioModel.getSemana();

        CompletableFuture<Double> cobranzaTotal = ReporteGeneralGerenciaUtil.pagoAgrupadoService
                .findCobranzaTotalByGerenciaSucursalAnioAndSemanaAsync(gerencia, sucursal, anio, semana);
        CompletableFuture<Double> excedente = ReporteGeneralGerenciaUtil.pagoAgrupadoService
                .findExcedenteByGerenciaSucursalAnioAndSemanaAsync(gerencia, sucursal, anio, semana);

        CompletableFuture.allOf(debitoTotal, cobranzaTotal, excedente);
        dashboard.setDebitoTotal(debitoTotal.get());
        dashboard.setCobranzaTotal(cobranzaTotal.get());
        dashboard.setExcedente(excedente.get());
    }

    private static AvanceReporteGeneralGerenciaDTO getAvanceSemanaActual(DashboardReporteGeneralGerenciaDTO dashboard,
                                                                         EncabezadoReporteGeneralGerenciaDTO encabezado,
                                                                         CalendarioModel calendarioModel)
            throws ExecutionException, InterruptedException {
        AvanceReporteGeneralGerenciaDTO avance = new AvanceReporteGeneralGerenciaDTO();
        {
            ReporteGeneralGerenciaUtil.funSemanaAnterior(calendarioModel);

            // To easy code
            int anio = calendarioModel.getAnio();
            int semana = calendarioModel.getSemana();
            String gerencia = String.format("Ger%s", encabezado.getZona().substring(4));
            String sucursal = encabezado.getSucursal().split(" ")[0];

            boolean existsSemana53 = ReporteGeneralGerenciaUtil.calendarioService
                    .existsByAnioAndSemana(anio, 53);
            ReporteGeneralGerenciaUtil.funSemanaSiguiente(calendarioModel, existsSemana53);

            CompletableFuture<Double> debitoTotal = ReporteGeneralGerenciaUtil.pagoAgrupadoService
                    .findDebitoTotalSemanaByGerenciaSucursalAnioAndSemanaAsync(gerencia, sucursal, anio, semana);

            avance.setConcepto(dashboard.getConcepto());
            avance.setCobranzaPura(dashboard.getCobranzaPura());

            debitoTotal.join();
            avance.setDebitoTotal(debitoTotal.get());
            avance.setPorcentajeCobranzaPura(100 / avance.getDebitoTotal() * avance.getCobranzaPura());
        }

        return avance;
    }

    private static ArrastreReporteGeneralGerenciaDTO getArrastreSemanaActual(
            DashboardReporteGeneralGerenciaDTO dashboard, int semana) {
        ArrastreReporteGeneralGerenciaDTO arrastre = new ArrastreReporteGeneralGerenciaDTO();
        {
            arrastre.setDebitoTotal(dashboard.getDebitoTotal());
            arrastre.setCobranzaPura(dashboard.getCobranzaPura());
            arrastre.setVentas(dashboard.getTotalVentas());
            arrastre.setPorcentajeCobranzaPura(dashboard.getPorcentajeCobranzaPura());
            arrastre.setSemana(semana);
        }

        return arrastre;
    }

    private static void funReportesAnterioresUltimos3Meses(ArrayList<DashboardReporteGeneralGerenciaDTO> dashboards,
                                                           ArrayList<AvanceReporteGeneralGerenciaDTO> avances,
                                                           ArrayList<ArrastreReporteGeneralGerenciaDTO> arrastres,
                                                           CalendarioModel calendarioModel, String reporteId) {
        String[] reporteIdSplit = reporteId.split("-");

        String gerencia = reporteIdSplit[0];
        String diaSemana = reporteIdSplit[3];

        for (int i = 0; i < 12; i++) {
            ReporteGeneralGerenciaUtil.funSemanaAnterior(calendarioModel);

            // To easy code
            int anio = calendarioModel.getAnio();
            int semana = calendarioModel.getSemana();

            String id = String.format("%s-%d-%d-%s", gerencia, anio, semana, diaSemana);

            Optional<ReporteGeneralGerenciaDocument> reporte = ReporteGeneralGerenciaUtil
                    .reporteGeneralGerenciaService.findById(id);

            if (reporte.isPresent()) {
                if (i < 4) {
                    dashboards.add(reporte.get().getDashboards().get(0));
                    avances.add(reporte.get().getAvances().get(0));
                }
                arrastres.add(reporte.get().getArrastres().get(0));
            }
        }
    }

    private static void funDiferenciaVsSemanaAnterior(ArrayList<DashboardReporteGeneralGerenciaDTO> dashboards) {
        for (int i = 0; i < dashboards.size(); i++) {
            if ((i + 1) < dashboards.size()) {
                // To easy code
                double diferenciaSemanaAnterior = dashboards.get(i + 1)
                        .getDiferenciaCobranzaPuraVsDebitoTotal();
                double diferenciaSemanaActual = dashboards.get(i).getDiferenciaCobranzaPuraVsDebitoTotal();

                dashboards.get(i).setDiferenciaActualVsDiferenciaAnterior(diferenciaSemanaAnterior
                        - diferenciaSemanaActual);
            } else {
                // To easy code
                DashboardReporteGeneralGerenciaDTO dashboard = dashboards.get(i);

                dashboard.setDiferenciaActualVsDiferenciaAnterior(0.0);
            }
        }
    }

    private static double getPerdidaAcumulada(ArrayList<DashboardReporteGeneralGerenciaDTO> dashboards) {
        double perdidaAcumulada = 0;

        for (DashboardReporteGeneralGerenciaDTO dashboard : dashboards) {
            perdidaAcumulada += dashboard.getDiferenciaActualVsDiferenciaAnterior();
        }

        return perdidaAcumulada;
    }

    private static void funSemanaAnterior(CalendarioModel calendarioModel) {
        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        if (semana == 1) {
            anio = anio - 1;
            semana = ReporteGeneralGerenciaUtil.calendarioService
                    .existsByAnioAndSemana(anio, 53) ? 53 : 52;
        } else {
            semana = semana - 1;
        }

        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);
    }

    private static void funSemanaSiguiente(CalendarioModel calendarioModel, boolean existsSemana53) {
        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        if (semana == 52 && existsSemana53) {
            semana = 53;
        } else if (semana == 52 || semana == 53) {
            anio = anio + 1;
            semana = 1;
        } else {
            semana = semana + 1;
        }

        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);
    }

    private static CalendarioModel getCalendarioEntity() throws ExecutionException, InterruptedException {
        LocalDate date = LocalDate.now();

        CompletableFuture<CalendarioModel> calendarioEntity = ReporteGeneralGerenciaUtil.calendarioService
                .findByFechaActualAsync(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        calendarioEntity.join();

        return calendarioEntity.get();
    }
}
