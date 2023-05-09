package tech.calaverita.reporterloanssql.retrofit;

import lombok.Data;

@Data
public class RetrofitResponse {
    private String jsonrpc = "2.0";
    private String id = "";
    private RetrofitResult result;
}
