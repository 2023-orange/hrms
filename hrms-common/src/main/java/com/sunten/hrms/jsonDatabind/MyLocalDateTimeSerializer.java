package com.sunten.hrms.jsonDatabind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sunten.hrms.utils.DateUtil;

import java.io.IOException;
import java.time.LocalDateTime;
/**
 * @author batan
 * @since 2020-01-03
 */
public class MyLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(DateUtil.asTimestamp(localDateTime).getTime());
    }
}