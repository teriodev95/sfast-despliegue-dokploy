package tech.calaverita.sfast_xpress.threads.pwa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.calaverita.sfast_xpress.enums.CobranzaStatusPWAEnum;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.PagoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoModel;
import tech.calaverita.sfast_xpress.pojos.PWA.PrestamoCobranzaPWA;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.PagoService;

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
    private PrestamoModel prestamoModel;
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
            PrestamoModel prestamoModel,
            PrestamoCobranzaPWA prestamoCobranzaPwa,
            int anio,
            int semana
    ) {
        this.prestamoModel = prestamoModel;
        this.prestamoCobranzaPwa = prestamoCobranzaPwa;
        this.anio = anio;
        this.semana = semana;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void run() {
        getMontoStatusAndPorcentaje(prestamoModel, prestamoCobranzaPwa, anio, semana);
    }

    public void getMontoStatusAndPorcentaje(PrestamoModel prestamoModel, PrestamoCobranzaPWA prestamoCobranzaPwa, int anio, int semana) {
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        CalendarioModel calendarioModel = CobranzaPWAThread.calServ.findByFechaActual(fechaActual);

        LocalDateTime fechaInicioSemana = LocalDate.parse(calendarioModel.getDesde()).atStartOfDay();
        LocalDateTime cierreMiercoles = fechaInicioSemana.plusHours(21);
        LocalDateTime cierreJueves = fechaInicioSemana.plusDays(1).plusHours(21);

        ArrayList<PagoModel> pagEntPagoEntities = CobranzaPWAThread.pagServ.findByPrestamoIdAnioAndSemanaOrderByFechaPagoDesc(prestamoModel.getPrestamoId(), anio, semana);

        prestamoCobranzaPwa.setNombre(prestamoModel.getNombres() + " " + prestamoModel.getApellidoPaterno() + " " + prestamoModel.getApellidoMaterno());
        prestamoCobranzaPwa.setPrestamoId(prestamoModel.getPrestamoId());
        prestamoCobranzaPwa.setTarifa(prestamoModel.getSaldoAlIniciarSemana() < prestamoModel.getTarifa() ? prestamoModel.getSaldoAlIniciarSemana() : prestamoModel.getTarifa());
        prestamoCobranzaPwa.setTotalAPagar(prestamoModel.getTotalAPagar());
        prestamoCobranzaPwa.setPagado(prestamoModel.getCobrado());
        prestamoCobranzaPwa.setRestante(prestamoModel.getSaldo());
        prestamoCobranzaPwa.setDiaDePago(prestamoModel.getDiaDePago());

        if (!pagEntPagoEntities.isEmpty()) {
            if (pagEntPagoEntities.size() == 1) {
                prestamoCobranzaPwa.setCobradoEnLaSemana(pagEntPagoEntities.get(0).getMonto());
                prestamoCobranzaPwa.setFechaUltimoPago(pagEntPagoEntities.get(0).getFechaPago());
            } else {
                double cobradoEnLaSemana = 0;

                for (PagoModel pagoModel : pagEntPagoEntities) {
                    cobradoEnLaSemana += pagoModel.getMonto();
                }

                prestamoCobranzaPwa.setCobradoEnLaSemana(cobradoEnLaSemana);
                prestamoCobranzaPwa.setFechaUltimoPago(pagEntPagoEntities.get(0).getFechaPago());
            }
        }

        prestamoCobranzaPwa.setStatus(prestamoCobranzaPwa.getCobradoEnLaSemana() == 0 ? CobranzaStatusPWAEnum.Pendiente : prestamoCobranzaPwa.getCobradoEnLaSemana() >= prestamoCobranzaPwa.getTarifa() ? CobranzaStatusPWAEnum.Completado : CobranzaStatusPWAEnum.Parcial);
        prestamoCobranzaPwa.setPorcentaje(Math.round(prestamoCobranzaPwa.getPagado() / prestamoCobranzaPwa.getTotalAPagar() * 100.0) / 100.0 * 100);

        if (prestamoCobranzaPwa.getStatus().equals(CobranzaStatusPWAEnum.Pendiente) && prestamoCobranzaPwa.getDiaDePago().equals("MIERCOLES") && fechaActual.compareTo(cierreMiercoles.toString()) >= 0) {
            prestamoCobranzaPwa.setStatus(CobranzaStatusPWAEnum.Desfase);
        }
        if (prestamoCobranzaPwa.getStatus().equals(CobranzaStatusPWAEnum.Pendiente) && prestamoCobranzaPwa.getDiaDePago().equals("JUEVES") && fechaActual.compareTo(cierreJueves.toString()) >= 0) {
            prestamoCobranzaPwa.setStatus(CobranzaStatusPWAEnum.Desfase);
        }
    }
}
