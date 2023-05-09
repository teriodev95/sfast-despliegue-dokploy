package tech.calaverita.reporterloanssql.pojos.xms;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.PagoModel;

@Data
public class PagoBody {
    public PagoBody(){

    }

    public PagoBody(PagoList params){
        setParams(params);
    }

    private String jsonrpc = "2.0";
    private String method = "call";
    private String id = "";
    private PagoList params;
}
