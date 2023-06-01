package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.enums.CobranzaStatusPwa;

@Data
public class PrestamoCobranzaPwa {
    private String nombre;
    private String prestamoId;
    private Double tarifa;
    private Double cobradoEnLaSemana = 0.0;
    private String diaDePago;
    private Enum<CobranzaStatusPwa> status;
    private String fechaUltimoPago = "";
    private Double totalAPagar;
    private Double pagado;
    private Double restante;
    private Double porcentaje;
}
