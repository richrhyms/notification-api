package com.richotaru.notificationapi.serviceImpl;

import com.richotaru.notificationapi.enums.MessageDeliveryChannelConstant;
import com.richotaru.notificationapi.service.PricingPlanService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class PricingPlanServiceImpl implements PricingPlanService {
    @Value("${EMAIL_LIMIT_FREE:40}")
    private Long emailRequestLimitFree;
    @Value("${EMAIL_LIMIT_BASIC:60}")
    private Long emailRequestLimitBasic;
    @Value("${EMAIL_LIMIT_PROF:100}")
    private Long emailRequestLimitProf;
    @Value("${SMS_LIMIT_FREE:20}")
    private Long smsRequestLimitFree;
    @Value("${SMS_LIMIT_BASIC:30}")
    private Long smsRequestLimitBasic;
    @Value("${SMS_LIMIT_PROF:60}")
    private Long smsRequestLimitProf;

    @Override
    public Bandwidth resolveBandWidthFromApiKeyAndType(String apiKey, MessageDeliveryChannelConstant type) {
        com.richotaru.notificationapi.enums.PricingPlan plan = getPricingPlan(apiKey);
        return getLimit(plan,type);
    }
    private Bandwidth getLimit(com.richotaru.notificationapi.enums.PricingPlan plan, MessageDeliveryChannelConstant type) {
        if(type == MessageDeliveryChannelConstant.SMS){
            if(plan == com.richotaru.notificationapi.enums.PricingPlan.FREE){
                return Bandwidth.classic(smsRequestLimitFree, Refill.intervally(smsRequestLimitFree, Duration.ofMinutes(1)));
            }
            if(plan == com.richotaru.notificationapi.enums.PricingPlan.BASIC){
                return Bandwidth.classic(smsRequestLimitBasic, Refill.intervally(smsRequestLimitFree, Duration.ofMinutes(1)));
            }
            if(plan == com.richotaru.notificationapi.enums.PricingPlan.PROFESSIONAL){
                return Bandwidth.classic(smsRequestLimitProf, Refill.intervally(smsRequestLimitFree, Duration.ofMinutes(1)));
            }
        }
        if(type == MessageDeliveryChannelConstant.EMAIL){
            if(plan == com.richotaru.notificationapi.enums.PricingPlan.FREE) {
                return Bandwidth.classic(emailRequestLimitFree, Refill.intervally(emailRequestLimitFree, Duration.ofMinutes(1)));
            }
            if(plan == com.richotaru.notificationapi.enums.PricingPlan.BASIC){
                return Bandwidth.classic(emailRequestLimitBasic, Refill.intervally(emailRequestLimitBasic, Duration.ofMinutes(1)));
            }
            if(plan == com.richotaru.notificationapi.enums.PricingPlan.PROFESSIONAL){
                return Bandwidth.classic(emailRequestLimitProf, Refill.intervally(emailRequestLimitProf, Duration.ofMinutes(1)));
            }
        }
        return Bandwidth.classic(2, Refill.intervally(2, Duration.ofMinutes(1)));
    }
    private com.richotaru.notificationapi.enums.PricingPlan getPricingPlan(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return com.richotaru.notificationapi.enums.PricingPlan.FREE;
        } else if (apiKey.startsWith("PX001-")) {
            return com.richotaru.notificationapi.enums.PricingPlan.PROFESSIONAL;
        } else if (apiKey.startsWith("BX001-")) {
            return com.richotaru.notificationapi.enums.PricingPlan.BASIC;
        }
        return com.richotaru.notificationapi.enums.PricingPlan.FREE;
    }
}
