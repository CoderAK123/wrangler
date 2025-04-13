package io.cdap.wrangler.api.parser;
import com.google.gson.JsonObject;
/**
 * Represents a time duration token with parsing logic.
 */

public class TimeDuration implements Token {
    private final TokenType type = TokenType.TIME_DURATION;
    private final String value;
    private final long millis;

    public TimeDuration(String value) {
        this.value = value;
        this.millis = parseTimeDuration(value);
    }

    private long parseTimeDuration(String value) {
        String unit = value.replaceAll("[0-9]", "");
        long number = Long.parseLong(value.replaceAll("[^0-9]", ""));
        switch (unit) {
            case "ms": return number;
            case "s": return number * 1000;
            case "m": return number * 60 * 1000;
            case "h": return number * 60 * 60 * 1000;
            case "d": return number * 24 * 60 * 60 * 1000;
            default: throw new IllegalArgumentException("Unknown time unit: " + unit);
        }
    }

    public long getMillis() {
        return millis;
    }

    @Override
    public TokenType type() {
        return type;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type.name());
        json.addProperty("value", value);
        return json;
    }
}
