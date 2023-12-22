package tech.calaverita.reporterloanssql.utils;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.persistence.entities.CalendarioEntity;
import tech.calaverita.reporterloanssql.persistence.entities.LiquidacionEntity;
import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.PorcentajesDescuentoLiquidacionesEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;
import tech.calaverita.reporterloanssql.services.CalendarioService;
import tech.calaverita.reporterloanssql.services.LiquidacionService;
import tech.calaverita.reporterloanssql.services.PorcentajesDescuentoLiquidacionesService;
import tech.calaverita.reporterloanssql.services.view.PrestamoService;

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

    public LiquidacionUtil(
            PrestamoService prestamoService,
            LiquidacionService liquidacionService,
            CalendarioService calendarioService,
            PorcentajesDescuentoLiquidacionesService porcentajesDescuentoLiquidacionesService
    ) {
        LiquidacionUtil.prestamoService = prestamoService;
        LiquidacionUtil.liquidacionService = liquidacionService;
        LiquidacionUtil.calendarioService = calendarioService;
        LiquidacionUtil.porcentajesDescuentoLiquidacionesService = porcentajesDescuentoLiquidacionesService;
    }

    public static LiquidacionDTO getLiquidacionDTO(
            String prestamoId
    ) {
        LiquidacionDTO liquidacionDTO = null;

        Optional<PrestamoEntity> prestamoEntity = prestamoService.findById(prestamoId);

        if (
                prestamoEntity.isPresent()
        ) {
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

    private static int getDescuentoPorcentaje(
            String identificadorCredito,
            int semanasTranscurridas
    ) {
        int descuentoPorcentaje = 0;

        Optional<PorcentajesDescuentoLiquidacionesEntity> porcentajeDescuentoLiquidacionesEntity = LiquidacionUtil
                .porcentajesDescuentoLiquidacionesService.findById(identificadorCredito);

        if (
                porcentajeDescuentoLiquidacionesEntity.isPresent()
                        && (semanasTranscurridas >= 1 && semanasTranscurridas <= 25)
        ) {
            descuentoPorcentaje = LiquidacionUtil.getDescuentoPorcentajeAux(porcentajeDescuentoLiquidacionesEntity
                    .get(), semanasTranscurridas);
        }

        return descuentoPorcentaje;
    }

    private static int getDescuentoPorcentajeAux(
            PorcentajesDescuentoLiquidacionesEntity porcentajesDescuentoLiquidacionesEntity,
            int semanasTranscurridas
    ) {
        int descuentoPorcentaje = 0;

        switch (semanasTranscurridas - 1) {
            case 0 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana1();
            case 1 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana2();
            case 2 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana3();
            case 3 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana4();
            case 4 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana5();
            case 5 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana6();
            case 6 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana7();
            case 7 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana8();
            case 8 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana9();
            case 9 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana10();
            case 10 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana11();
            case 11 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana12();
            case 12 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana13();
            case 13 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana14();
            case 14 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana15();
            case 15 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana16();
            case 16 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana17();
            case 17 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana18();
            case 18 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana19();
            case 19 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana20();
            case 20 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana21();
            case 21 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana22();
            case 22 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana23();
            case 23 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana24();
            case 24 -> descuentoPorcentaje = porcentajesDescuentoLiquidacionesEntity.getSemana25();
        }
        return descuentoPorcentaje;
    }

    private static double getDescuentoDinero(
            double saldo,
            int descuentoPorcentaje
    ) {
        return saldo / 100 * descuentoPorcentaje;
    }

    private static int getSemanasTranscurridas(
            int anio,
            int semana
    ) {
        CalendarioEntity semanaEntregaCalendarioEntity = new CalendarioEntity();
        {
            semanaEntregaCalendarioEntity.setAnio(anio);
            semanaEntregaCalendarioEntity.setSemana(semana);
        }

        // To easy code
        String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        CalendarioEntity semanaActualCalendarioEntity = LiquidacionUtil.calendarioService
                .findByFechaActual(fechaActual);
        boolean existsSemana53 = LiquidacionUtil.calendarioService
                .existsByAnioAndSemana(anio, 53);

        int semanasTranscurridas = 0;
        while (!(
                Objects.equals(semanaEntregaCalendarioEntity.getAnio(), semanaActualCalendarioEntity.getAnio())
                        && Objects.equals(semanaEntregaCalendarioEntity.getSemana(), semanaActualCalendarioEntity
                        .getSemana())
        )) {
            LiquidacionUtil.funSemanaSiguiente(semanaEntregaCalendarioEntity, existsSemana53);
            semanasTranscurridas++;
        }

        return semanasTranscurridas;
    }

    private static void funSemanaSiguiente(
            CalendarioEntity calendarioEntity,
            boolean existsSemana53
    ) {
        // To easy code
        int anio = calendarioEntity.getAnio();
        int semana = calendarioEntity.getSemana();

        if (
                semana == 52 && existsSemana53
        ) {
            semana = 53;
        } //
        else if (
                semana == 52 || semana == 53
        ) {
            anio = anio + 1;
            semana = 1;
        }
        //
        else {
            semana = semana + 1;
        }

        calendarioEntity.setAnio(anio);
        calendarioEntity.setSemana(semana);
    }

    private static double getLiquidaCon(
            double saldo,
            double descuentoDinero
    ) {
        return saldo - descuentoDinero;
    }

    public static LiquidacionEntity getLiquidacionEntity(
            LiquidacionDTO liquidacionDTO,
            PagoEntity pagoEntity
    ) {
        LiquidacionEntity liquidacionEntity = LiquidacionUtil.liquidacionService.getLiquidacionEntity(liquidacionDTO);
        {
            liquidacionEntity.setAnio(pagoEntity.getAnio());
            liquidacionEntity.setSemana(pagoEntity.getSemana());
            liquidacionEntity.setPagoId(pagoEntity.getPagoId());
        }
        return liquidacionEntity;
    }
}
