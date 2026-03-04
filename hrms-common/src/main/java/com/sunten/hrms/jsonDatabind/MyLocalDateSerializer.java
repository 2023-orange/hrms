package com.sunten.hrms.jsonDatabind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sunten.hrms.utils.DateUtil;

import java.io.IOException;
import java.time.LocalDate;
/**
 * @author batan
 * @since 2020-01-03
 */
public class MyLocalDateSerializer extends JsonSerializer<LocalDate> {
    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(DateUtil.asTimestamp(localDate).getTime());
    }
}