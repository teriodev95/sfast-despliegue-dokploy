package tech.calaverita.reporterloanssql.services;

import com.google.gson.Gson;
import tech.calaverita.reporterloanssql.retrofit.RetrofitOdoo;

public class Singleton {
    private static Gson gsonInstance;
    public static synchronized Gson getGsonInstance() {
        if (gsonInstance == null) {
            gsonInstance = new Gson();
        }
        return gsonInstance;
    }
}
