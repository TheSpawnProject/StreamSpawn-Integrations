package net.programmer.igoodie.streamspawn_integrations.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class JSONUtils {

    public static Optional<Number> extractNumberFrom(JSONObject json, String key) {
        return Optional.ofNullable(extractNumberFrom(json, key, () -> null));
    }

    public static Number extractNumberFrom(JSONObject json, String key, Supplier<Number> defaultValue) {
        try {
            Object value = json.get(key);

            if (value instanceof String)
                return Double.parseDouble((String) value);

            return (Number) value;

        } catch (Exception e) {
            return defaultValue.get();
        }
    }

    public static <T> T extractFrom(JSONObject json, String key, Class<T> type) {
        return extractFrom(json, key, type, () -> null);
    }

    public static <T> T extractFrom(JSONObject json, String key, Class<T> type, Supplier<T> defaultValue) {
        try {
            Object value = json.get(key);
            return type.cast(value);

        } catch (Exception e) {
            return defaultValue.get();
        }
    }

    public static void forEach(JSONArray array, Consumer<JSONObject> consumer) {
        if (array == null) return;

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject json = array.getJSONObject(i);
                consumer.accept(json);

            } catch (JSONException e) {
                throw new IllegalArgumentException("Error performing JSONArray forEach.", e);
            }
        }
    }

    public static String escape(String jsonString) {
        StringBuilder escapedString = new StringBuilder();

        for (char character : jsonString.toCharArray()) {
            if (character == '\'' || character == '"' || character == '\\') {
                escapedString.append("\\");
            }

            escapedString.append(character);
        }

        return escapedString.toString();
    }

}
