package ru.exrates.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.exrates.entities.TimePeriod;
import java.io.IOException;
import java.io.StringWriter;


public class JsonSerializers {
    
    public static class TimePeriodSerializer extends JsonSerializer<TimePeriod>{
        private ObjectMapper mapper = new ObjectMapper();

        @Override
        public void serialize(TimePeriod value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            try {
                StringWriter writer = new StringWriter();
                mapper.writeValue(writer, value.getName());
                gen.writeFieldName(writer.toString());


                /*System.out.println("value = [" + value + "]");
                gen.writeFieldName();
                gen.writeStringField("");*/
            }catch (Exception e){

                e.printStackTrace();

            }
        }
    }


}
