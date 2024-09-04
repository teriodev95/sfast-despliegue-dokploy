package tech.calaverita.sfast_xpress.DTOs.cobranza;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;

@Data
public class CobranzaDTO {
    private String gerencia;
    private String agencia;
    private Integer anio;
    private Integer semana;
    private Integer clientes;
    private Double debitoMiercoles;
    private Double debitoJueves;
    private Double debitoViernes;
    private Double debitoTotal;
    @JsonProperty(value = "prestamos")
    private ArrayList<PrestamoViewModel> prestamoViewModels;

    public CobranzaDTO() {

    }

    public CobranzaDTO(InfoCobranzaDTO infoSemanaCobranzaDTO, DebitosCobranzaDTO debitosCobranzaDTO,
            ArrayList<PrestamoViewModel> prestamoViewModels) {
        this.gerencia = infoSemanaCobranzaDTO.getGerencia();
        this.agencia = infoSemanaCobranzaDTO.getAgencia();
        this.anio = infoSemanaCobranzaDTO.getAnio();
        this.semana = infoSemanaCobranzaDTO.getSemana();
        this.clientes = infoSemanaCobranzaDTO.getClientes();
        this.debitoMiercoles = debitosCobranzaDTO.getDebitoMiercoles();
        this.debitoJueves = debitosCobranzaDTO.getDebitoJueves();
        this.debitoViernes = debitosCobranzaDTO.getDebitoViernes();
        this.debitoTotal = debitosCobranzaDTO.getDebitoTotal();
        this.prestamoViewModels = prestamoViewModels;
    }
}
