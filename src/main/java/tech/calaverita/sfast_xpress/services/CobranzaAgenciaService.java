package tech.calaverita.sfast_xpress.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.pojos.AlmacenObjects;
import tech.calaverita.sfast_xpress.pojos.CobranzaAgencia;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;

@Service
public class CobranzaAgenciaService {
    private final AgenciaService agenciaService;
    private final CalendarioService calendarioService;
    private final PagoDynamicService pagoDynamicService;
    private final PrestamoViewService prestamoViewService;
    private final UsuarioService usuarioService;
    private final VentaService ventaService;

    public CobranzaAgenciaService(AgenciaService agenciaService, CalendarioService calendarioService,
            PagoDynamicService pagoDynamicService, PrestamoViewService prestamoViewService,
            UsuarioService usuarioService,
            VentaService ventaService) {
        this.agenciaService = agenciaService;
        this.calendarioService = calendarioService;
        this.pagoDynamicService = pagoDynamicService;
        this.prestamoViewService = prestamoViewService;
        this.usuarioService = usuarioService;
        this.ventaService = ventaService;
    }

    public CobranzaAgencia getCobranzaAgenciaByAgencia(String agencia) {
        return null;
    }

    public CobranzaAgencia getCobranzaAgenciaByAgenciaAnioSemana() {
        return null;
    }

    public List<CobranzaAgencia> getCobranzaAgenciasByGerencia(String gerencia) {
        CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());

        List<CobranzaAgencia> cobranzaAgencias = new ArrayList<>();
        if (calendarioModel != null) {
            cobranzaAgencias = this.getCobranzaAgenciasByGerenciaAnioSemana(gerencia,
                    calendarioModel.getAnio(), calendarioModel.getSemana());
        }

        return cobranzaAgencias;
    }

    public List<CobranzaAgencia> getCobranzaAgenciasByGerenciaAnioSemana(String gerencia, int anio,
            int semana) {
        List<CobranzaAgencia> cobranzaAgencias = new ArrayList<>();

        CompletableFuture<List<PagoDynamicModel>> pagoDynamicModelsCf = this.pagoDynamicService
                .findByGerenciaAnioSemanaEsPrimerPagoAsync(gerencia, anio, semana, false);
        CompletableFuture<List<PrestamoViewModel>> prestamoViewModelsCf = this.prestamoViewService
                .findByGerenciaSaldoAlIniciarSemanaGreaterThan(gerencia, 0.0);
        CompletableFuture<List<UsuarioModel>> usuarioModelsCf = this.usuarioService
                .findAgentesByGerenciaTipoAndStatusAsync(gerencia, "Agente", true);
        CompletableFuture<List<VentaModel>> ventaModelsCf = this.ventaService.findByGerenciaAnioSemanaAsync(gerencia,
                anio, semana);

        CompletableFuture.allOf(pagoDynamicModelsCf, prestamoViewModelsCf, usuarioModelsCf, ventaModelsCf).join();

        ArrayList<String> agenciasIds = this.agenciaService.findIdsByGerenciaId(gerencia);
        for (String agencia : agenciasIds) {
            List<PagoDynamicModel> pagoDynamicModels = getPagosByAgencia(pagoDynamicModelsCf.join(), agencia);
            List<PrestamoViewModel> prestamoViewModels = getPrestamosByAgencia(prestamoViewModelsCf.join(), agencia);
            List<VentaModel> ventaModels = getVentasByAgencia(ventaModelsCf.join(), agencia);

            String agente = "Sin Agente Asignado";
            for (UsuarioModel usuarioModel : usuarioModelsCf.join()) {
                if (agencia.equals(usuarioModel.getAgencia())) {
                    agente = usuarioModel.getNombre();
                    break;
                }
            }

            AlmacenObjects almacenObjects = new AlmacenObjects();
            almacenObjects.addObject("agencia", agencia);
            almacenObjects.addObject("agente", agente);
            almacenObjects.addObject("pagoDynamicModels", pagoDynamicModels);
            almacenObjects.addObject("prestamoViewModels", prestamoViewModels);
            almacenObjects.addObject("ventaModels", ventaModels);

            cobranzaAgencias.add(new CobranzaAgencia(almacenObjects));
        }

        return cobranzaAgencias;
    }

    private List<PagoDynamicModel> getPagosByAgencia(List<PagoDynamicModel> pagoDynamicModels, String agencia) {
        List<PagoDynamicModel> pagoDynamicModelsFiltrados = new ArrayList<>();

        for (PagoDynamicModel pagoDynamicModel : pagoDynamicModels) {
            if (pagoDynamicModel.getAgencia().equals(agencia)) {
                pagoDynamicModelsFiltrados.add(pagoDynamicModel);
            }
        }

        return pagoDynamicModelsFiltrados;
    }

    private List<PrestamoViewModel> getPrestamosByAgencia(List<PrestamoViewModel> prestamoViewModels, String agencia) {
        List<PrestamoViewModel> prestamoViewModelsFiltrados = new ArrayList<>();

        for (PrestamoViewModel prestamoViewModel : prestamoViewModels) {
            if (prestamoViewModel.getAgencia().equals(agencia)) {
                prestamoViewModelsFiltrados.add(prestamoViewModel);
            }
        }

        return prestamoViewModelsFiltrados;
    }

    private List<VentaModel> getVentasByAgencia(List<VentaModel> ventaModels, String agencia) {
        List<VentaModel> ventaModelsFiltrados = new ArrayList<>();

        for (VentaModel ventaModel : ventaModels) {
            if (ventaModel.getAgencia().equals(agencia)) {
                ventaModelsFiltrados.add(ventaModel);
            }
        }

        return ventaModelsFiltrados;
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<CobranzaAgencia>> getCobranzaAgenciasByGerenciaAsync(String gerencia) {
        return CompletableFuture.completedFuture(getCobranzaAgenciasByGerencia(gerencia));
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<CobranzaAgencia>> getCobranzaAgenciasByGerenciaAnioSemanaAsync(String gerencia,
            int anio, int semana) {
        return CompletableFuture.completedFuture(getCobranzaAgenciasByGerenciaAnioSemana(gerencia, anio, semana));
    }
}
