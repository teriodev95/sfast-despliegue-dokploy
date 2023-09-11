package tech.calaverita.reporterloanssql.retrofit.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.LiquidacionModel;

@Data
public class LiquidacionBody {
    private String jsonrpc = "2.0";
    private String method = "call";
    private String id = "";
    private LiquidacionModel params;
    public LiquidacionBody() {

    }
    public LiquidacionBody(LiquidacionModel params) {
        setParams(params);
    }
}
