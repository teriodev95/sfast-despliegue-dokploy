package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.PrestamoModel;

import java.util.ArrayList;

@Data
public class CobranzaPwa {
    ArrayList<PrestamoCobranzaPwa> cobranza;
}
