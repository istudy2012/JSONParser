package json;

import java.util.LinkedHashMap;
import java.util.Map;

public class JSONObject {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();

    public static final Object NULL = new Object() {
        @Override public boolean equals(Object o) {
            return o == this || o == null; // API specifies this broken equals implementation
        }
        @Override public String toString() {
            return "null";
        }
    };

    public JSONObject() {

    }

    public JSONObject(String jsonStr) {
        JSONTokener jsonTokener = new JSONTokener(jsonStr);

        try {
            Object object = jsonTokener.parse();
            if (object instanceof JSONObject) {
                this.map = ((JSONObject) object).map;
            } else {
                throw new JSONException("err jsonStr");
            }
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }

    public void put(String key, Object object) {
        map.put(key, object);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append('\n');
        }

        return sb.toString();
    }
}
