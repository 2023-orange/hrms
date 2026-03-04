package com.sunten.hrms.jsonDatabind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.sunten.hrms.utils.DateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author batan
 * @since 2020-01-03
 */
public class MyLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
//        String dateStr = jsonParser.getText();
//        DateTime dateTime = null;
//        try{
//            dateTime = DateUtil.parse(dateStr);
//        }catch (Exception e){
//            dateTime = DateUtil.parseDateTime(dateStr.replaceAll("T"," "));
//        }
//        Date date = dateTime.toJdkDate();
//        Instant instant = date.toInstant();
//        ZoneId zoneId = ZoneId.systemDefault();
//        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        if(jsonParser.getText().equals("now()")){
            return LocalDate.now();
        }else{
            Long dateLong = jsonParser.getValueAsLong();
            LocalDate localDate = DateUtil.asLocalDate(new Date(dateLong));
            return localDate;
        }
    }

    @Override
    public Class<?> handledType() {
        return LocalDate.class;
    }
}
