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
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.SucursalService;
import tech.calaverita.reporterloanssql.services.UsuarioService;
import tech.calaverita.reporterloanssql.services.reportes.ReporteGeneralGerenciaService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class ReporteGeneralGerenciaMigracionUtil {
    private static UsuarioService usuarioService;
    private static CalendarioService calendarioService;
    private static PagoService pagoService;
    private static SucursalService sucursalService;
    private static ReporteGeneralGerenciaService reporteGeneralGerenciaService;

    public ReporteGeneralGerenciaMigracionUtil(
            UsuarioService usuarioService,
            CalendarioService calendarioService,
            PagoService pagoService,
            SucursalService sucursalService,
            ReporteGeneralGerenciaService reporteGeneralGerenciaService
    ) {
        ReporteGeneralGerenciaMigracionUtil.usuarioService = usuarioService;
        ReporteGeneralGerenciaMigracionUtil.calendarioService = calendarioService;
        ReporteGeneralGerenciaMigracionUtil.pagoService = pagoService;
        ReporteGeneralGerenciaMigracionUtil.sucursalService = sucursalService;
        ReporteGeneralGerenciaMigracionUtil.reporteGeneralGerenciaService = reporteGeneralGerenciaService;
    }

    public static ReporteGeneralGerenciaDocument getReporte(
            GerenciaModel gerenciaModel,
            CalendarioModel calendarioModel,
            String fechaDiaSemana
    ) throws ExecutionException, InterruptedException {
        ReporteGeneralGerenciaDocument reporte = new ReporteGeneralGerenciaDocument();
        {
            reporte.setId(ReporteGeneralGerenciaMigracionUtil.getId(gerenciaModel, calendarioModel, fechaDiaSemana));
            reporte.setEncabezado(ReporteGeneralGerenciaMigracionUtil.getEncabezado(gerenciaModel, fechaDiaSemana,
                    calendarioModel.getSemana()));

            ArrayList<DashboardReporteGeneralGerenciaDTO> dashboards = new ArrayList<>();
            dashboards.add(ReporteGeneralGerenciaMigracionUtil.getDashboardSemanaActual(reporte.getEncabezado(),
                    calendarioModel, fechaDiaSemana));

            ArrayList<AvanceReporteGeneralGerenciaDTO> avances = new ArrayList<>();
            avances.add(ReporteGeneralGerenciaMigracionUtil.getAvanceSemanaActual(dashboards.get(0),
                    reporte.getEncabezado(), calendarioModel));

            ArrayList<ArrastreReporteGeneralGerenciaDTO> arrastres = new ArrayList<>();
            arrastres.add(ReporteGeneralGerenciaMigracionUtil.getArrastreSemanaActual(dashboards.get(0),
                    calendarioModel.getSemana()));

            ReporteGeneralGerenciaMigracionUtil.funReportesAnterioresUltimos3Meses(dashboards, avances, arrastres,
                    calendarioModel, reporte.getId());

            reporte.setDashboards(dashboards);
            reporte.setAvances(avances);
            reporte.setArrastres(arrastres);

            // Solamente calculamos la diferencia actual vs la semana anterior  y se asigna a su dashboard
            ReporteGeneralGerenciaMigracionUtil.funDiferenciaVsSemanaAnterior(dashboards);

            // To easy code
            double perdidaAcumulada = ReporteGeneralGerenciaMigracionUtil.getPerdidaAcumulada(dashboards);

            reporte.setPerdidaAcumulada(perdidaAcumulada);
            reporte.setEfectivoGerente(null);
            reporte.setEfectivoCampo(null);
            reporte.setTotalEfectivo(null);
        }

        return reporte;
    }

    private static String getId(
            GerenciaModel gerenciaModel,
            CalendarioModel calendarioModel,
            String fecha
    ) {
        LocalDate date = LocalDate.parse(fecha);

        String diaSemana = date.format(DateTimeFormatter.ofPattern("EEEE", new Locale("es",
                "ES")));

        int anioActual = calendarioModel.getAnio();
        int semanaActual = calendarioModel.getSemana();

        return String.format("%s-%d-%d-%s", gerenciaModel.getGerenciaId(), anioActual, semanaActual, diaSemana);
    }

    private static EncabezadoReporteGeneralGerenciaDTO getEncabezado(
            GerenciaModel gerenciaModel,
            String fecha,
            int semana
    ) throws ExecutionException, InterruptedException {
        EncabezadoReporteGeneralGerenciaDTO encabezado = new EncabezadoReporteGeneralGerenciaDTO();
        {
            CompletableFuture<Optional<UsuarioModel>> usuarioEntityGerente = ReporteGeneralGerenciaMigracionUtil
                    .usuarioService.findByUsuarioAsync(gerenciaModel.getGerenciaId());
            CompletableFuture<UsuarioModel> usuarioEntitySeguridad = ReporteGeneralGerenciaMigracionUtil.usuarioService
                    .findByUsuarioIdAsync(gerenciaModel.getSeguridadId());
            CompletableFuture<SucursalModel> sucursalEntity = ReporteGeneralGerenciaMigracionUtil.sucursalService
                    .findBySucursalIdAsync(gerenciaModel.getSucursalId());

            encabezado.setZona(gerenciaModel.getGerenciaId());
            encabezado.setFecha(ReporteGeneralGerenciaMigracionUtil.getFecha(fecha));
            encabezado.setSemana(semana);
            encabezado.setHora("8:00");

            CompletableFuture.allOf(usuarioEntityGerente, usuarioEntitySeguridad, sucursalEntity).join();

            encabezado.setSucursal(sucursalEntity.get().getNombre());

            String nombreCompletoGerente;
            {
                nombreCompletoGerente = usuarioEntityGerente.get().isPresent()
                        ? usuarioEntityGerente.get().get().getNombre() : null;
                nombreCompletoGerente += " ";
                nombreCompletoGerente += usuarioEntityGerente.get().isPresent()
                        ? usuarioEntityGerente.get().get().getApellidoPaterno() : null;
                nombreCompletoGerente += " ";
                nombreCompletoGerente += usuarioEntityGerente.get().isPresent()
                        ? usuarioEntityGerente.get().get().getApellidoMaterno() : null;
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

    private static String getFecha(
            String fecha
    ) {
        LocalDate date = LocalDate.parse(fecha);
        String diaSemana = date.format(DateTimeFormatter.ofPattern("EEEE", new Locale("es",
                "ES")));

        int dia = date.getDayOfMonth();
        String mes = date.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        int anio = date.getYear();

        return String.format("%s, %d de %s de %d", diaSemana, dia, mes, anio);
    }

    private static DashboardReporteGeneralGerenciaDTO getDashboardSemanaActual(
            EncabezadoReporteGeneralGerenciaDTO encabezado,
            CalendarioModel calendarioModel,
            String fecha
    )
            throws ExecutionException, InterruptedException {
        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        DashboardReporteGeneralGerenciaDTO dashboard = new DashboardReporteGeneralGerenciaDTO();
        {
            ReporteGeneralGerenciaMigracionUtil.funEstadisticasSemanaActual(dashboard, encabezado, calendarioModel,
                    fecha);
            dashboard.setConcepto("SEM. " + semana + " " + anio);
            dashboard.setCobranzaPura(dashboard.getCobranzaTotal() - dashboard.getExcedente());
            dashboard.setDiferenciaCobranzaPuraVsDebitoTotal(dashboard.getDebitoTotal() - dashboard.getCobranzaPura());
            dashboard.setPorcentajeCobranzaPura(100 / dashboard.getDebitoTotal() * dashboard.getCobranzaPura());
            dashboard.setTotalVentas(null);
        }

        return dashboard;
    }

    private static void funEstadisticasSemanaActual(
            DashboardReporteGeneralGerenciaDTO dashboard,
            EncabezadoReporteGeneralGerenciaDTO encabezado,
            CalendarioModel calendarioModel,
            String fecha
    ) throws ExecutionException, InterruptedException {
        // To easy code
        int anio;
        int semana;
        String gerencia = String.format("Ger%s", encabezado.getZona().substring(4));
        String sucursal = encabezado.getSucursal().split(" ")[0];

        LocalDate date = LocalDate.parse(fecha);
        String diaSemana = date.format(DateTimeFormatter.ofPattern("EEEE", new Locale("es",
                "ES")));

        CompletableFuture<Double> debitoTotal;
        ReporteGeneralGerenciaMigracionUtil.funSemanaAnterior(calendarioModel);
        anio = calendarioModel.getAnio();
        semana = calendarioModel.getSemana();
        if (
                diaSemana.equals("jueves")
        ) {
            debitoTotal = ReporteGeneralGerenciaMigracionUtil.pagoService
                    .getDebitoTotalParcialByGerenciaAnioSemanaAndFechaAsync(gerencia, sucursal, anio, semana);
        } else {
            debitoTotal = ReporteGeneralGerenciaMigracionUtil.pagoService
                    .getDebitoTotalSemanaByGerenciaAnioSemanaAndFechaAsync(gerencia, sucursal, anio, semana);
        }
        ReporteGeneralGerenciaMigracionUtil.funSemanaSiguiente(calendarioModel);
        anio = calendarioModel.getAnio();
        semana = calendarioModel.getSemana();

        CompletableFuture<Double> cobranzaTotal = ReporteGeneralGerenciaMigracionUtil.pagoService
                .getCobranzaTotalByGerenciaAnioSemanaAndFechaAsync(gerencia, sucursal, anio, semana, fecha);
        CompletableFuture<Double> excedente = ReporteGeneralGerenciaMigracionUtil.pagoService
                .getExcedenteByGerenciaAnioSemanaAndFechaAsync(gerencia, sucursal, anio, semana, fecha);
        CompletableFuture<Integer> clientesCobrados = ReporteGeneralGerenciaMigracionUtil.pagoService
                .getClientesCobradosByGerenciaAnioSemanaAndFechaAsync(gerencia, sucursal, anio, semana, fecha);

        CompletableFuture.allOf(debitoTotal, cobranzaTotal, excedente, clientesCobrados);
        dashboard.setDebitoTotal(debitoTotal.get());
        dashboard.setCobranzaTotal(cobranzaTotal.get());
        dashboard.setExcedente(excedente.get());
        dashboard.setClientesCobrados(clientesCobrados.get());
    }

    private static AvanceReporteGeneralGerenciaDTO getAvanceSemanaActual(
            DashboardReporteGeneralGerenciaDTO dashboard,
            EncabezadoReporteGeneralGerenciaDTO encabezado,
            CalendarioModel calendarioModel
    ) throws ExecutionException, InterruptedException {
        AvanceReporteGeneralGerenciaDTO avance = new AvanceReporteGeneralGerenciaDTO();
        {
            ReporteGeneralGerenciaMigracionUtil.funSemanaAnterior(calendarioModel);

            // To easy code
            int anio = calendarioModel.getAnio();
            int semana = calendarioModel.getSemana();
            String gerencia = String.format("Ger%s", encabezado.getZona().substring(4));
            String sucursal = encabezado.getSucursal().split(" ")[0];

            ReporteGeneralGerenciaMigracionUtil.funSemanaSiguiente(calendarioModel);

            CompletableFuture<Double> debitoTotal = ReporteGeneralGerenciaMigracionUtil.pagoService
                    .getDebitoTotalSemanaByGerenciaAnioAndSemanaAsync(gerencia, sucursal, anio, semana);

            avance.setConcepto(dashboard.getConcepto());
            avance.setCobranzaPura(dashboard.getCobranzaPura());

            debitoTotal.join();
            avance.setDebitoTotal(debitoTotal.get());
            avance.setPorcentajeCobranzaPura(100 / avance.getDebitoTotal() * avance.getCobranzaPura());
        }

        return avance;
    }

    private static ArrastreReporteGeneralGerenciaDTO getArrastreSemanaActual(
            DashboardReporteGeneralGerenciaDTO dashboard,
            int semana
    ) {
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

    private static void funReportesAnterioresUltimos3Meses(
            ArrayList<DashboardReporteGeneralGerenciaDTO> dashboards,
            ArrayList<AvanceReporteGeneralGerenciaDTO> avances,
            ArrayList<ArrastreReporteGeneralGerenciaDTO> arrastres,

            CalendarioModel calendarioModel,
            String reporteId
    ) {
        String[] reporteIdSplit = reporteId.split("-");

        String gerencia = reporteIdSplit[0];
        String diaSemana = reporteIdSplit[3];

        for (int i = 0; i < 12; i++) {
            ReporteGeneralGerenciaMigracionUtil.funSemanaAnterior(calendarioModel);

            // To easy code
            int anio = calendarioModel.getAnio();
            int semana = calendarioModel.getSemana();

            String id = String.format("%s-%d-%d-%s", gerencia, anio, semana, diaSemana);

            Optional<ReporteGeneralGerenciaDocument> reporte = ReporteGeneralGerenciaMigracionUtil
                    .reporteGeneralGerenciaService.findById(id);

            if (
                    reporte.isPresent()
            ) {
                dashboards.add(reporte.get().getDashboards().get(0));
                avances.add(reporte.get().getAvances().get(0));
                arrastres.add(reporte.get().getArrastres().get(0));
            }
        }
    }

    private static void funDiferenciaVsSemanaAnterior(
            ArrayList<DashboardReporteGeneralGerenciaDTO> dashboards
    ) {
        for (int i = 0; i < dashboards.size(); i++) {
            if (
                    (i + 1) < dashboards.size()
            ) {
                // To easy code
                double diferenciaSemanaAnterior = dashboards.get(i + 1)
                        .getDiferenciaCobranzaPuraVsDebitoTotal();
                double diferenciaSemanaActual = dashboards.get(i).getDiferenciaCobranzaPuraVsDebitoTotal();

                dashboards.get(i).setDiferenciaActualVsDiferenciaAnterior(diferenciaSemanaAnterior
                        - diferenciaSemanaActual);
            } //
            else {
                // To easy code
                DashboardReporteGeneralGerenciaDTO dashboard = dashboards.get(i);

                dashboard.setDiferenciaActualVsDiferenciaAnterior(0.0);
            }
        }
    }

    private static double getPerdidaAcumulada(
            ArrayList<DashboardReporteGeneralGerenciaDTO> dashboards
    ) {
        double perdidaAcumulada = 0;

        for (DashboardReporteGeneralGerenciaDTO dashboard : dashboards) {
            perdidaAcumulada += dashboard.getDiferenciaActualVsDiferenciaAnterior();
        }

        return perdidaAcumulada;
    }

    private static void funSemanaAnterior(
            CalendarioModel calendarioModel
    ) {
        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        if (
                semana == 1
        ) {
            anio = anio - 1;
            semana = ReporteGeneralGerenciaMigracionUtil.calendarioService
                    .existsByAnioAndSemana(anio, 53) ? 53 : 52;
        } //
        else {
            semana = semana - 1;
        }

        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);
    }

    private static void funSemanaSiguiente(
            CalendarioModel calendarioModel
    ) {
        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        boolean existsSemana53 = ReporteGeneralGerenciaMigracionUtil.calendarioService
                .existsByAnioAndSemana(anio, 53);

        if (
                semana == 52 && existsSemana53
        ) {
            semana = 53;
        } //
        else if (
                semana == 52 || semana == 53
        ) {
            anio = anio + 1;
            semana = 1;
        }
        //
        else {
            semana = semana + 1;
        }

        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);
    }

    public static ArrayList<String> getFechaDiasSemana(
            CalendarioModel calendarioModel
    ) {
        LocalDate fecha = LocalDate.parse(calendarioModel.getDesde());

        ArrayList<String> fechaDiasSemana = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            fecha = fecha.plusDays(1);
            fechaDiasSemana.add(fecha.toString());
        }

        fechaDiasSemana.remove(3);

        return fechaDiasSemana;
    }

//    private static HashMap<String, Integer> getAnioAndSemana(
//            int anio,
//            int semana
//    ) throws ExecutionException, InterruptedException {
//        CompletableFuture<CalendarioEntity> calendarioEntity = ReporteGeneralGerenciaMigracionUtil.calendarioService
//                .findByAnioAndSemanaAsync(anio, semana);
//
//        calendarioEntity.join();
//        HashMap<String, Integer> anioAndSemana = new HashMap<>();
//        anioAndSemana.put("anio", calendarioEntity.get().getAnio());
//        anioAndSemana.put("semana", calendarioEntity.get().getSemana());
//
//        return anioAndSemana;
//    }
}
