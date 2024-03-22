package tech.calaverita.reporterloanssql.controllers.PGS;

import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.Constants;
import tech.calaverita.reporterloanssql.controllers.XpressController;
import tech.calaverita.reporterloanssql.dto.cierre_semanal.CierreSemanalDTO;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;
import tech.calaverita.reporterloanssql.models.mariaDB.cierre_semanal.*;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.services.AsignacionService;
import tech.calaverita.reporterloanssql.services.UsuarioService;
import tech.calaverita.reporterloanssql.services.cierre_semanal.*;
import tech.calaverita.reporterloanssql.utils.CierreSemanalUtil;
import tech.calaverita.reporterloanssql.utils.LogUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa/cierres_semanales")
public final class CierreSemanalController {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final AsignacionService asignacionService;
    private final UsuarioService usuarioService;
    private final XpressController xpressController;
    private final BalanceAgenciaService balanceAgenciaService;
    private final CierreSemanalService cierreSemanalService;
    private final EgresosAgenteService egresosAgenteService;
    private final EgresosGerenteService egresosGerenteService;
    private final IngresosAgenteService ingresosAgenteService;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private CierreSemanalController(
            AsignacionService asignacionService,
            UsuarioService usuarioService,
            XpressController xpressController,
            BalanceAgenciaService balanceAgenciaService,
            CierreSemanalService cierreSemanalService,
            EgresosAgenteService egresosAgenteService,
            EgresosGerenteService egresosGerenteService,
            IngresosAgenteService ingresosAgenteService
    ) {
        this.asignacionService = asignacionService;
        this.usuarioService = usuarioService;
        this.xpressController = xpressController;
        this.balanceAgenciaService = balanceAgenciaService;
        this.cierreSemanalService = cierreSemanalService;
        this.egresosAgenteService = egresosAgenteService;
        this.egresosGerenteService = egresosGerenteService;
        this.ingresosAgenteService = ingresosAgenteService;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(path = "/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<?> getCierreSemanalByAgenciaAnioAndSemana(
            @RequestHeader(name = "staticToken") String staticToken,
            @RequestHeader(name = "username") String username,
            @PathVariable("agencia") String agencia,
            @PathVariable("anio") int anio,
            @PathVariable("semana") int semana,
            HttpServletRequest request
    ) throws ExecutionException, InterruptedException {
        CierreSemanalDTO cierreSemanalDTO = null;
        HttpStatus responseStatus = HttpStatus.OK;

        request.getHeader("X-FORWARDED-FOR");

        Dashboard dashboard;
        List<UsuarioModel> usuarioModels;
        double asignaciones;

        Optional<CierreSemanalModel> cierreSemanalEntity = this.cierreSemanalService.findByAgenciaAnioAndSemana(agencia, anio,
                semana);

        if (
                staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")
        ) {
            if (
                    !this.usuarioService.existsByUsuario(username)
            ) {
                responseStatus = HttpStatus.FORBIDDEN;
            } //
            else if (
                    !this.usuarioService.existsByUsuarioActivo(username)
            ) {
                responseStatus = HttpStatus.UNAUTHORIZED;
            } //
            else {
                if (
                        cierreSemanalEntity.isPresent()
                ) {
                    cierreSemanalDTO = CierreSemanalUtil.getCierreSemanalDTO(cierreSemanalEntity.get());
                } //
                else if (
                        this.usuarioService.findByAgencia(agencia).isPresent()
                ) {
                    dashboard = xpressController.getDashboardByAgenciaAnioAndSemana(agencia, anio, semana).getBody();
                    usuarioModels = new ArrayList<>();
                    usuarioModels.add(this.usuarioService.findByAgencia(agencia).get());
                    usuarioModels.add(this.usuarioService.findGerenteByGerencia(usuarioModels.get(0).getGerencia()));
                    asignaciones = this.asignacionService.getSumaDeAsigancionesByAgenciaAnioAndSemana(agencia, anio, semana);

                    cierreSemanalDTO = CierreSemanalUtil.getCierreSemanalDTO(dashboard, usuarioModels, asignaciones);
                } //
                else {
                    return new ResponseEntity<>("La agencia no existe", HttpStatus.BAD_REQUEST);
                }
            }
        } //
        else {
            responseStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(cierreSemanalDTO, responseStatus);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @PostMapping(path = "/create-one")
    public @ResponseBody ResponseEntity<String> createCierreSemanal(
            @RequestBody CierreSemanalDTO cierreSemanalDTO,
            @RequestHeader(name = "staticToken") String staticToken,
            @RequestHeader(name = "username") String username
    ) throws DocumentException, FileNotFoundException {
        String responseText = "";
        HttpStatus responseStatus;
        CierreSemanalModel cierreSemanalModel = this.cierreSemanalService.getCierreSemanalEntity(cierreSemanalDTO);

        // To easy code
        String urlPDF = "https://sfast-api.terio.xyz/xpress/v1/pwa/cierres_semanales/pdf/"
                + cierreSemanalModel.getId() + ".pdf";

        cierreSemanalDTO.setPDF(urlPDF);
        cierreSemanalModel.setPDF(urlPDF);
        cierreSemanalModel.setLog(LogUtil.getLogCierreSemanal(cierreSemanalDTO.getBalanceAgencia()));

        if (
                staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")
        ) {
            if (
                    !this.usuarioService.existsByUsuarioGerente(username)
            ) {
                responseStatus = HttpStatus.FORBIDDEN;
            } //
            else if (
                    !this.usuarioService.existsByUsuarioGerenteActivo(username)
            ) {
                responseStatus = HttpStatus.UNAUTHORIZED;
            } //
            else {
                if (
                        this.cierreSemanalService.findById(cierreSemanalModel.getId()).isEmpty()
                ) {
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
                } //
                else {
                    responseText = "No se pudo registrar el cierre semanal porque ya existe";
                    responseStatus = HttpStatus.CONFLICT;
                }
            }
        } //
        else {
            responseStatus = HttpStatus.BAD_REQUEST;
        }

        CierreSemanalUtil.subSendCierreSemanalMessage(cierreSemanalDTO);
        CierreSemanalUtil.createCierreSemanalPDF(cierreSemanalDTO);

        return new ResponseEntity<>(responseText, responseStatus);
    }

    @GetMapping(path = "/pdf/{file}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody ResponseEntity<InputStreamResource> getPdf2(
            @PathVariable("file") String file
    ) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(Constants.RUTA_PDF_PRODUCCION + file);

        return new ResponseEntity<>(new InputStreamResource(fileInputStream), HttpStatus.OK);
    }

    @GetMapping(path = "/{agencia}/{anio}/{semana}/{nivel}")
    public @ResponseBody ResponseEntity<ComisionCobranza> getComisionCobranzaByAgenciaAnioSemanaAndNivel(
            @PathVariable("agencia") String agencia,
            @PathVariable("anio") int anio,
            @PathVariable("semana") int semana,
            @PathVariable("nivel") String nivel
    ) throws ExecutionException, InterruptedException {
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

        public ComisionCobranza(
                Integer porcentajeComisionCobranza,
                Double pagoComisionCobranza
        ) {
            this.porcentajeComisionCobranza = porcentajeComisionCobranza;
            this.pagoComisionCobranza = pagoComisionCobranza;
        }
    }
}
