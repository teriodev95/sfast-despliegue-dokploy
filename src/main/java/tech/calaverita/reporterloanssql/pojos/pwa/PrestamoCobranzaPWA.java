package tech.calaverita.reporterloanssql.pojos.pwa;

import lombok.Data;
import tech.calaverita.reporterloanssql.enums.CobranzaStatusPWA;

@Data
public class PrestamoCobranzaPWA {
    private String nombre;
    private String prestamoId;
    private Double tarifa;
    private Double cobradoEnLaSemana = 0.0;
    private String diaDePago;
    private Enum<CobranzaStatusPWA> status;
    private String fechaUltimoPago = "";
    private Double totalAPagar;
    private Double pagado;
    private Double restante;
    private Double porcentaje;
}
