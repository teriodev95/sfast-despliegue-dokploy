package tech.calaverita.sfast_xpress.pojos;

import java.util.HashMap;

import lombok.Data;

@Data
public class AlmacenObjects {
    private HashMap<String, Object> objects;

    public AlmacenObjects() {
        this.objects = new HashMap<>();
    }

    public void addObject(String key, Object object) {
        this.objects.put(key, object);
    }

    public Object getObject(String key) {
        return this.objects.get(key);
    }
}
