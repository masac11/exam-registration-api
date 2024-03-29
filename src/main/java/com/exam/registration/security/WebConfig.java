package com.exam.registration.security;

import com.exam.registration.interceptor.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yhf
 * @classname WebConfig
 **/
@Configuration
public class WebConfig extends WebMvcConfigurationSupport{


    @Value("${photo.upload.staticAccessPath}")
    private String staticPhotoPath;
    @Value("${photo.upload.path}")
    private String photoUploadFolder;
    @Value("${ticket.create.staticAccessPath}")
    private String staticTicketPath;
    @Value("${ticket.create.path}")
    private String ticketCreteFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(staticPhotoPath).addResourceLocations("file:///" + photoUploadFolder);
        registry.addResourceHandler(staticTicketPath).addResourceLocations("file:///" + ticketCreteFolder);
        super.addResourceHandlers(registry);
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.clear();
        converters.add(responseBodyConverter());
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.ALL);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        converters.add(mappingJackson2HttpMessageConverter);
    }

    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/**/login")
                .excludePathPatterns("/photo/**")
                .excludePathPatterns("/ticket/**")
                .excludePathPatterns("/**/alipay/notify")
                .excludePathPatterns("/**/alipay/return")
                .excludePathPatterns("/**/students/reg");
        registry.addInterceptor(new PermissionInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/**/login")
                .excludePathPatterns("/photo/**")
                .excludePathPatterns("/ticket/**")
                .excludePathPatterns("/**/alipay/notify")
                .excludePathPatterns("/**/alipay/return")
                .excludePathPatterns("/**/students/reg");
    }
}
