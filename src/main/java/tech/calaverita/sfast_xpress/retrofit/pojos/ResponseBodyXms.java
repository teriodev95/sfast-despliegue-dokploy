package tech.calaverita.sfast_xpress.retrofit.pojos;

import lombok.Data;

@Data
public class ResponseBodyXms {
    private String jsonrpc;
    private String id;
    private ResultBody result;
}
