package tech.calaverita.reporterloanssql.threads.pwa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.enums.CobrStatPwaCobranzaStatusPwa;
import tech.calaverita.reporterloanssql.persistence.entities.CalendarioEntity;
import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;
import tech.calaverita.reporterloanssql.pojos.pwa.PrestamoCobranzaPWA;
import tech.calaverita.reporterloanssql.services.CalendarioService;
import tech.calaverita.reporterloanssql.services.PagoService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Component
public class CobranzaPWAThread implements Runnable {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private PrestamoEntity prestamoEntity;
    private PrestamoCobranzaPWA prestamoCobranzaPwa;
    private int anio;
    private int semana;
    private static CalendarioService calServ;
    private static PagoService pagServ;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //-----------------------------------------------------------------------------------------------------------------
    @Autowired
    private CobranzaPWAThread(
            CalendarioService calServ_S,
            PagoService pagServ_S
    ) {
        CobranzaPWAThread.calServ = calServ_S;
        CobranzaPWAThread.pagServ = pagServ_S;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public CobranzaPWAThread(
            PrestamoEntity prestamoEntity,
            PrestamoCobranzaPWA prestamoCobranzaPwa,
            int anio,
            int semana
    ) {
        this.prestamoEntity = prestamoEntity;
        this.prestamoCobranzaPwa = prestamoCobranzaPwa;
        this.anio = anio;
        this.semana = semana;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void run() {
        getMontoStatusAndPorcentaje(prestamoEntity, prestamoCobranzaPwa, anio, semana);
    }

    public void getMontoStatusAndPorcentaje(PrestamoEntity prestamoEntity, PrestamoCobranzaPWA prestamoCobranzaPwa, int anio, int semana) {
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        CalendarioEntity calendarioEntity = CobranzaPWAThread.calServ.findByFechaActual(fechaActual);

        LocalDateTime fechaInicioSemana = LocalDate.parse(calendarioEntity.getDesde()).atStartOfDay();
        LocalDateTime cierreMiercoles = fechaInicioSemana.plusHours(21);
        LocalDateTime cierreJueves = fechaInicioSemana.plusDays(1).plusHours(21);

        ArrayList<PagoEntity> pagEntPagoEntities = CobranzaPWAThread.pagServ.darrpagModFindByPrestamoIdAnioAndSemana(prestamoEntity.getPrestamoId(), anio, semana);

        prestamoCobranzaPwa.setNombre(prestamoEntity.getNombres() + " " + prestamoEntity.getApellidoPaterno() + " " + prestamoEntity.getApellidoMaterno());
        prestamoCobranzaPwa.setPrestamoId(prestamoEntity.getPrestamoId());
        prestamoCobranzaPwa.setTarifa(prestamoEntity.getSaldoAlIniciarSemana() < prestamoEntity.getTarifa() ? prestamoEntity.getSaldoAlIniciarSemana() : prestamoEntity.getTarifa());
        prestamoCobranzaPwa.setTotalAPagar(prestamoEntity.getTotalAPagar());
        prestamoCobranzaPwa.setPagado(prestamoEntity.getCobrado());
        prestamoCobranzaPwa.setRestante(prestamoEntity.getSaldo());
        prestamoCobranzaPwa.setDiaDePago(prestamoEntity.getDiaDePago());

        if (!pagEntPagoEntities.isEmpty()) {
            if (pagEntPagoEntities.size() == 1) {
                prestamoCobranzaPwa.setCobradoEnLaSemana(pagEntPagoEntities.get(0).getMonto());
                prestamoCobranzaPwa.setFechaUltimoPago(pagEntPagoEntities.get(0).getFechaPago());
            } else {
                double cobradoEnLaSemana = 0;

                for (PagoEntity pagoEntity : pagEntPagoEntities) {
                    cobradoEnLaSemana += pagoEntity.getMonto();
                }

                prestamoCobranzaPwa.setCobradoEnLaSemana(cobradoEnLaSemana);
                prestamoCobranzaPwa.setFechaUltimoPago(pagEntPagoEntities.get(0).getFechaPago());
            }
        }

        prestamoCobranzaPwa.setStatus(prestamoCobranzaPwa.getCobradoEnLaSemana() == 0 ? CobrStatPwaCobranzaStatusPwa.Pendiente : prestamoCobranzaPwa.getCobradoEnLaSemana() >= prestamoCobranzaPwa.getTarifa() ? CobrStatPwaCobranzaStatusPwa.Completado : CobrStatPwaCobranzaStatusPwa.Parcial);
        prestamoCobranzaPwa.setPorcentaje(Math.round(prestamoCobranzaPwa.getPagado() / prestamoCobranzaPwa.getTotalAPagar() * 100.0) / 100.0 * 100);

        if (prestamoCobranzaPwa.getStatus().equals(CobrStatPwaCobranzaStatusPwa.Pendiente) && prestamoCobranzaPwa.getDiaDePago().equals("MIERCOLES") && fechaActual.compareTo(cierreMiercoles.toString()) >= 0) {
            prestamoCobranzaPwa.setStatus(CobrStatPwaCobranzaStatusPwa.Desfase);
        }
        if (prestamoCobranzaPwa.getStatus().equals(CobrStatPwaCobranzaStatusPwa.Pendiente) && prestamoCobranzaPwa.getDiaDePago().equals("JUEVES") && fechaActual.compareTo(cierreJueves.toString()) >= 0) {
            prestamoCobranzaPwa.setStatus(CobrStatPwaCobranzaStatusPwa.Desfase);
        }
    }
}
