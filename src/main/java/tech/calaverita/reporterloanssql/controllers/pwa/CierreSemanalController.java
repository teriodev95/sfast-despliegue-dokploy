package tech.calaverita.reporterloanssql.controllers.pwa;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.controllers.XpressController;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.CierreSemanalDTO;
import tech.calaverita.reporterloanssql.persistence.entities.UsuarioEntity;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.*;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.services.AsignacionService;
import tech.calaverita.reporterloanssql.services.UsuarioService;
import tech.calaverita.reporterloanssql.services.cierre_semanal.*;
import tech.calaverita.reporterloanssql.utils.CierreSemanalUtil;

import java.util.ArrayList;
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
    private final AsignacionService asignServ;
    private final UsuarioService usuarServ;
    private final XpressController xprContr;
    private final BalanceAgenciaService balAgencServ;
    private final CierreSemanalService cierSemServ;
    private final EgresosAgenteService egrAgentServ;
    private final EgresosGerenteService egrGerServ;
    private final IngresosAgenteService ingrAgentServ;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private CierreSemanalController(
            AsignacionService asignServ_S,
            UsuarioService usuarServ_S,
            XpressController xprContr_S,
            BalanceAgenciaService balAgencServ_S,
            CierreSemanalService cierSemServ_S,
            EgresosAgenteService egrAgentServ_S,
            EgresosGerenteService egrGerServ_S,
            IngresosAgenteService ingrAgentServ_S
    ) {
        this.asignServ = asignServ_S;
        this.usuarServ = usuarServ_S;
        this.xprContr = xprContr_S;
        this.balAgencServ = balAgencServ_S;
        this.cierSemServ = cierSemServ_S;
        this.egrAgentServ = egrAgentServ_S;
        this.egrGerServ = egrGerServ_S;
        this.ingrAgentServ = ingrAgentServ_S;
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
        List<UsuarioEntity> usuarioModels;
        double asignaciones;

        Optional<CierreSemanalEntity> cierreSemanalEntity = this.cierSemServ.findByAgenciaAnioAndSemana(agencia, anio,
                semana);

        if (
                staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")
        ) {
            if (
                    !this.usuarServ.existsByUsuarioGerente(username)
            ) {
                responseStatus = HttpStatus.FORBIDDEN;
            } //
            else if (
                    !this.usuarServ.existsByUsuarioGerenteActivo(username)
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
                        this.usuarServ.findByUsuario(agencia).isPresent()
                ) {
                    dashboard = xprContr.getDashboardByAgenciaAnioAndSemana(agencia, anio, semana).getBody();
                    usuarioModels = new ArrayList<>();
                    usuarioModels.add(this.usuarServ.usuarModFindByUsuario(agencia));
                    usuarioModels.add(this.usuarServ.usuarModFindByUsuario(usuarioModels.get(0).getGerencia()));
                    asignaciones = this.asignServ.getSumaDeAsigancionesByAgenciaAnioAndSemana(agencia, anio, semana);

                    System.out.println(dashboard.toString());

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
    public @ResponseBody ResponseEntity<String> restrCreateCierreSemanal(
            @RequestBody CierreSemanalDTO cierreSemanalDTO_I,
            @RequestHeader(name = "staticToken") String staticToken,
            @RequestHeader(name = "username") String username
    ) {
        String responseText = "";
        HttpStatus responseStatus;
        CierreSemanalEntity cierreSemanalEntity = this.cierSemServ.getCierreSemanalEntity(cierreSemanalDTO_I);

        if (
                staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")
        ) {
            if (
                    !this.usuarServ.existsByUsuarioGerente(username)
            ) {
                responseStatus = HttpStatus.FORBIDDEN;
            } //
            else if (
                    !this.usuarServ.existsByUsuarioGerenteActivo(username)
            ) {
                responseStatus = HttpStatus.UNAUTHORIZED;
            } //
            else {
                if (
                        this.cierSemServ.findById(cierreSemanalEntity.getId()).isEmpty()
                ) {
                    BalanceAgenciaEntity balanceAgenciaEntity = this.balAgencServ.getBalanceAgenciaEntity(cierreSemanalDTO_I
                            .getBalanceAgencia());
                    balanceAgenciaEntity.setId(cierreSemanalEntity.getBalanceAgenciaId());
                    this.balAgencServ.save(balanceAgenciaEntity);

                    EgresosAgenteEntity egresosAgenteEntity = this.egrAgentServ.getEgresosGerenteEntity(cierreSemanalDTO_I
                            .getEgresosAgente());
                    egresosAgenteEntity.setId(cierreSemanalEntity.getEgresosAgenteId());
                    this.egrAgentServ.save(egresosAgenteEntity);

                    EgresosGerenteEntity egresosGerenteEntity = this.egrGerServ.getEgresosGerenteEntity(cierreSemanalDTO_I
                            .getEgresosGerente());
                    egresosGerenteEntity.setId(cierreSemanalEntity.getEgresosGerenteId());
                    this.egrGerServ.save(egresosGerenteEntity);

                    IngresosAgenteEntity ingresosAgenteEntity = this.ingrAgentServ.getIngresosAgenteEntity(cierreSemanalDTO_I
                            .getIngresosAgente());
                    ingresosAgenteEntity.setId(cierreSemanalEntity.getIngresosAgenteId());
                    this.ingrAgentServ.save(ingresosAgenteEntity);

                    this.cierSemServ.save(cierreSemanalEntity);

                    responseText = "Cierre semanal registrado con éxito";
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

        return new ResponseEntity<>(responseText, responseStatus);
    }
}
