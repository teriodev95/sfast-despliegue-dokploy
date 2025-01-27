package tech.calaverita.sfast_xpress.threads.pwa;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.calaverita.sfast_xpress.enums.CobranzaStatusPWAEnum;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.PagoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.pojos.PWA.PrestamoCobranzaPWA;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.PagoService;

@Component
public class CobranzaPWAThread implements Runnable {
    // ------------------------------------------------------------------------------------------------------------------
    /* INSTANCE VARIABLES */
    // ------------------------------------------------------------------------------------------------------------------
    private PrestamoViewModel prestamoViewModel;
    private PrestamoCobranzaPWA prestamoCobranzaPwa;
    private int anio;
    private int semana;
    private static CalendarioService calServ;
    private static PagoService pagServ;

    // ------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTORS */
    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    private CobranzaPWAThread(
            CalendarioService calServ_S,
            PagoService pagServ_S) {
        CobranzaPWAThread.calServ = calServ_S;
        CobranzaPWAThread.pagServ = pagServ_S;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - - - - - - - - - - - - - - - - -
    public CobranzaPWAThread(
            PrestamoViewModel prestamoViewModel,
            PrestamoCobranzaPWA prestamoCobranzaPwa,
            int anio,
            int semana) {
        this.prestamoViewModel = prestamoViewModel;
        this.prestamoCobranzaPwa = prestamoCobranzaPwa;
        this.anio = anio;
        this.semana = semana;
    }

    // ------------------------------------------------------------------------------------------------------------------
    /* METHODS */
    // ------------------------------------------------------------------------------------------------------------------
    @Override
    public void run() {
        getMontoStatusAndPorcentaje(prestamoViewModel, prestamoCobranzaPwa, anio, semana);
    }

    public void getMontoStatusAndPorcentaje(PrestamoViewModel prestamoViewModel,
            PrestamoCobranzaPWA prestamoCobranzaPwa, int anio, int semana) {
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        CalendarioModel calendarioModel = CobranzaPWAThread.calServ.findByFechaActual(fechaActual);

        LocalDateTime fechaInicioSemana = LocalDate.parse(calendarioModel.getDesde()).atStartOfDay();
        LocalDateTime cierreMiercoles = fechaInicioSemana.plusHours(21);
        LocalDateTime cierreJueves = fechaInicioSemana.plusDays(1).plusHours(21);

        // To easy code
        String[] tipos = { "Multa", "Visita" };

        ArrayList<PagoModel> pagEntPagoEntities = CobranzaPWAThread.pagServ
                .findByPrestamoIdAnioAndSemanaAndTipoNotInOrderByFechaPagoDesc(prestamoViewModel.getPrestamoId(), anio,
                        semana, tipos);

        prestamoCobranzaPwa.setNombre(prestamoViewModel.getNombres() + " " + prestamoViewModel.getApellidoPaterno()
                + " " + prestamoViewModel.getApellidoMaterno());
        prestamoCobranzaPwa.setPrestamoId(prestamoViewModel.getPrestamoId());
        prestamoCobranzaPwa.setTarifa(prestamoViewModel.getSaldoAlIniciarSemana() < prestamoViewModel.getTarifa()
                ? prestamoViewModel.getSaldoAlIniciarSemana()
                : prestamoViewModel.getTarifa());
        prestamoCobranzaPwa.setTotalAPagar(prestamoViewModel.getTotalAPagar());
        prestamoCobranzaPwa.setPagado(prestamoViewModel.getCobrado());
        prestamoCobranzaPwa.setRestante(prestamoViewModel.getSaldo());
        prestamoCobranzaPwa.setDiaDePago(prestamoViewModel.getDiaDePago());
        prestamoCobranzaPwa.setExcelIndex(prestamoViewModel.getExcelIndex());

        if (!pagEntPagoEntities.isEmpty()) {
            prestamoCobranzaPwa.setCrtp(pagEntPagoEntities.size());
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
        } else {
            prestamoCobranzaPwa.setCrtp(0);
        }

        prestamoCobranzaPwa.setStatus(prestamoCobranzaPwa.getCobradoEnLaSemana() == 0 ? CobranzaStatusPWAEnum.Pendiente
                : prestamoCobranzaPwa.getCobradoEnLaSemana() >= prestamoCobranzaPwa.getTarifa()
                        ? CobranzaStatusPWAEnum.Completado
                        : CobranzaStatusPWAEnum.Parcial);
        prestamoCobranzaPwa.setPorcentaje(
                Math.round(prestamoCobranzaPwa.getPagado() / prestamoCobranzaPwa.getTotalAPagar() * 100.0) / 100.0
                        * 100);

        if (prestamoCobranzaPwa.getStatus().equals(CobranzaStatusPWAEnum.Pendiente)
                && prestamoCobranzaPwa.getDiaDePago().equals("MIERCOLES")
                && fechaActual.compareTo(cierreMiercoles.toString()) >= 0) {
            prestamoCobranzaPwa.setStatus(CobranzaStatusPWAEnum.Desfase);
        }
        if (prestamoCobranzaPwa.getStatus().equals(CobranzaStatusPWAEnum.Pendiente)
                && prestamoCobranzaPwa.getDiaDePago().equals("JUEVES")
                && fechaActual.compareTo(cierreJueves.toString()) >= 0) {
            prestamoCobranzaPwa.setStatus(CobranzaStatusPWAEnum.Desfase);
        }
    }
}
