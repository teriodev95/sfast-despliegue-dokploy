package tech.calaverita.reporterloanssql.threads;

import tech.calaverita.reporterloanssql.enums.CobranzaStatusPwa;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.PrestamoCobranzaPwa;
import tech.calaverita.reporterloanssql.services.PagoService;

public class CobranzaPwaThread implements Runnable {
    PrestamoModel prestamoModel;
    PrestamoCobranzaPwa prestamoCobranzaPwa;
    int anio;
    int semana;

    public CobranzaPwaThread(PrestamoModel prestamoModel, PrestamoCobranzaPwa prestamoCobranzaPwa, int anio, int semana) {
        this.prestamoModel = prestamoModel;
        this.prestamoCobranzaPwa = prestamoCobranzaPwa;
        this.anio = anio;
        this.semana = semana;
    }

    @Override
    public void run() {
        getMontoStatusAndPorcentaje(prestamoModel, prestamoCobranzaPwa, anio, semana);
    }

    public void getMontoStatusAndPorcentaje(PrestamoModel prestamoModel, PrestamoCobranzaPwa prestamoCobranzaPwa, int anio, int semana) {
        PagoModel pagoModel = PagoService.getPagoByPrestamoIdAnioAndSemana(prestamoModel.getPrestamoId(), anio, semana);

        prestamoCobranzaPwa.setNombre(prestamoModel.getNombres() + " " + prestamoModel.getApellidoPaterno() + " " + prestamoModel.getApellidoMaterno());
        prestamoCobranzaPwa.setPrestamoId(prestamoModel.getPrestamoId());
        prestamoCobranzaPwa.setTarifa(prestamoModel.getSaldoAlIniciarSemana() < prestamoModel.getTarifa() ? prestamoModel.getSaldoAlIniciarSemana() : prestamoModel.getTarifa());
        prestamoCobranzaPwa.setTotalAPagar(prestamoModel.getTotalAPagar());
        prestamoCobranzaPwa.setPagado(prestamoModel.getCobrado());
        prestamoCobranzaPwa.setRestante(prestamoModel.getSaldo());
        prestamoCobranzaPwa.setCobradoEnLaSemana(pagoModel.getMonto());

//        if(!pagoModel.getTipo().equals("No_pago") && !(pagoModel.getMonto() == 0 && pagoModel.getTipo().equals("Reducido"))){
//        }

        prestamoCobranzaPwa.setStatus(prestamoCobranzaPwa.getCobradoEnLaSemana() == 0 ? CobranzaStatusPwa.Pendiente : prestamoCobranzaPwa.getCobradoEnLaSemana() >= prestamoCobranzaPwa.getTarifa() ? CobranzaStatusPwa.Completado : CobranzaStatusPwa.Parcial);
        prestamoCobranzaPwa.setFechaUltimoPago(pagoModel.getFechaPago());
        prestamoCobranzaPwa.setPorcentaje(Math.round(prestamoCobranzaPwa.getPagado() / prestamoCobranzaPwa.getTotalAPagar() * 100.0) / 100.0 * 100);
    }
}
