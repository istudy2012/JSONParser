package json;

public class JSONTokener {
    private String source;
    private int pos;

    public JSONTokener(String source) {
        this.source = source;
    }

    public Object parse() throws JSONException {
        return nextValue();
    }

    public Object nextValue() throws JSONException {
        int c = nextCleanInternal();
        switch (c) {
            case -1:
                throw new JSONException("invalid input");
            case '{':
                return readObject();
            case '[':
                return readArray();
            case '\"':
                return readString();
            default:
                pos--;
                return readLiteral();
        }
    }

    private int nextCleanInternal() {
        while (pos < source.length()) {
            int c = source.charAt(pos++);
            switch (c) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                    continue;
                default:
                    return c;
            }
        }

        return -1;
    }

    private JSONObject readObject() {
        JSONObject jsonObject = new JSONObject();

        while (true) {
            try {
                Object key = nextValue();
                if (!(key instanceof String)) {
                    throw new JSONException("error key");
                }

                int sep = nextCleanInternal();
                if (sep != ':') {
                    throw new JSONException("error sep");
                }

                Object object = nextValue();
                jsonObject.put((String) key, object);

                int end = nextCleanInternal();
                if (end == '}') {
                    return jsonObject;
                }
            } catch (JSONException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
    }

    private JSONArray readArray() {
        JSONArray jsonArray = new JSONArray();
        while (true) {
            try {
                Object object = nextValue();
                jsonArray.put(object);

                int end = nextCleanInternal();
                if (end == ']') {
                    return jsonArray;
                }
            } catch (JSONException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String readString() {
        int start = pos++;
        while (pos < source.length()) {
            int c = source.charAt(pos++);
            if (c == '"') {
                return source.substring(start, pos - 1);
            }
        }

        return null;
    }

    private Object readLiteral() {
        String literal = nextToInternal("{}[]/\\:,=;# \t\f");
        if (literal.equals("null")) {
            return JSONObject.NULL;
        } else if (literal.equals("true")) {
            return Boolean.TRUE;
        } else if (literal.equals("false")) {
            return Boolean.FALSE;
        }

        if (literal.indexOf('.') != -1) {
            return Double.valueOf(literal);
        }

        int base = 10;
        String number = literal;
        if (number.startsWith("0x") || number.startsWith("0X")) {
            number = number.substring(2);
            base = 16;
        } else if (number.startsWith("0") && number.length() > 1) {
            number = number.substring(1);
            base = 8;
        }
        try {
            long longValue = Long.parseLong(number, base);
            if (longValue <= Integer.MAX_VALUE && longValue >= Integer.MIN_VALUE) {
                return (int) longValue;
            } else {
                return longValue;
            }
        } catch (NumberFormatException e) {
                /*
                 * This only happens for integral numbers greater than
                 * Long.MAX_VALUE, numbers in exponential form (5e-10) and
                 * unquoted strings. Fall through to try floating point.
                 */
        }

        return literal;
    }

    private String nextToInternal(String excluded) {
        int start = pos;
        for (; pos < source.length(); pos++) {
            char c = source.charAt(pos);
            if (c == '\r' || c == '\n' || excluded.indexOf(c) != -1) {
                return source.substring(start, pos);
            }
        }
        return source.substring(start);
    }
}
