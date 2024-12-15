package tech.calaverita.sfast_xpress.controllers.PGS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.DTOs.TablaFlujoEfectivoDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.CierreSemanalDTO;
import tech.calaverita.sfast_xpress.DTOs.dashboard.DashboardDTO;
import tech.calaverita.sfast_xpress.controllers.XpressController;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.BalanceAgenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.EgresosAgenteModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.EgresosGerenteModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.IngresosAgenteModel;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.cierre_semanal.BalanceAgenciaService;
import tech.calaverita.sfast_xpress.services.cierre_semanal.CierreSemanalService;
import tech.calaverita.sfast_xpress.services.cierre_semanal.EgresosAgenteService;
import tech.calaverita.sfast_xpress.services.cierre_semanal.EgresosGerenteService;
import tech.calaverita.sfast_xpress.services.cierre_semanal.IngresosAgenteService;
import tech.calaverita.sfast_xpress.utils.CierreSemanalUtil;
import tech.calaverita.sfast_xpress.utils.DesgloceCobranzaYComisionesUtil;
import tech.calaverita.sfast_xpress.utils.LogUtil;

@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa/cierres_semanales")
public final class CierreSemanalController {
    private final AsignacionService asignacionService;
    private final UsuarioService usuarioService;
    private final XpressController xpressController;
    private final BalanceAgenciaService balanceAgenciaService;
    private final CierreSemanalService cierreSemanalService;
    private final EgresosAgenteService egresosAgenteService;
    private final EgresosGerenteService egresosGerenteService;
    private final IngresosAgenteService ingresosAgenteService;

