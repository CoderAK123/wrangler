/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.wrangler.api.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Tokenizer implementation for parsing input strings into tokens.
 */
public class Tokenizer {

    private final String input;

    // Regex patterns for token detection
    private static final String TIME_DURATION_PATTERN = "\\d+(ms|s|m|h|d)";
    private static final String BYTE_SIZE_PATTERN = "\\d+(B|KB|MB|GB|TB)";

    /**
     * Creates a tokenizer for the given input string.
     * @param input the string to be tokenized
     */
    public Tokenizer(final String input) {
        this.input = input;
    }

    /**
     * Tokenize the input string into a list of tokens.
     */
    public final List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        // Split the input into parts (for simplicity, we split by whitespace)
        String[] parts = input.split("\\s+");
        for (String part : parts) {
            if (isTimeDuration(part)) {
                tokens.add(new TimeDuration(part));
            } else if (isByteSize(part)) {
                tokens.add(new ByteSize(part));
            } else {
                // Add other token types or default token if needed
                tokens.add(new SimpleToken(part));
            }
        }
        return tokens;
    }

    /**
     * Checks if the given value is a time duration.
     * @param value the value to check
     * @return true if the value matches the time duration pattern, false otherwise
     */
    private boolean isTimeDuration(String value) {
        return value.matches(TIME_DURATION_PATTERN);
    }

    /**
     * Checks if the given value is a byte size.
     * @param value the value to check
     * @return true if the value matches the byte size pattern, false otherwise
     */
    private boolean isByteSize(String value) {
        return value.matches(BYTE_SIZE_PATTERN);
    }

    /**
     * Simple token for unclassified values
     */
    private static class SimpleToken implements Token {
        private final String value;

        public SimpleToken(String value) {
            this.value = value;
        }

        @Override
        public TokenType type() {
            return TokenType.TEXT; // Assuming STRING is a valid type in TokenType
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public com.google.gson.JsonObject toJson() {
            com.google.gson.JsonObject json = new com.google.gson.JsonObject();
            json.addProperty("type", type().name());
            json.addProperty("value", value);
            return json;
        }
    }
}
