package tech.calaverita.sfast_xpress.retrofit.pojos;

import lombok.Data;

@Data
public class PagoBody {
    private String jsonrpc = "2.0";
    private String method = "call";
    private String id = "";
    private PagoList params;
    public PagoBody() {

    }
    public PagoBody(PagoList params) {
        this.params = params;
    }
}
