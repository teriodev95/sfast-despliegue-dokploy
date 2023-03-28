package tech.calaverita.reporterloanssql.services;

import com.google.gson.Gson;
import retrofit2.Call;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.pojos.odoo.OdooRequest;
import tech.calaverita.reporterloanssql.pojos.odoo.OdooResponse;
import tech.calaverita.reporterloanssql.retrofit.RetrofitOdoo;

public class OdooService {
    public static void pagoCreateOne(PagoModel pagoModel){
        OdooRequest odooResponse = new OdooRequest(pagoModel);
        String userCookie = "session_id=c81a51ec6e57d647481899585fad601eedac7213";

        Call<OdooResponse> call = RetrofitOdoo.getInstance().getApi().pagoCreateOne(userCookie, odooResponse);

        call.enqueue(new retrofit2.Callback<OdooResponse>() {
            @Override
            public void onResponse(Call<OdooResponse> call, retrofit2.Response<OdooResponse> response) {
                Gson gson = new Gson();
                System.out.println(gson.toJson(response.body()));

                if(response.body().getResult().getCode() == 201)
                    System.out.println("Odoo");
                else
                    System.out.printf("SFast");
            }

            @Override
            public void onFailure(Call<OdooResponse> call, Throwable throwable) {
                System.out.println("Chales mijo ya la regaste");
            }
        });
    }
}
