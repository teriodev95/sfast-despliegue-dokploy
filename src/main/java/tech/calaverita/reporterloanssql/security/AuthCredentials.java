package tech.calaverita.reporterloanssql.security;

import lombok.Data;

@Data
public class AuthCredentials {
    private String username;
    private Integer password;
}
