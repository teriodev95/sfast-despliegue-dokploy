package tech.calaverita.sfast_xpress.models.mariaDB;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "agencias")
public class AgenciaModel {
    @Id
    @Column(name = "agenciaid")
    private String id;
    private String status;
    @Column(name = "gerenciaid")
    private String gerenciaId;
}
