package io.cdap.wrangler.api.parser;
import com.google.gson.JsonObject;
/**
 * Represents a byte size token with parsing logic.
 */
public class ByteSize implements Token {
    private final TokenType type = TokenType.BYTE_SIZE;
    private final String value;
    private final long bytes;

    public ByteSize(String value) {
        this.value = value;
        this.bytes = parseBytes(value);
    }

    private long parseBytes(String value) {
        String numberPart = value.replaceAll("[^0-9.]", "");
        String unitPart = value.replaceAll("[0-9.]", "").toUpperCase();

        double number = Double.parseDouble(numberPart);
        switch (unitPart) {
            case "B": return (long) number;
            case "KB": return (long) (number * 1024);
            case "MB": return (long) (number * 1024 * 1024);
            case "GB": return (long) (number * 1024 * 1024 * 1024);
            case "TB": return (long) (number * 1024L * 1024L * 1024L * 1024L);
            default: throw new IllegalArgumentException("Unknown byte unit: " + unitPart);
        }
    }

    public long getBytes() {
        return bytes;
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
