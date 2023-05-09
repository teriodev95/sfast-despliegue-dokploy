package tech.calaverita.reporterloanssql.retrofit;

import lombok.Data;

@Data
public class RetrofitResult {
    private Integer code;
    private String message;
    private String data;
}
