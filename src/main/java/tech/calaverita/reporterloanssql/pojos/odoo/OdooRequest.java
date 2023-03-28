package tech.calaverita.reporterloanssql.pojos.odoo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import tech.calaverita.reporterloanssql.models.*;

@Data
public class OdooRequest {
    public OdooRequest(){

    }

    public OdooRequest(Object object){
        this.params = object;
    }

    private String jsonrpc = "2.0";
    private String method = "call";
    private String id = "";
    private Object params;
}
