package tech.calaverita.sfast_xpress.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ResumenYBalanceModel {
    @Id
    private String concepto;
    private Double monto;
}
