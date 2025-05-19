package tech.calaverita.sfast_xpress.dashboard_gerencia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.dashboard_agencia.DashboardAgenciaDto;
import tech.calaverita.sfast_xpress.dashboard_agencia.DashboardAgenciaService;
import tech.calaverita.sfast_xpress.dashboard_gerencia.pojo.DashboardGerencia;
import tech.calaverita.sfast_xpress.services.AgenciaService;

@Service
public class DashboardGerenciaService {
    private final AgenciaService agenciaService;
    private final DashboardAgenciaService dashboardAgenciaService;

    public DashboardGerenciaService(AgenciaService agenciaService, DashboardAgenciaService dashboardAgenciaService) {
        this.agenciaService = agenciaService;
        this.dashboardAgenciaService = dashboardAgenciaService;
    }

    public ResponseEntity<DashboardGerenciaDto> getByGerenciaAnioSemana(String gerencia, int anio, int semana) {
        List<String> agencias = this.agenciaService.findIdsByGerenciaId(gerencia);

        List<CompletableFuture<ResponseEntity<DashboardAgenciaDto>>> futures = new ArrayList<>();
        for (String agencia : agencias) {
            futures.add(this.dashboardAgenciaService.getByAgenciaAnioSemanaAsync(agencia, anio, semana));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        List<DashboardAgenciaDto> dashboardAgenciaDtos = new ArrayList<>();
        for (CompletableFuture<ResponseEntity<DashboardAgenciaDto>> dashboardAgenciaDto : futures) {
            if (dashboardAgenciaDto.join().getBody() != null) {
                dashboardAgenciaDtos.add(dashboardAgenciaDto.join().getBody());
            }
        }

        DashboardGerenciaDto dashboardGerenciaDto = new DashboardGerenciaDto(
                new DashboardGerencia(dashboardAgenciaDtos));

        return new ResponseEntity<>(dashboardGerenciaDto, HttpStatus.OK);
    }
}
