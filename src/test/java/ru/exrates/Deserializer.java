package ru.exrates;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.ArrayBlockingQueueDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Test;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.TimePeriod;
import ru.exrates.entities.exchanges.secondary.collections.UpdateListenerMap;
import ru.exrates.utils.JsonSerializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class Deserializer {


    @Test
    public void customDeserializer(){
        //String in = "{\"symbol\": \"VENBTC\", \"price\":0.993, \"priceHistory\":[2.4, 3.1]}";
        String in = "{\"symbol\": \"VENBTC\", \"price\":0.993, \"priceHistory\":[]}";
        ObjectMapper objectMapper = new ObjectMapper();
        ModPair cp = null;
        try {
            cp = objectMapper.readValue(in, ModPair.class);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Assert.assertEquals(0, cp.getPriceHistory().size());
        //Assert.assertEquals(2.4d, (double) cp.getPriceHistory().peek(), 0.0);

    }

    static class ModPair{
        @JsonDeserialize(using = CustomArBlQDeserializer.class)
        private ArrayBlockingQueue<Double> priceHistory;
        private String symbol;
        private double price;

        public ModPair() {
            super();
        }


        public ArrayBlockingQueue<Double> getPriceHistory() {
            return priceHistory;
        }

        public void setPriceHistory(ArrayBlockingQueue<Double> priceHistory) {
            this.priceHistory = priceHistory;
        }


        public String getSymbol() {
            return symbol;
        }


        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }


        public double getPrice() {
            return price;
        }


        public void setPrice(double price) {
            this.price = price;
        }


    }

    /*static class CustomArBlQDeserializer extends ArrayBlockingQueueDeserializer {

        public CustomArBlQDeserializer(JavaType containerType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator) {
            super(containerType, valueDeser, valueTypeDeser, valueInstantiator);
        }

        @Override
        public Collection<Object> deserialize(JsonParser p, DeserializationContext ctxt, Collection<Object> result0) throws IOException {
            if(!result0.isEmpty()) return super.deserialize(p, ctxt, result0);
            if(p.isExpectedStartArrayToken()) return handleNonArray(p, ctxt, new ArrayBlockingQueue<Object>(1));
            result0 = super.deserialize(p, ctxt, new ArrayList<>());
            return new ArrayBlockingQueue<>(1, false, result0);
        }
    }*/
}
