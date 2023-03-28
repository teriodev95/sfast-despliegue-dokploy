package tech.calaverita.reporterloanssql.pojos.odoo;

import lombok.Data;

@Data
public class OdooResult {
    private Integer code;
    private String message;
    private Integer data;
    private Integer user_uid;
    private String userName;
    private Integer companyID;
    private String company;
    private Boolean authentification;
}
