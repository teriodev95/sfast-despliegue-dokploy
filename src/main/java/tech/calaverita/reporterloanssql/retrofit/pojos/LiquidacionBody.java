package tech.calaverita.reporterloanssql.retrofit.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.persistence.entities.LiquidacionEntity;

@Data
public class LiquidacionBody {
    private String jsonrpc = "2.0";
    private String method = "call";
    private String id = "";
    private LiquidacionEntity params;
    public LiquidacionBody() {

    }
    public LiquidacionBody(LiquidacionEntity params) {
        setParams(params);
    }
}
