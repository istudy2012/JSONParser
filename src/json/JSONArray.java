package json;

import java.util.ArrayList;
import java.util.List;

public class JSONArray {
    private List<Object> list = new ArrayList<>();

    public JSONArray() {

    }

    public JSONArray put (Object item) {
        list.add(item);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Object o : list) {
            sb.append(o).append(',');
        }

        return sb.toString();
    }
}
