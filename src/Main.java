import json.JSONObject;

public class Main {

    public static void main(String[] args) {
        String jsonStr = "{\n" +
                "  \"username\": \"shiq\",\n" +
                "  \"age\": 20,\n" +
                "  \"phone\": [\n" +
                "    123,\n" +
                "    456\n" +
                "  ]\n" +
                "}";

        JSONObject jsonObject = new JSONObject(jsonStr);
        System.out.println(jsonObject);
    }
}
