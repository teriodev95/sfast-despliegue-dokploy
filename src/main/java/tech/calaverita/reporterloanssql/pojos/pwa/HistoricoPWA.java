package tech.calaverita.reporterloanssql.pojos.pwa;

import lombok.Data;

import java.util.ArrayList;

@Data
public class HistoricoPWA {
    ArrayList<PagoHistoricoPWA> historico;
}
