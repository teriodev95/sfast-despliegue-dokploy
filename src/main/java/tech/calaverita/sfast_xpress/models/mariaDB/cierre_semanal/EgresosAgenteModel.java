package tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "egresos_agentes")
public class EgresosAgenteModel {
    @Id
    private String id;
    private Double asignaciones;
    private Double otros;
    private String motivoOtros;
    private Double efectivoEntregadoCierre;
    private Double total;
}
