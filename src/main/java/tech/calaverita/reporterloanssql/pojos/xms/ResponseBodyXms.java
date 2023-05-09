package tech.calaverita.reporterloanssql.pojos.xms;

import lombok.Data;

@Data
public class ResponseBodyXms {
    private String jsonrpc;
    private String id;
    private ResultBody result;
}
