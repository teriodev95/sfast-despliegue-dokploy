package tech.calaverita.sfast_xpress.controllers.PGS;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tech.calaverita.sfast_xpress.models.GastoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.services.GastoService;
import tech.calaverita.sfast_xpress.services.UsuarioService;

@CrossOrigin
@RestController
@RequestMapping(path = "/xpress/v1/pwa/gastos")
public class GastoController {
    private GastoService gastoService;
    private UsuarioService usuarioService;

    public GastoController(GastoService gastoService, UsuarioService usuarioService) {
        this.gastoService = gastoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping(path = "/by_usuario_id/{usuarioId}")
    public @ResponseBody ResponseEntity<ArrayList<GastoModel>> getByAgencia(@PathVariable Integer usuarioId) {
        HttpStatus httpStatus = HttpStatus.OK;

        ArrayList<GastoModel> gastoModels = this.gastoService.findByCreadoPorId(usuarioId);

        if (gastoModels.isEmpty()) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(gastoModels, httpStatus);
    }

    @GetMapping(path = "/by_agencia/{agencia}")
    public @ResponseBody ResponseEntity<ArrayList<GastoModel>> getByAgencia(@PathVariable String agencia) {
        UsuarioModel usuarioModel = this.usuarioService.findByAgenciaAndStatus(agencia, true);
        ArrayList<GastoModel> gastoModels = this.gastoService.findByCreadoPorId(usuarioModel.getUsuarioId());

        return new ResponseEntity<>(gastoModels, HttpStatus.OK);
    }

    @GetMapping(path = "/by_gerencia/{gerencia}")
    public @ResponseBody ResponseEntity<ArrayList<GastoModel>> getByGerencia(@PathVariable String gerencia) {
        ArrayList<UsuarioModel> usuarioModels = this.usuarioService.findByGerenciaAndStatus(gerencia, true);

        int[] idsUsuario = usuarioModels.stream()
                .mapToInt(UsuarioModel::getUsuarioId)
                .toArray();

        ArrayList<GastoModel> gastoModels = this.gastoService.findByCreadoPorIdIn(idsUsuario);

        return new ResponseEntity<>(gastoModels, HttpStatus.OK);
    }

    @PostMapping(path = "/create")
    public @ResponseBody ResponseEntity<GastoModel> create(@RequestBody GastoModel gastoModel) {
        HttpStatus responseHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        GastoModel responseGastoModel = this.gastoService.save(gastoModel);

        if (responseGastoModel != null) {
            responseHttpStatus = HttpStatus.CREATED;
        }

        return new ResponseEntity<>(responseGastoModel, responseHttpStatus);
    }

    @PostMapping(path = "/createAll")
    public @ResponseBody ResponseEntity<ArrayList<GastoModel>> createAll(
            @RequestBody ArrayList<GastoModel> gastoModels) {
        HttpStatus responseHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        ArrayList<GastoModel> responseGastoModels = this.gastoService.saveAll(gastoModels);

        if (!responseGastoModels.isEmpty()) {
            responseHttpStatus = HttpStatus.CREATED;
        }

        return new ResponseEntity<>(responseGastoModels, responseHttpStatus);
    }
}
