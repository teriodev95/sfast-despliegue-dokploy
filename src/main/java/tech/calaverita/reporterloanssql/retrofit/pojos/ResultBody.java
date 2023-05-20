package tech.calaverita.reporterloanssql.retrofit.pojos;

import lombok.Data;

@Data
public class ResultBody {
    private Integer code;
    private String message;
    private Integer data;
    private Integer user_uid;
    private String userName;
    private Integer companyID;
    private String company;
    private Boolean authentification;
}
