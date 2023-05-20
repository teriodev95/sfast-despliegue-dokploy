package tech.calaverita.reporterloanssql.retrofit.pojos;

import lombok.Data;

@Data
public class PagoBody {
    public PagoBody(){

    }
    public PagoBody(PagoList params){
        this.params = params;
    }
    private String jsonrpc = "2.0";
    private String method = "call";
    private String id = "";
    private PagoList params;
}
