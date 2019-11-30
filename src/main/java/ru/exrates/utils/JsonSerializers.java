package ru.exrates.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.exrates.entities.TimePeriod;
import java.io.IOException;


public class JsonSerializers {
    
    public static class TimePeriodSerializer extends JsonSerializer<TimePeriod>{

        @Override
        public void serialize(TimePeriod value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            try {
                gen.writeFieldName(value.getName());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
