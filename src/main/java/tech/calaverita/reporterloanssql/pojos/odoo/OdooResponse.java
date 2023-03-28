package tech.calaverita.reporterloanssql.pojos.odoo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OdooResponse {
    private String jsonrpc;
    private String id;
    private OdooResult result;
}
