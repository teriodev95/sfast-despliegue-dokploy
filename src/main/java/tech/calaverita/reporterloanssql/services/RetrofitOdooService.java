package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.calaverita.reporterloanssql.pojos.xms.ResponseBodyXms;
import tech.calaverita.reporterloanssql.retrofit.RetrofitResponse;

@Service
public class RetrofitOdooService {
    public void sendCall(Call<ResponseBodyXms> call){
        call.enqueue(new Callback<ResponseBodyXms>() {
            @Override
            public void onResponse(Call<ResponseBodyXms> call, Response<ResponseBodyXms> response) {
                System.out.println(response.body().toString());
            }

            @Override
            public void onFailure(Call<ResponseBodyXms> call, Throwable throwable) {

            }
        });
    }
}
