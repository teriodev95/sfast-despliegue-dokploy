package tech.calaverita.sfast_xpress.mappers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tech.calaverita.sfast_xpress.pojos.PreguntaCallCenter;

import java.util.ArrayList;

public class PreguntaCallCenterMapper {
    public ArrayList<PreguntaCallCenter> mapOuts(String out) {
        TypeToken<ArrayList<PreguntaCallCenter>> typeToken = new TypeToken<>() {
        };
        return new Gson().fromJson(out, typeToken);
    }

}
