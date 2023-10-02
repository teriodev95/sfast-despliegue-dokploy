package tech.calaverita.reporterloanssql.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
}
