package tech.calaverita.reporterloanssql.utils;

import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.calaverita.reporterloanssql.retrofit.pojos.ResponseBodyXms;

@Service
public class RetrofitOdooUtil {
    public static void sendCall(Call<ResponseBodyXms> call) {
        call.enqueue(new Callback<ResponseBodyXms>() {
            @Override
            public void onResponse(Call<ResponseBodyXms> call, Response<ResponseBodyXms> response) {

            }

            @Override
            public void onFailure(Call<ResponseBodyXms> call, Throwable throwable) {

            }
        });
    }
}
