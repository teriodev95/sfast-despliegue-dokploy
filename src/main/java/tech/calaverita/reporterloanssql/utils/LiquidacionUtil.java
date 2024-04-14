package tech.calaverita.reporterloanssql.utils;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.models.mariaDB.CalendarioModel;
import tech.calaverita.reporterloanssql.models.mariaDB.LiquidacionModel;
import tech.calaverita.reporterloanssql.models.mariaDB.PagoModel;
import tech.calaverita.reporterloanssql.models.mariaDB.PorcentajesDescuentoLiquidacionesModel;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PrestamoModel;
import tech.calaverita.reporterloanssql.services.CalendarioService;
import tech.calaverita.reporterloanssql.services.LiquidacionService;
import tech.calaverita.reporterloanssql.services.PorcentajesDescuentoLiquidacionesService;
import tech.calaverita.reporterloanssql.services.views.PrestamoService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Component
public class LiquidacionUtil {
    private static PrestamoService prestamoService;
    private static LiquidacionService liquidacionService;
    private static CalendarioService calendarioService;
    private static PorcentajesDescuentoLiquidacionesService porcentajesDescuentoLiquidacionesService;

    public LiquidacionUtil(PrestamoService prestamoService, LiquidacionService liquidacionService,
                           CalendarioService calendarioService,
                           PorcentajesDescuentoLiquidacionesService porcentajesDescuentoLiquidacionesService) {
        LiquidacionUtil.prestamoService = prestamoService;
        LiquidacionUtil.liquidacionService = liquidacionService;
        LiquidacionUtil.calendarioService = calendarioService;
        LiquidacionUtil.porcentajesDescuentoLiquidacionesService = porcentajesDescuentoLiquidacionesService;
    }

    public static LiquidacionDTO getLiquidacionDTO(String prestamoId) {
        LiquidacionDTO liquidacionDTO = null;

        Optional<PrestamoModel> prestamoEntity = prestamoService.findById(prestamoId);

        if (prestamoEntity.isPresent()) {
            // To easy code
            int anioEntrega = prestamoEntity.get().getAnio();
            int semanaEntrega = prestamoEntity.get().getSemana();

            liquidacionDTO = LiquidacionUtil.liquidacionService.getLiquidacionDTO(prestamoEntity.get());
            liquidacionDTO.setSemanasTranscurridas(LiquidacionUtil.getSemanasTranscurridas(anioEntrega,
                    semanaEntrega));
            liquidacionDTO.setDescuentoPorcentaje(LiquidacionUtil.getDescuentoPorcentaje(liquidacionDTO
                    .getIdentificador(), liquidacionDTO.getSemanasTranscurridas()));
            liquidacionDTO.setDescuentoDinero(LiquidacionUtil.getDescuentoDinero(liquidacionDTO.getSaldo(),
                    liquidacionDTO.getDescuentoPorcentaje()));
            liquidacionDTO.setLiquidaCon(LiquidacionUtil.getLiquidaCon(liquidacionDTO.getSaldo(),
                    liquidacionDTO.getDescuentoDinero()));
        }

        return liquidacionDTO;
    }

    private static int getDescuentoPorcentaje(String identificadorCredito, int semanasTranscurridas) {
        int descuentoPorcentaje = 0;

        Optional<PorcentajesDescuentoLiquidacionesModel> porcentajeDescuentoLiquidacionesEntity = LiquidacionUtil
                .porcentajesDescuentoLiquidacionesService.findById(identificadorCredito);

        if (porcentajeDescuentoLiquidacionesEntity.isPresent()
                && (semanasTranscurridas >= 1 && semanasTranscurridas <= 25)) {
            descuentoPorcentaje = LiquidacionUtil.getDescuentoPorcentajeAux(porcentajeDescuentoLiquidacionesEntity
                    .get(), semanasTranscurridas);
        }

        return descuentoPorcentaje;
    }

    private static int getDescuentoPorcentajeAux(
            PorcentajesDescuentoLiquidacionesModel porcentajesDescuentoLiquidacionesModel,
            int semanasTranscurridas) {
        int descuentoPorcentaje = 0;

        switch (semanasTranscurridas - 1) {
            case 0 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana1();
            case 1 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana2();
            case 2 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana3();
            case 3 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana4();
            case 4 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana5();
            case 5 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana6();
            case 6 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana7();
            case 7 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana8();
            case 8 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana9();
            case 9 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana10();
            case 10 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana11();
            case 11 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana12();
            case 12 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana13();
            case 13 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana14();
            case 14 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana15();
            case 15 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana16();
            case 16 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana17();
            case 17 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana18();
            case 18 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana19();
            case 19 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana20();
            case 20 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana21();
            case 21 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana22();
            case 22 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana23();
            case 23 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana24();
            case 24 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesModel.getSemana25();
        }
        return descuentoPorcentaje;
    }

    private static double getDescuentoDinero(double saldo, int descuentoPorcentaje) {
        return saldo / 100 * descuentoPorcentaje;
    }

    private static int getSemanasTranscurridas(int anio, int semana) {
        CalendarioModel semanaEntregaCalendarioModel = new CalendarioModel();
        {
            semanaEntregaCalendarioModel.setAnio(anio);
            semanaEntregaCalendarioModel.setSemana(semana);
        }

        // To easy code
        String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        CalendarioModel semanaActualCalendarioModel = LiquidacionUtil.calendarioService
                .findByFechaActual(fechaActual);
        boolean existsSemana53 = LiquidacionUtil.calendarioService
                .existsByAnioAndSemana(anio, 53);

        int semanasTranscurridas = 0;
        while (!(
                Objects.equals(semanaEntregaCalendarioModel.getAnio(), semanaActualCalendarioModel.getAnio())
                        && Objects.equals(semanaEntregaCalendarioModel.getSemana(), semanaActualCalendarioModel
                        .getSemana())
        )) {
            LiquidacionUtil.funSemanaSiguiente(semanaEntregaCalendarioModel, existsSemana53);
            semanasTranscurridas++;
        }

        return semanasTranscurridas;
    }

    private static void funSemanaSiguiente(CalendarioModel calendarioModel, boolean existsSemana53) {
        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        if (semana == 52 && existsSemana53) {
            semana = 53;
        } else if (semana == 52 || semana == 53) {
            anio = anio + 1;
            semana = 1;
        } else {
            semana = semana + 1;
        }

        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);
    }

    private static double getLiquidaCon(double saldo, double descuentoDinero) {
        return saldo - descuentoDinero;
    }

    public static LiquidacionModel getLiquidacionEntity(LiquidacionDTO liquidacionDTO, PagoModel pagoModel) {
        LiquidacionModel liquidacionModel = LiquidacionUtil.liquidacionService.getLiquidacionEntity(liquidacionDTO);
        {
            liquidacionModel.setAnio(pagoModel.getAnio());
            liquidacionModel.setSemana(pagoModel.getSemana());
            liquidacionModel.setPagoId(pagoModel.getPagoId());
        }
        return liquidacionModel;
    }
}
