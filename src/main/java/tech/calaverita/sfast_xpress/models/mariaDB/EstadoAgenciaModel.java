package tech.calaverita.sfast_xpress.models.mariaDB;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "estado_agencias")
public class EstadoAgenciaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String agenciaId;
    private String gerenciaId;
    private String sucursal;
    private String agenteAsignado;
    private String status;
}
