package tech.calaverita.sfast_xpress.DTOs.detalle_cierre_agencias;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.pojos.AlmacenObjects;

@Data
public class DetalleCierreAgenciasDto {
    private Integer semana;
    private Integer anio;
    private String tituloReporte;
    private String sucursal;
    private String gerente;
    private String zona;
    private String curdate;
    private List<DetalleCierreAgenciasAgenciaDto> agencias;

    public DetalleCierreAgenciasDto() {
        this.semana = 0;
        this.anio = 0;
        this.tituloReporte = "BALANCE GENERAL DE CIERRE ";
        this.sucursal = "";
        this.gerente = "";
        this.zona = "";
        this.curdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public DetalleCierreAgenciasDto(AlmacenObjects almacenObjects, List<DetalleCierreAgenciasAgenciaDto> agenciasDtos) {
        this();
        this.semana = (Integer) almacenObjects.getObject("semana");
        this.anio = (Integer) almacenObjects.getObject("anio");
        this.sucursal = (String) almacenObjects.getObject("sucursal");
        this.gerente = (String) almacenObjects.getObject("gerente");
        this.zona = (String) almacenObjects.getObject("zona");
        this.agencias = agenciasDtos;
    }
}
