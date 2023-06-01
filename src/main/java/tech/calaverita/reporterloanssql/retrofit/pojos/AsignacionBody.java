package tech.calaverita.reporterloanssql.retrofit.pojos;

import lombok.Data;

@Data
public class AsignacionBody {
    public AsignacionBody() {

    }

    public AsignacionBody(AsignacionList params) {
        this.params = params;
    }

    private String jsonrpc = "2.0";
    private String method = "call";
    private String id = "";
    private AsignacionList params;
}
