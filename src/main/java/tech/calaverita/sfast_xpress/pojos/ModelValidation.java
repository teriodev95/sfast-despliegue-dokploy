package tech.calaverita.sfast_xpress.pojos;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ModelValidation {
    private String strResponse;
    private HttpStatus httpStatus;
    private boolean boolIsOnline;

    public ModelValidation() {

    }

    public ModelValidation(
            String strResponse_I,
            HttpStatus httpStatus_I
    ) {
        this.strResponse = strResponse_I;
        this.httpStatus = httpStatus_I;
    }

    public ModelValidation(
            String strResponse_I,
            HttpStatus httpStatus_I,
            boolean boolIsOnline_I
    ) {
        this.strResponse = strResponse_I;
        this.httpStatus = httpStatus_I;
        this.boolIsOnline = boolIsOnline_I;
    }
}
