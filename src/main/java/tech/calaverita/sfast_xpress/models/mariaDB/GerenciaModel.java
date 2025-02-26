package tech.calaverita.sfast_xpress.models.mariaDB;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "gerencias")
public class GerenciaModel {
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
    private String deprecatedName;
    private String sucursal;
    @JsonIgnore
    @OneToMany(mappedBy = "gerenciaModel", fetch = FetchType.LAZY)
    private List<UsuarioModel> usuarioModels;
}
