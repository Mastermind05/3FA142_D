package org.itp.server;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {
    @Override
    public void serialize(LocalDate date, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}