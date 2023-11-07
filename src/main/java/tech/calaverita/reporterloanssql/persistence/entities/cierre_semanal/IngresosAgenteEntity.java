package tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ingresos_agente")
public class IngresosAgenteEntity {
    @Id
    String id;
    Double cobranzaPura;
    Double montoExcedente;
    Double liquidaciones;
    Double multas;
    Double otros;
    Double total;
}
