package tech.calaverita.reporterloanssql.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitOdoo {
    private static RetrofitOdoo mInstance;
    private Retrofit retrofit;

    private RetrofitOdoo() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://xmsdev.terio.xyz/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized RetrofitOdoo getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitOdoo();
        }
        return mInstance;
    }

    public MyApiOdoo getApi() {
        return retrofit.create(MyApiOdoo.class);
    }

}