    public CierreSemanalController(AsignacionService asignacionService, UsuarioService usuarioService,
            XpressController xpressController, BalanceAgenciaService balanceAgenciaService,
            CierreSemanalService cierreSemanalService, EgresosAgenteService egresosAgenteService,
            EgresosGerenteService egresosGerenteService,
            IngresosAgenteService ingresosAgenteService) {
        this.asignacionService = asignacionService;
        this.usuarioService = usuarioService;
        this.xpressController = xpressController;
        this.balanceAgenciaService = balanceAgenciaService;
        this.cierreSemanalService = cierreSemanalService;
        this.egresosAgenteService = egresosAgenteService;
        this.egresosGerenteService = egresosGerenteService;
        this.ingresosAgenteService = ingresosAgenteService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @GetMapping(path = "/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<?> getByAgenciaAnioAndSemana(@RequestHeader String staticToken,
            @RequestHeader String username,
            @PathVariable String agencia,
            @PathVariable int anio, @PathVariable int semana)
            throws ExecutionException, InterruptedException {
        CierreSemanalDTO cierreSemanalDTO = null;
        HttpStatus responseStatus = HttpStatus.OK;

        DashboardDTO dashboard;
        List<UsuarioModel> usuarioModels;
        double asignaciones;

        Optional<CierreSemanalModel> cierreSemanalEntity = this.cierreSemanalService
                .findByAgenciaAnioAndSemana(agencia, anio, semana);

        if (staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")) {
            if (!this.usuarioService.existsByUsuario(username)) {
                responseStatus = HttpStatus.FORBIDDEN;
            } else if (!this.usuarioService.existsByUsuarioAndStatus(username, true)) {
                responseStatus = HttpStatus.UNAUTHORIZED;
            } else {
                if (cierreSemanalEntity.isPresent()) {
                    cierreSemanalDTO = CierreSemanalUtil.getCierreSemanalDTO(cierreSemanalEntity.get());
                } else if (this.usuarioService.existsByAgencia(agencia)) {
                    dashboard = xpressController.getDashboardByAgenciaAnioAndSemana(agencia, anio, semana).getBody();
                    usuarioModels = new ArrayList<>();
                    usuarioModels.add(this.usuarioService.findByAgenciaAndStatus(agencia, true));
                    usuarioModels.add(this.usuarioService.findByGerenciaAndTipo(usuarioModels.get(0).getGerencia(),
                            "Gerente"));
                    asignaciones = this.asignacionService
                            .findSumaAsigancionesByAgenciaAnioAndSemana(agencia, anio, semana).join();

                    cierreSemanalDTO = CierreSemanalUtil.getCierreSemanalDTO(dashboard, usuarioModels, asignaciones);
                } else {
                    return new ResponseEntity<>("La agencia no existe", HttpStatus.BAD_REQUEST);
                }
            }
        } else {
            responseStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(cierreSemanalDTO, responseStatus);
    }

    @PostMapping(path = "/create-one")
    public @ResponseBody ResponseEntity<String> createOne(@RequestBody CierreSemanalDTO cierreSemanalDTO,
            @RequestHeader String staticToken,
            @RequestHeader String username)
            throws DocumentException, FileNotFoundException {
        String responseText = "";
        HttpStatus responseStatus;
        CierreSemanalModel cierreSemanalModel = this.cierreSemanalService.getCierreSemanalEntity(cierreSemanalDTO);

        // To easy code
        String urlPDF = "https://sfast-api.terio.xyz/xpress/v1/pwa/cierres_semanales/pdf/"
                + cierreSemanalModel.getId() + ".pdf";

        cierreSemanalDTO.setPDF(urlPDF);
        cierreSemanalModel.setPDF(urlPDF);
        cierreSemanalModel.setLog(LogUtil.getLogCierreSemanal(cierreSemanalDTO.getBalanceAgencia()));

        if (staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")) {
            if (!this.usuarioService.existsByUsuarioAndTipo(username, "Gerente")) {
                responseStatus = HttpStatus.FORBIDDEN;
            } else if (!this.usuarioService.existsByUsuarioTipoAndStatus(username, "Gerente", true)) {
                responseStatus = HttpStatus.UNAUTHORIZED;
            } else {
                if (this.cierreSemanalService.findById(cierreSemanalModel.getId()).isEmpty()) {
                    BalanceAgenciaModel balanceAgenciaModel = this.balanceAgenciaService
                            .getBalanceAgenciaEntity(cierreSemanalDTO.getBalanceAgencia());
                    balanceAgenciaModel.setId(cierreSemanalModel.getBalanceAgenciaId());
                    this.balanceAgenciaService.save(balanceAgenciaModel);

                    EgresosAgenteModel egresosAgenteModel = this.egresosAgenteService
                            .getEgresosGerenteEntity(cierreSemanalDTO.getEgresosAgente());
                    egresosAgenteModel.setId(cierreSemanalModel.getEgresosAgenteId());
                    this.egresosAgenteService.save(egresosAgenteModel);

                    EgresosGerenteModel egresosGerenteModel = this.egresosGerenteService
                            .getEgresosGerenteEntity(cierreSemanalDTO.getEgresosGerente());
                    egresosGerenteModel.setId(cierreSemanalModel.getEgresosGerenteId());
                    this.egresosGerenteService.save(egresosGerenteModel);

                    IngresosAgenteModel ingresosAgenteModel = this.ingresosAgenteService
                            .getIngresosAgenteEntity(cierreSemanalDTO.getIngresosAgente());
                    ingresosAgenteModel.setId(cierreSemanalModel.getIngresosAgenteId());
                    this.ingresosAgenteService.save(ingresosAgenteModel);

                    this.cierreSemanalService.save(cierreSemanalModel);

                    responseText = urlPDF;
                    responseStatus = HttpStatus.CREATED;
                } else {
                    responseText = "No se pudo registrar el cierre semanal porque ya existe";
                    responseStatus = HttpStatus.CONFLICT;
                }
            }
        } else {
            responseStatus = HttpStatus.BAD_REQUEST;
        }

        CierreSemanalUtil.subSendCierreSemanalMessage(cierreSemanalDTO);
        // CierreSemanalUtil.createCierreSemanalPDF(cierreSemanalDTO);

        return new ResponseEntity<>(responseText, responseStatus);
    }

    @GetMapping(path = "/pdf/{file}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody ResponseEntity<InputStreamResource> getPdfByRuta(@PathVariable String file)
            throws IOException {
        FileInputStream fileInputStream = new FileInputStream(Constants.RUTA_PDF_PRODUCCION + file);

        return new ResponseEntity<>(new InputStreamResource(fileInputStream), HttpStatus.OK);
    }

    @PostMapping(path = "/desgloce_cobranza_y_comisiones/pdf/create-one")
    public @ResponseBody ResponseEntity<String> desgloceCobranzaYComisionesCreateOne(
            @RequestBody TablaFlujoEfectivoDTO tablaFlujoEfectivoDTO)
            throws DocumentException, FileNotFoundException {
        String idPDF = "CIERRE_" + tablaFlujoEfectivoDTO.getZona() + "_SEM" + tablaFlujoEfectivoDTO.getSemana() + "_"
                + tablaFlujoEfectivoDTO.getAnio();
        String urlPDF = "https://sfast-api.terio.xyz/xpress/v1/pwa/cierres_semanales/desgloce_cobranza_y_comisiones/pdf/"
                + idPDF + ".pdf";

        DesgloceCobranzaYComisionesUtil.createCierreSemanalPDF(tablaFlujoEfectivoDTO, idPDF);

        return new ResponseEntity<>(urlPDF, HttpStatus.OK);
    }

    @GetMapping(path = "/desgloce_cobranza_y_comisiones/pdf/{file}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody ResponseEntity<InputStreamResource> getPdfDesgloceCobranzaYComisionesByRuta(
            @PathVariable String file)
            throws IOException {
        FileInputStream fileInputStream = new FileInputStream(Constants.RUTA_PDF_PRODUCCION + file);

        return new ResponseEntity<>(new InputStreamResource(fileInputStream), HttpStatus.OK);
    }

    @GetMapping(path = "/{agencia}/{anio}/{semana}/{nivel}")
    public @ResponseBody ResponseEntity<ComisionCobranza> getComisionCobranzaByAgenciaAnioSemanaAndNivel(
            @PathVariable String agencia, @PathVariable int anio, @PathVariable int semana, @PathVariable String nivel)
            throws ExecutionException, InterruptedException {
        HashMap<String, Integer> porcentajesComision = new HashMap<>();
        porcentajesComision.put("SILVER", 7);
        porcentajesComision.put("GOLD", 8);
        porcentajesComision.put("PLATINUM", 9);
        porcentajesComision.put("DIAMOND", 10);

        int porcentajeComision = porcentajesComision.get(nivel);

        double cobranzaTotal = CierreSemanalUtil.getCobranzaTotalByAgenciaAnioAndSemana(agencia, anio, semana);

        ComisionCobranza comisionCobranza = new ComisionCobranza(porcentajeComision,
                cobranzaTotal / 100 * porcentajeComision);

        return new ResponseEntity<>(comisionCobranza, HttpStatus.OK);
    }

    @Data
    public static class ComisionCobranza {
        private Integer porcentajeComisionCobranza;
        private Double pagoComisionCobranza;

        public ComisionCobranza(Integer porcentajeComisionCobranza, Double pagoComisionCobranza) {
            this.porcentajeComisionCobranza = porcentajeComisionCobranza;
            this.pagoComisionCobranza = pagoComisionCobranza;
        }
    }
}
