package ru.exrates;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.ArrayBlockingQueueDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

public class CustomArBlQDeserializer extends StdDeserializer<ArrayBlockingQueue<Double>> {


    public CustomArBlQDeserializer() {
        super(ArrayBlockingQueue.class);
    }



    @Override
    public ArrayBlockingQueue<Double> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            var list = new ArrayList<Double>();
            while ( p.nextToken() == JsonToken.VALUE_NUMBER_FLOAT){
                list.add(p.getDoubleValue());
            }
            return list.size() > 0 ? new ArrayBlockingQueue<Double>(list.size(), false, list) : new ArrayBlockingQueue<Double>(1);
    }




}