package tech.calaverita.reporterloanssql.models.mariaDB.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "prestamos_util")
public class PrestamoUtilModel {
    @Id
    private String prestamoId;
    private Double tarifa;
    private Double saldo;
    private Double cobrado;
    private String diaDePago;
    private String gerencia;
}
