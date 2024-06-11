package tech.calaverita.sfast_xpress.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import tech.calaverita.sfast_xpress.retrofit.pojos.AsignacionBody;
import tech.calaverita.sfast_xpress.retrofit.pojos.LiquidacionBody;
import tech.calaverita.sfast_xpress.retrofit.pojos.PagoBody;
import tech.calaverita.sfast_xpress.retrofit.pojos.ResponseBodyXms;
import tech.calaverita.sfast_xpress.security.AuthCredentials;

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

    @POST("login")
    Call<ResponseBodyXms> login(@Body AuthCredentials authCredentials);
}
