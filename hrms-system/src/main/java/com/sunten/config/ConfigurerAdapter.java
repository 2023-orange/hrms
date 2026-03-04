package com.sunten.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.sunten.hrms.jsonDatabind.MyLocalDateDeserializer;
import com.sunten.hrms.jsonDatabind.MyLocalDateSerializer;
import com.sunten.hrms.jsonDatabind.MyLocalDateTimeDeserializer;
import com.sunten.hrms.jsonDatabind.MyLocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * WebMvcConfigurer
 *
 * @author batan
 * @since 2018-11-30
 */
@Configuration
//@EnableWebMvc
public class ConfigurerAdapter implements WebMvcConfigurer {

    @Value("${file.path}")
    private String path;

    @Value("${file.avatar}")
    private String avatar;

    @Value("${file.pmPhoto}")
    private String pmPhoto;

    @Value("${file.attachedDoc}")
    private String attachedDoc;

    @Value("${file.attestation}")
    private String attestation;

    @Value("${file.courseware}")
    private String courseware;

    @Value("${sunten.date-time-formatter}")
    private String dateTimeFormatter;

    @Value("${sunten.date-formatter}")
    private String dateFormatter;

    @Value("${sunten.time-formatter}")
    private String timeFormatter;

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowCredentials(true)
//                .allowedHeaders("*")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "PUT", "DELETE");
//
//    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        String avatarUtl = Paths.get(avatar).normalize().toUri().toASCIIString();
//        String pathUtl = Paths.get(path).normalize().toUri().toASCIIString();
        String avatarUtl = "file:" + avatar.replace("\\", "/");
        String pathUtl = "file:" + path.replace("\\", "/");
        String pmPhotoUtl = "file:" + pmPhoto.replace("\\", "/");
        String attachedDocUtl = "file:" + attachedDoc.replace("\\", "/");
        String attestationUtl = "file:" + attestation.replace("\\", "/");
        String coursewareUrl = "file:" + courseware.replace("\\", "/");
        registry.addResourceHandler("/avatar/**").addResourceLocations(avatarUtl).setCachePeriod(0);
        registry.addResourceHandler("/file/**").addResourceLocations(pathUtl).setCachePeriod(0);
        registry.addResourceHandler("/pmPhoto/**").addResourceLocations(pmPhotoUtl).setCachePeriod(0);
        registry.addResourceHandler("/attachedDoc/**").addResourceLocations(attachedDocUtl).setCachePeriod(0);
        registry.addResourceHandler("/attestation/**").addResourceLocations(attestationUtl).setCachePeriod(0);
        registry.addResourceHandler("/courseware/**").addResourceLocations(coursewareUrl).setCachePeriod(0);
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        //日期序列化
        javaTimeModule.addSerializer(LocalDateTime.class, new MyLocalDateTimeSerializer());
        javaTimeModule.addSerializer(LocalDate.class, new MyLocalDateSerializer());
//        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormatter)));
//        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormatter)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(timeFormatter)));

        //日期反序列化
//        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormatter)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new MyLocalDateTimeDeserializer());
        javaTimeModule.addDeserializer(LocalDate.class, new MyLocalDateDeserializer());
//        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormatter)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(timeFormatter)));

        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    /**
     * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(dateTimeFormatter));
            }
        };
    }

    /**
     * LocalDate转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern(dateFormatter));
            }
        };
    }

    /**
     * LocalTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                return LocalTime.parse(source, DateTimeFormatter.ofPattern(timeFormatter));
            }
        };
    }
}
