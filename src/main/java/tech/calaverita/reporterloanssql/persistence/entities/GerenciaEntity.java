package tech.calaverita.reporterloanssql.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "gerencias")
public class GerenciaEntity {
    @Id
    @Column(name = "gerenciaid")
    private String gerenciaId;
    private String status;
    @Column(name = "chatidpagos")
    private String chatIdPagos;
    @Column(name = "chatidnumeros")
    private String chatIdNumeros;
    @Column(name = "seguridadid")
    private Integer seguridadId;
    @Column(name = "sucursalid")
    private String sucursalId;
}
