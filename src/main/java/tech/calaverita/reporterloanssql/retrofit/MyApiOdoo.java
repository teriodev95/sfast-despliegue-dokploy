package tech.calaverita.reporterloanssql.retrofit;

import retrofit2.Call;
import retrofit2.http.*;
import tech.calaverita.reporterloanssql.retrofit.pojos.AsignacionBody;
import tech.calaverita.reporterloanssql.retrofit.pojos.LiquidacionBody;
import tech.calaverita.reporterloanssql.retrofit.pojos.PagoBody;
import tech.calaverita.reporterloanssql.retrofit.pojos.ResponseBodyXms;

public interface MyApiOdoo {

//    @GET("cobranza/{agency_}/{year_}/{week_}")
//    Call<CobranzaResponse> cobranzaGetByAgencyAndWeek(@Path("agency_") String agency, @Path("year_") int year, @Path("week_") int week);

//    @POST("pays/create-one")
//    Call<ResponseBody> pagoCreateOne(@Body PagoRealm pago);

    @POST("api/v1/prestamos/liquidacion")
    Call<ResponseBodyXms> liquidacionCreateOne(@Header("Cookie") String userCookie, @Body LiquidacionBody liquidacion);

    @POST("api/v1/prestamos/pagos")
    Call<ResponseBodyXms> pagoCreateOne(@Header("Cookie") String userCookie, @Body PagoBody pago);

    @POST("api/v1/asignaciones")
    Call<ResponseBodyXms> asignacionCreateOne(@Header("Cookie") String userCookie, @Body AsignacionBody asignacion);
}
