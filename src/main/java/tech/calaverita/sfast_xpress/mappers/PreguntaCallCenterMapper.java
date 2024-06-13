package tech.calaverita.sfast_xpress.mappers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tech.calaverita.sfast_xpress.pojos.PreguntaCallCenter;

import java.util.ArrayList;

public class PreguntaCallCenterMapper {
    public ArrayList<Object> mapOuts(String out) {
        TypeToken<ArrayList<Object>> typeToken = new TypeToken<>() {
        };

        ArrayList<Object> preguntas = new Gson().fromJson(out, typeToken.getType());
        if (preguntas != null) {
            for (int i = 0; i < preguntas.size(); i++) {
                if (preguntas.get(i) instanceof String) {
                    preguntas.set(i, new PreguntaCallCenter());
                }
            }
        }
        return preguntas;
    }

}
