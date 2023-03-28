package tech.calaverita.reporterloanssql.pojos.odoo;

import lombok.Data;

@Data
public class OdooLogin {
    private String login = "elvira@xpress.mail";
    private String password = "qwerty123";
    private String db = "xpress_testing";
}
