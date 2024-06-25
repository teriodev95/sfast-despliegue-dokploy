package tech.calaverita.sfast_xpress.controllers;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.sfast_xpress.DTOs.ReporteCallCenterDTO;
import tech.calaverita.sfast_xpress.mappers.ReporteCallCenterMapper;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.GerenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.ReporteCallCenterLiteModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VisitaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoModel;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.GerenciaService;
import tech.calaverita.sfast_xpress.services.ReporteCallCenterService;
import tech.calaverita.sfast_xpress.services.VisitaService;
import tech.calaverita.sfast_xpress.services.views.PrestamoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/call_center")
public class CallCenterController {
    private final ReporteCallCenterService reporteCallCenterService;
    private final GerenciaService gerenciaService;
    private final VisitaService visitaService;
    private final PrestamoService prestamoService;
    private final CalendarioService calendarioService;

    public CallCenterController(ReporteCallCenterService reporteCallCenterService, GerenciaService gerenciaService,
                                VisitaService visitaService, PrestamoService prestamoService,
                                CalendarioService calendarioService) {
        this.reporteCallCenterService = reporteCallCenterService;
        this.gerenciaService = gerenciaService;
        this.visitaService = visitaService;
        this.prestamoService = prestamoService;
        this.calendarioService = calendarioService;
    }

    @GetMapping("/reportes/usuario/{usuario}")
    public ResponseEntity<ArrayList<ReporteCallCenterDTO>> getReportesByUserSeguridad(@PathVariable String usuario) {
        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
        ArrayList<ReporteCallCenterDTO> reportesCallCenterDTO = new ArrayList<>();

        ArrayList<GerenciaModel> gerenciaModels = this.gerenciaService.findByUsuario(usuario);
        ArrayList<ReporteCallCenterLiteModel> reportesCallCenterLiteModel = new ArrayList<>();
        gerenciaModels.forEach(gerenciaModel -> reportesCallCenterLiteModel.addAll(this.reporteCallCenterService
                .findLiteModelByGerencia(gerenciaModel.getGerenciaId())));

        if (!reportesCallCenterLiteModel.isEmpty()) {
            reportesCallCenterDTO = new ReporteCallCenterMapper().mapOuts(reportesCallCenterLiteModel);
            httpStatus = HttpStatus.OK;
        }

        return new ResponseEntity<>(reportesCallCenterDTO, httpStatus);
    }

    @GetMapping("/reportes/prestamo_id/{id}")
    public ResponseEntity<ReporteCallCenterDTO> getReporteById(@PathVariable String id) {
        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
        ReporteCallCenterDTO reporteCallCenterDTO = new ReporteCallCenterDTO();

        ReporteCallCenterLiteModel reporteCallCenterLiteModel = this.reporteCallCenterService
                .findLiteModelById(id);
        if (reporteCallCenterLiteModel != null) {
            reporteCallCenterDTO = new ReporteCallCenterMapper().mapOut(reporteCallCenterLiteModel);
            httpStatus = HttpStatus.OK;
        }

        return new ResponseEntity<>(reporteCallCenterDTO, httpStatus);
    }

    @PostMapping("/visitas/create-one")
    public ResponseEntity<String> postVisitaCreateOne(@RequestBody HashMap<String, Object> visita) {
        CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());
        PrestamoModel prestamoModel = this.prestamoService.findById(visita.get("prestamoId").toString());

        VisitaModel save = null;

        if (prestamoModel != null) {
            VisitaModel visitaModel = new VisitaModel();
            visitaModel.setLat(Double.parseDouble(visita.get("lat").toString()));
            visitaModel.setLng(Double.parseDouble(visita.get("lng").toString()));
            visitaModel.setPrestamoId(visita.get("prestamoId").toString());
            visitaModel.setAnio(calendarioModel.getAnio());
            visitaModel.setSemana(calendarioModel.getSemana());
            visitaModel.setAgente(prestamoModel.getAgente());
            visitaModel.setLog(new Gson().toJson(visita.get("log")));
            visitaModel.setCliente(prestamoModel.getNombres() + " " + prestamoModel.getApellidoPaterno() + " "
                    + prestamoModel.getApellidoMaterno());
            visitaModel.setVisitaId(UUID.randomUUID().toString());
            visitaModel.setFecha(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            save = this.visitaService.save(visitaModel);
        }

        return new ResponseEntity<>(save != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }
}
