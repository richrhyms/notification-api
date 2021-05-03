package com.richotaru.notificationapi.configuration;

import com.richotaru.notificationapi.enums.MessageDeliveryChannelConstant;
import com.richotaru.notificationapi.service.PricingPlanService;
import io.github.bucket4j.grid.GridBucketState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.cache.Cache;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    Cache<String, GridBucketState> cache;
    @Autowired
    PricingPlanService pricingPlanService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(cache, MessageDeliveryChannelConstant.EMAIL, pricingPlanService))
                .addPathPatterns("/api/v1/notification/email/**");
        registry.addInterceptor(new RateLimitInterceptor(cache, MessageDeliveryChannelConstant.SMS, pricingPlanService))
                .addPathPatterns("/api/v1/notification/sms/**");
    }
}
