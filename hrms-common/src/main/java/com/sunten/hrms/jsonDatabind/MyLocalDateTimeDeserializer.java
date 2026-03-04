package com.sunten.hrms.jsonDatabind;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.sunten.hrms.utils.DateUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
/**
 * @author batan
 * @since 2020-01-03
 */
public class MyLocalDateTimeDeserializer  extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if(jsonParser.getText().equals("now()")){
            return LocalDateTime.now();
        }else {
            Long dateLong = jsonParser.getValueAsLong();
            LocalDateTime localDateTime = DateUtil.asLocalDateTime(new Date(dateLong));
            return localDateTime;
        }
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
//        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
    }

    @Override
    public Class<?> handledType() {
        return LocalDateTime.class;
    }
}
