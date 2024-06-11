package tech.calaverita.sfast_xpress.pojos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dashboard {
    private String gerencia;
    private String agencia;
    private Integer anio;
    private Integer semana;
    private Integer clientes;
    private Integer clientesCobrados;
    private Integer noPagos;
    private Integer numeroLiquidaciones;
    private Integer pagosReducidos;
    private Double debitoMiercoles;
    private Double debitoJueves;
    private Double debitoViernes;
    private Double debitoTotal;
    private Double rendimiento;
    private Double totalDeDescuento;
    private Double totalCobranzaPura;
    private Double montoExcedente;
    private Double multas;
    private Double liquidaciones;
    private Double cobranzaTotal;
    private Double montoDeDebitoFaltante;
    private Double efectivoEnCampo;
    private String statusAgencia;
}
