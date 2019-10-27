package com.ftp.deploy.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.ftp.deploy.config.date.DateConverterConfig;
import com.ftp.deploy.config.date.LocalDateFormatter;
import com.ftp.deploy.config.date.LocalDateTimeFormatter;
import com.ftp.deploy.config.date.LocalTimeFormatter;
import com.ftp.deploy.config.filter.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * 配置全局的 跨域访问处理类
 *
 * @author - ZERO
 */
@Configuration
@Slf4j
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Resource
    private RequestInterceptor requestInterceptor;

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        //格式化 date 的时间处理类
        registry.addConverter(new DateConverterConfig());
        //处理LocalDate
        registry.addConverter(new LocalDateTimeFormatter());
        registry.addConverter(new LocalTimeFormatter());
        registry.addConverter(new LocalDateFormatter());
    }

    /**
     * 配置 @ResponseBody 序列化 方法
     *
     * @param converters
     */
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new ToStringSerializer(Long.TYPE));
        module.addSerializer(new ToStringSerializer(Long.class));
        module.addSerializer(new ToStringSerializer(BigInteger.class));

        //反序列化 和 序列化
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        //反序列化 和 序列化
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        //反序列化 和 序列化
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(module);

        converter.setObjectMapper(objectMapper);

        converters.add(converter);
    }


    /**
     * 配置静态资源文件映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludePathPatterns = new String[]{"/js/**", "/css/**", "/img/**", "/", "/static/**", "/errors","/webSocket/**"};
        registry.addInterceptor(requestInterceptor).addPathPatterns("/**").excludePathPatterns(excludePathPatterns);
        super.addInterceptors(registry);
    }

}
