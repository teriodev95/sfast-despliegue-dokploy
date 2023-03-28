package tech.calaverita.reporterloanssql.retrofit;

import retrofit2.Call;
import retrofit2.http.*;
import tech.calaverita.reporterloanssql.pojos.odoo.OdooRequest;
import tech.calaverita.reporterloanssql.pojos.odoo.OdooResponse;

public interface MyApiOdoo {

//    @GET("cobranza/{agency_}/{year_}/{week_}")
//    Call<CobranzaResponse> cobranzaGetByAgencyAndWeek(@Path("agency_") String agency, @Path("year_") int year, @Path("week_") int week);

//    @POST("pays/create-one")
//    Call<ResponseBody> pagoCreateOne(@Body PagoRealm pago);

//    @PUT("pays/liquidacion-pago")
//    Call<ResponseBody> liquidacionCreateOne(@Body Liquidacion liquidacion);

    @POST("prestamo/pago")
    Call<OdooResponse> pagoCreateOne(@Header("Cookie") String userCookie, @Body OdooRequest odooResponse);

//    @POST("common/login")
//    Call<OdooResponse> loginToOdoo(@Body OdooRequest odooRequest);
}
