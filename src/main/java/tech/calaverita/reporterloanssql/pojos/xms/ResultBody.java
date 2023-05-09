package tech.calaverita.reporterloanssql.pojos.xms;

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
