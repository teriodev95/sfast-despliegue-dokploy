package tech.calaverita.sfast_xpress.v2.modules.asignaciones;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;;

@Entity
@Table(name = "asignaciones_v2")
@Data
public class AsignacionV2Model {
    @Id
    @Column(length = 64)
    private String id;

    @Column(precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(length = 64)
    private String agencia;

    @Column(name = "gerencia_entrega", length = 64)
    private String gerenciaEntrega;

    @Column(name = "gerencia_recibe", length = 64)
    private String gerenciaRecibe;

    private Integer semana;
    private Integer anio;

    @Column(length = 16)
    private String tipo;

    @Column(name = "cubre_vacante")
    private Boolean cubreVacante;

    @Column(name = "quien_entrego")
    private Integer quienEntrego;

    @Column(name = "quien_recibio")
    private Integer quienRecibio;

    @Column(columnDefinition = "longtext")
    private String log;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "quien_entrego", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_asignaciones_entrego"))
    private UsuarioModel usuarioEntrego;

    @ManyToOne
    @JoinColumn(name = "quien_recibio", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_asignaciones_recibio"))
    private UsuarioModel usuarioRecibio;
} 