package tech.calaverita.sfast_xpress.models.mariaDB;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "calendario")
public class CalendarioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer semana;
    private Integer anio;
    private String mes;
    private String desde;
    private String hasta;
    private boolean pagoBono;

    public CalendarioModel() {
        this.semana = 0;
        this.anio = 0;
        this.mes = "";
        this.desde = "";
        this.hasta = "";
        this.pagoBono = false;
    }

    public CalendarioModel(CalendarioModel calendarioModel) {
        this();
        this.semana = calendarioModel.getSemana();
        this.anio = calendarioModel.getAnio();
        this.mes = calendarioModel.getMes();
        this.desde = calendarioModel.getDesde();
        this.hasta = calendarioModel.getHasta();
        this.pagoBono = calendarioModel.isPagoBono();
    }
}
