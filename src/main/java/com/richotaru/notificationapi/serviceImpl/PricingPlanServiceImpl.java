package com.richotaru.notificationapi.serviceImpl;

import com.richotaru.notificationapi.dao.ClientAccountRepository;
import com.richotaru.notificationapi.entity.ClientAccount;
import com.richotaru.notificationapi.entity.SubscriptionPlan;
import com.richotaru.notificationapi.enumeration.GenericStatusConstant;
import com.richotaru.notificationapi.enums.MessageDeliveryChannelConstant;
import com.richotaru.notificationapi.service.PricingPlanService;
import com.richotaru.notificationapi.service.SettingService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PricingPlanServiceImpl implements PricingPlanService {

    private final Long emailRequestLimitFree;
    private final Long smsRequestLimitFree;

    private final ClientAccountRepository clientAccountRepository;
    private final SettingService settingService;

    @Autowired
    public PricingPlanServiceImpl(ClientAccountRepository clientAccountRepository, SettingService settingService) {
        this.clientAccountRepository = clientAccountRepository;
        this.settingService = settingService;
        this.emailRequestLimitFree = settingService.getLong("EMAIL_LIMIT_FREE", 2);
        this.smsRequestLimitFree = settingService.getLong("SMS_LIMIT_FREE",2);
    }

    @Override
    public List<Bandwidth> resolveBandWidthFromApiKeyAndType(String apiKey, MessageDeliveryChannelConstant type) {
        if(type == MessageDeliveryChannelConstant.SMS){
           return getClientSmsBandWidths(apiKey);
        }else {
            return getClientEmailBandWidths(apiKey);
        }
    }
    private List<Bandwidth> getClientSmsBandWidths(String apiKey) {
        List<Bandwidth> bandwidths = new ArrayList<>();
        if (apiKey != null && !apiKey.isEmpty()) {
            Optional<ClientAccount> optionalClientAccount = clientAccountRepository.findByApiKeyAndStatus(apiKey, GenericStatusConstant.ACTIVE);
            if(optionalClientAccount.isPresent()){
                SubscriptionPlan subscriptionPlan = optionalClientAccount.get().getSubscriptionPlan();

                Long maxMonthlySmsLimit = subscriptionPlan.getMaxMonthlySmsLimit();
                if(maxMonthlySmsLimit !=null){
                    Bandwidth monthlySmsBandwidth = Bandwidth.classic(maxMonthlySmsLimit, Refill.intervally(maxMonthlySmsLimit,
                            Duration.ofDays(30)));

                    long daily = Math.floorDiv(maxMonthlySmsLimit , 30);
                    Bandwidth dailySmsBandwidth = Bandwidth.classic(daily, Refill.intervally(daily,
                            Duration.ofDays(1)));

                    long hourly = Math.floorDiv(daily , 24);
                    Bandwidth hourlySmsBandwidth = Bandwidth.classic(hourly, Refill.intervally(hourly,
                            Duration.ofHours(1)));

                    long minutes = Math.floorDiv(daily , 60);

                    minutes = minutes > smsRequestLimitFree ? minutes : smsRequestLimitFree;

                    Bandwidth minuteSmsBandwidth = Bandwidth.classic(minutes, Refill.intervally(minutes,
                            Duration.ofMinutes(1)));

                    bandwidths.add(monthlySmsBandwidth);
                    bandwidths.add(dailySmsBandwidth);
                    bandwidths.add(hourlySmsBandwidth);
                    bandwidths.add(minuteSmsBandwidth);
                }
                return bandwidths;
            }
        }
        bandwidths.add(Bandwidth.classic(smsRequestLimitFree, Refill.intervally(smsRequestLimitFree, Duration.ofMinutes(1))));
        return bandwidths;
    }

    private List<Bandwidth> getClientEmailBandWidths(String apiKey) {
        List<Bandwidth> bandwidths = new ArrayList<>();
        if (apiKey != null && !apiKey.isEmpty()) {
            Optional<ClientAccount> optionalClientAccount = clientAccountRepository.findByApiKeyAndStatus(apiKey, GenericStatusConstant.ACTIVE);
            if(optionalClientAccount.isPresent()){
                SubscriptionPlan subscriptionPlan = optionalClientAccount.get().getSubscriptionPlan();
                Long maxMonthlyEmailLimit = subscriptionPlan.getMaxMonthlyEmailLimit();
                if(maxMonthlyEmailLimit !=null){
                    Bandwidth monthlyEmailBandwith = Bandwidth.classic(maxMonthlyEmailLimit, Refill.intervally(maxMonthlyEmailLimit,
                            Duration.ofDays(30)));
                    bandwidths.add(monthlyEmailBandwith);

                    long daily = Math.floorDiv(maxMonthlyEmailLimit , 30);
                    Bandwidth dailyEmailBandwidth = Bandwidth.classic(daily, Refill.intervally(daily,
                            Duration.ofDays(1)));

                    long hourly = Math.floorDiv(daily, 24);
                    Bandwidth hourlyEmailBandwidth = Bandwidth.classic(hourly, Refill.intervally(hourly,
                            Duration.ofHours(1)));

                    long minutes = Math.floorDiv(daily , 60);
                    minutes = minutes > emailRequestLimitFree ? minutes : emailRequestLimitFree;
                    Bandwidth minuteEmailBandwidth = Bandwidth.classic(minutes, Refill.intervally(minutes,
                            Duration.ofMinutes(1)));

                    bandwidths.add(monthlyEmailBandwith);
                    bandwidths.add(dailyEmailBandwidth);
                    bandwidths.add(hourlyEmailBandwidth);
                    bandwidths.add(minuteEmailBandwidth);
                }

                return bandwidths;
            }
        }
        bandwidths.add(Bandwidth.classic(emailRequestLimitFree, Refill.intervally(emailRequestLimitFree, Duration.ofMinutes(1))));
        return bandwidths;
    }
}
