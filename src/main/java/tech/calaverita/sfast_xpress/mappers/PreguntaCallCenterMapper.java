package tech.calaverita.sfast_xpress.mappers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class PreguntaCallCenterMapper {
    public ArrayList<Object> mapOuts(String out) {
        TypeToken<ArrayList<Object>> typeToken = new TypeToken<>() {
        };
        return new Gson().fromJson(out, typeToken);
    }

}
