package com.isilona.restapi.spring;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;
import java.util.Optional;

@Configuration
@ComponentScan({"com.isilona.restapi.web", "com.isilona.common.web"})
@EnableWebMvc
public class HostmanagerWebConfig extends WebMvcConfigurerAdapter {

    public HostmanagerWebConfig() {
        super();
    }

    @Override
    public void extendMessageConverters(final List<HttpMessageConverter<?>> converters) {

        //Look for the converter responsible for Un/Marshaling JSON
        final Optional<HttpMessageConverter<?>> jackson2HttpMessageConverterFound = converters.stream().filter(c -> c instanceof AbstractJackson2HttpMessageConverter).findFirst();
        if (jackson2HttpMessageConverterFound.isPresent()) {
            final AbstractJackson2HttpMessageConverter converter = (AbstractJackson2HttpMessageConverter) jackson2HttpMessageConverterFound.get();

            //Enable pretty print output
            converter.getObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

            // deserialization will fail whenever we are passing property that does not map to the DTO
            converter.getObjectMapper().enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }

        final Optional<HttpMessageConverter<?>> jackson2XmlHttpMessageConverterFound = converters.stream().filter(c -> c instanceof MappingJackson2XmlHttpMessageConverter).findFirst();
        if (jackson2XmlHttpMessageConverterFound.isPresent()) {
            final MappingJackson2XmlHttpMessageConverter converter = (MappingJackson2XmlHttpMessageConverter) jackson2XmlHttpMessageConverterFound.get();
            converter.getObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            converter.getObjectMapper().enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:4200")
                .allowedHeaders("*")
                .allowedMethods("*");
    }

}