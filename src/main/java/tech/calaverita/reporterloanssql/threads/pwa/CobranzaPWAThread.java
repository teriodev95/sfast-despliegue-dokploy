package tech.calaverita.reporterloanssql.threads.pwa;

import tech.calaverita.reporterloanssql.enums.CobranzaStatusPWA;
import tech.calaverita.reporterloanssql.models.CalendarioModel;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.pwa.PrestamoCobranzaPWA;
import tech.calaverita.reporterloanssql.services.CalendarioService;
import tech.calaverita.reporterloanssql.services.PagoService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class CobranzaPWAThread implements Runnable {
    PrestamoModel prestamoModel;
    PrestamoCobranzaPWA prestamoCobranzaPwa;
    int anio;
    int semana;

    public CobranzaPWAThread(PrestamoModel prestamoModel, PrestamoCobranzaPWA prestamoCobranzaPwa, int anio, int semana) {
        this.prestamoModel = prestamoModel;
        this.prestamoCobranzaPwa = prestamoCobranzaPwa;
        this.anio = anio;
        this.semana = semana;
    }

    @Override
    public void run() {
        getMontoStatusAndPorcentaje(prestamoModel, prestamoCobranzaPwa, anio, semana);
    }

    public void getMontoStatusAndPorcentaje(PrestamoModel prestamoModel, PrestamoCobranzaPWA prestamoCobranzaPwa, int anio, int semana) {
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        CalendarioModel calendarioModel = CalendarioService.getSemanaActualXpressByFechaActual(fechaActual);

        LocalDateTime fechaInicioSemana = LocalDate.parse(calendarioModel.getDesde()).atStartOfDay();
        LocalDateTime cierreMiercoles = fechaInicioSemana.plusHours(21);
        LocalDateTime cierreJueves = fechaInicioSemana.plusDays(1).plusHours(21);

        PagoModel pagoModel = PagoService.getPagoByPrestamoIdAnioAndSemana(prestamoModel.getPrestamoId(), anio, semana);

        prestamoCobranzaPwa.setNombre(prestamoModel.getNombres() + " " + prestamoModel.getApellidoPaterno() + " " + prestamoModel.getApellidoMaterno());
        prestamoCobranzaPwa.setPrestamoId(prestamoModel.getPrestamoId());
        prestamoCobranzaPwa.setTarifa(prestamoModel.getSaldoAlIniciarSemana() < prestamoModel.getTarifa() ? prestamoModel.getSaldoAlIniciarSemana() : prestamoModel.getTarifa());
        prestamoCobranzaPwa.setTotalAPagar(prestamoModel.getTotalAPagar());
        prestamoCobranzaPwa.setPagado(prestamoModel.getCobrado());
        prestamoCobranzaPwa.setRestante(prestamoModel.getSaldo());
        prestamoCobranzaPwa.setDiaDePago(prestamoModel.getDiaDePago());

        if (pagoModel != null) {
            prestamoCobranzaPwa.setCobradoEnLaSemana(pagoModel.getMonto());
            prestamoCobranzaPwa.setFechaUltimoPago(pagoModel.getFechaPago());
        }

        prestamoCobranzaPwa.setStatus(prestamoCobranzaPwa.getCobradoEnLaSemana() == 0 ? CobranzaStatusPWA.Pendiente : prestamoCobranzaPwa.getCobradoEnLaSemana() >= prestamoCobranzaPwa.getTarifa() ? CobranzaStatusPWA.Completado : CobranzaStatusPWA.Parcial);
        prestamoCobranzaPwa.setPorcentaje(Math.round(prestamoCobranzaPwa.getPagado() / prestamoCobranzaPwa.getTotalAPagar() * 100.0) / 100.0 * 100);

        if (prestamoCobranzaPwa.getStatus().equals(CobranzaStatusPWA.Pendiente) && prestamoCobranzaPwa.getDiaDePago().equals("MIERCOLES") && fechaActual.compareTo(cierreMiercoles.toString()) >= 0) {
            prestamoCobranzaPwa.setStatus(CobranzaStatusPWA.Desfase);
        }
        if (prestamoCobranzaPwa.getStatus().equals(CobranzaStatusPWA.Pendiente) && prestamoCobranzaPwa.getDiaDePago().equals("JUEVES") && fechaActual.compareTo(cierreJueves.toString()) >= 0) {
            prestamoCobranzaPwa.setStatus(CobranzaStatusPWA.Desfase);
        }
    }
}
