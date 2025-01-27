package tech.calaverita.sfast_xpress.pojos.PWA;

import lombok.Data;
import tech.calaverita.sfast_xpress.enums.CobranzaStatusPWAEnum;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class PrestamoCobranzaPWA {
    private String nombre;
    private String prestamoId;
    private Double tarifa;
    private Double cobradoEnLaSemana = 0.0;
    private String diaDePago;
    private Enum<CobranzaStatusPWAEnum> status;
    private String fechaUltimoPago = "";
    private Double totalAPagar;
    private Double pagado;
    private Double restante;
    private Double porcentaje;
    private Integer excelIndex;
    private Integer crtp;

    public PrestamoCobranzaPWA() {

    }

    public PrestamoCobranzaPWA(PrestamoViewModel prestamoViewModel) {
        // To easy code
        String nombreCompleto = prestamoViewModel.getNombres() + " " + prestamoViewModel.getApellidoPaterno() + " "
                + prestamoViewModel.getApellidoMaterno();

        this.nombre = nombreCompleto;
        this.prestamoId = prestamoViewModel.getPrestamoId();
        this.tarifa = prestamoViewModel.getTarifa();
        this.cobradoEnLaSemana = 0D;
        this.diaDePago = prestamoViewModel.getDiaDePago();
        this.status = CobranzaStatusPWAEnum.Desfase;
        this.fechaUltimoPago = "";
        this.totalAPagar = prestamoViewModel.getTotalAPagar();
        this.pagado = prestamoViewModel.getCobrado();
        this.restante = prestamoViewModel.getSaldo();
        this.porcentaje = MyUtil.getDouble(this.pagado / this.totalAPagar * 100);
        this.excelIndex = prestamoViewModel.getExcelIndex();
    }
}
