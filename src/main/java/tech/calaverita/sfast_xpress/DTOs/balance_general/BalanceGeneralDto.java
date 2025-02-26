package tech.calaverita.sfast_xpress.DTOs.balance_general;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.pojos.AlmacenObjects;

@Data
public class BalanceGeneralDto {
    private Integer semana;
    private Integer anio;
    private String tituloReporte;
    private String sucursal;
    private String gerente;
    private String zona;
    private LocalDateTime curdate;
    private List<BalanceGeneralAgenciaDto> agencias;

    public BalanceGeneralDto() {
        this.semana = 0;
        this.anio = 0;
        this.tituloReporte = "BALANCE GENERAL DE CIERRE SEMANAL";
        this.sucursal = "";
        this.gerente = "";
        this.zona = "";
        this.curdate = LocalDateTime.now();
    }

    public BalanceGeneralDto(AlmacenObjects almacenObjects, List<BalanceGeneralAgenciaDto> agenciasDtos) {
        this();
        this.semana = (Integer) almacenObjects.getObject("semana");
        this.anio = (Integer) almacenObjects.getObject("anio");
        this.sucursal = (String) almacenObjects.getObject("sucursal");
        this.gerente = (String) almacenObjects.getObject("gerente");
        this.zona = (String) almacenObjects.getObject("zona");
        this.agencias = agenciasDtos;
    }
}
