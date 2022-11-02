package org.example.generic;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;


public abstract class AbstractSerializer {


    protected Gson gson;


    protected AbstractSerializer() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new DateSerializer())
                .registerTypeAdapter(Instant.class, new DateDeserializer())
                .serializeNulls()
                .create();
    }


    public Gson getGson() {
        return gson;
    }

    private static class DateSerializer implements JsonSerializer<Instant> {
        @Override
        public JsonElement serialize(Instant source, Type typeOfSource, JsonSerializationContext context) {
            return new JsonPrimitive(Long.toString(source.toEpochMilli()));
        }
    }

    private static class DateDeserializer implements JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonElement json, Type typeOfTarget, JsonDeserializationContext context) {
            long time = Long.parseLong(json.getAsJsonPrimitive().getAsString());
            return Instant.ofEpochMilli(time);
        }
    }
}