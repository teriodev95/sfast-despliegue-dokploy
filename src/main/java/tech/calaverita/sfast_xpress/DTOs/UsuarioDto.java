package tech.calaverita.sfast_xpress.DTOs;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.GerenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;

@Data
public class UsuarioDto {
    private Integer usuarioId;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipo;
    private Integer pin;
    private String usuario;
    private Boolean puedeVerificarAsignaciones;
    private Boolean puedeCobrar;
    private Boolean status;
    private String gerencia;
    private String agencia;
    private String fechaIngreso;
    private String telegramId;
    private String numeroCelular;
    private String createdAt;
    private String updatedAt;
    private List<GerenciaModel> gerenciasAsignadas;

    public UsuarioDto() {

    }

    public UsuarioDto(UsuarioModel usuarioModel, ArrayList<GerenciaModel> gerenciasAsignadas) {
        this.usuarioId = usuarioModel.getUsuarioId();
        this.nombre = usuarioModel.getNombre();
        this.apellidoPaterno = usuarioModel.getApellidoPaterno();
        this.apellidoMaterno = usuarioModel.getApellidoMaterno();
        this.tipo = usuarioModel.getTipo();
        this.pin = usuarioModel.getPin();
        this.usuario = usuarioModel.getUsuario();
        this.puedeVerificarAsignaciones = usuarioModel.getPuedeVerificarAsignaciones();
        this.puedeCobrar = usuarioModel.getPuedeCobrar();
        this.status = usuarioModel.getStatus();
        this.gerencia = usuarioModel.getGerencia();
        this.agencia = usuarioModel.getAgencia();
        this.fechaIngreso = usuarioModel.getFechaIngreso();
        this.telegramId = usuarioModel.getTelegramId();
        this.numeroCelular = usuarioModel.getNumeroCelular();
        this.createdAt = usuarioModel.getCreatedAt();
        this.updatedAt = usuarioModel.getUpdatedAt();
        this.gerenciasAsignadas = gerenciasAsignadas;
    }
}