package tech.calaverita.sfast_xpress.models.mariaDB;

import org.hibernate.sql.ast.tree.predicate.BooleanExpressionPredicate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "gastos")
public class GastoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gastoId;
    private Integer creadoPorId;
    private String tipoGasto;
    private String fecha;
    private Integer semana;
    private Integer anio;
    private Double monto;
    private Double litros;
    private String concepto;
    private String urlRecibo;
    private Boolean reembolsado;
    private String createdAt;
    private String updatedAt;
}
