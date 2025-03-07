package tech.calaverita.sfast_xpress.models.mariaDB;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class UsuarioModel {
    @Id
    @Column(name = "usuarioid")
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
    // @OneToMany(mappedBy = "recibioUsuarioModel")
    // private List<AsignacionModel> recibioAsignacionModels;
    // @OneToMany(mappedBy = "entregoUsuarioModel")
    // private List<AsignacionModel> entregoAsignacionModels;
    // @OneToMany(mappedBy = "usuarioModel")
    // private List<GastoModel> gastoModels;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gerencia", insertable = false, updatable = false)
    private GerenciaModel gerenciaModel;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "agencia", insertable = false, updatable = false)
    private EstadoAgenciaModel estadoAgenciaModel;

    public static UsuarioModel getSinAgenteAsignado() {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setUsuarioId(0);
        usuarioModel.setNombre("Sin");
        usuarioModel.setApellidoPaterno("Agente");
        usuarioModel.setApellidoMaterno("Asignado");
        usuarioModel.setTipo("Agente");
        usuarioModel.setPin(1111);
        usuarioModel.setUsuario("SinAgenteAsignado");
        usuarioModel.setPuedeVerificarAsignaciones(true);
        usuarioModel.setPuedeCobrar(true);
        usuarioModel.setStatus(true);
        usuarioModel.setGerencia("SinGerencia");
        usuarioModel.setAgencia("SinAgencia");
        usuarioModel.setFechaIngreso("0000-00-00");
        usuarioModel.setTelegramId("0");
        usuarioModel.setNumeroCelular("0000000000");

        return usuarioModel;
    }

    public static UsuarioModel getSinGerenteAsignado() {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setUsuarioId(0);
        usuarioModel.setNombre("Sin");
        usuarioModel.setApellidoPaterno("Gerente");
        usuarioModel.setApellidoMaterno("Asignado");
        usuarioModel.setTipo("Gerente");
        usuarioModel.setPin(1111);
        usuarioModel.setUsuario("SinGerenteAsignado");
        usuarioModel.setPuedeVerificarAsignaciones(true);
        usuarioModel.setPuedeCobrar(false);
        usuarioModel.setStatus(true);
        usuarioModel.setGerencia("SinGerencia");
        usuarioModel.setAgencia("SinAgencia");
        usuarioModel.setFechaIngreso("0000-00-00");
        usuarioModel.setTelegramId("0");
        usuarioModel.setNumeroCelular("0000000000");

        return usuarioModel;
    }
}
