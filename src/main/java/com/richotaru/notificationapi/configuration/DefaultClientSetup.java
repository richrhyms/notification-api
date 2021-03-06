package com.richotaru.notificationapi.configuration;
import com.richotaru.notificationapi.dao.AppRepository;
import com.richotaru.notificationapi.dao.SubscriptionPlanRepository;
import com.richotaru.notificationapi.entity.SubscriptionPlan;


import com.richotaru.notificationapi.dao.ClientAccountRepository;
import com.richotaru.notificationapi.entity.ClientAccount;
import com.richotaru.notificationapi.enumeration.GenericStatusConstant;
import com.richotaru.notificationapi.enums.PricingPlan;
import com.richotaru.notificationapi.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import java.time.LocalDateTime;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Component
public class DefaultClientSetup {

    private final TransactionTemplate transactionTemplate;
    private final ClientAccountRepository clientAccountRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final AppRepository appRepository;
    private final SettingService settingService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public DefaultClientSetup(TransactionTemplate transactionTemplate,
                              ClientAccountRepository clientAccountRepository,
                              SubscriptionPlanRepository subscriptionPlanRepository,
                              AppRepository appRepository,
                              SettingService settingService) {
        this.transactionTemplate = transactionTemplate;
        this.clientAccountRepository = clientAccountRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.appRepository = appRepository;
        this.settingService = settingService;
    }


    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        transactionTemplate.execute(tx -> {
            try {

                    // CREATING PROFESSIONAL CLIENT
                    clientAccountRepository.findByDisplayNameAndStatus("DEFAULT_PROFESSIONAL_CLIENT", GenericStatusConstant.ACTIVE)
                            .orElseGet(() -> {
                                logger.info("Creating Default Client 1...");

                                SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findByName(PricingPlan.PROFESSIONAL.name())
                                        .orElseGet(
                                        ()->{
                                            Long emailRequestLimitProf = settingService.getLong("EMAIL_LIMIT_PROF", 180000);
                                            Long smsRequestLimitProf = settingService.getLong("SMS_LIMIT_PROF", 120000);

                                            SubscriptionPlan plan =  new SubscriptionPlan();
                                            plan.setName(PricingPlan.PROFESSIONAL.name());
                                            plan.setDescription(PricingPlan.PROFESSIONAL.name() + " Service");
                                            plan.setMaxMonthlyEmailLimit(emailRequestLimitProf);
                                            plan.setMaxMonthlySmsLimit(smsRequestLimitProf);
                                            plan.setStatus(GenericStatusConstant.ACTIVE);
                                            plan.setDateDeactivated(LocalDateTime.now());
                                            plan.setCreatedAt(LocalDateTime.now());
                                            plan.setLastUpdatedAt(LocalDateTime.now());
                                            appRepository.persist(plan);
                                            return plan;
                                        });

                                ClientAccount clientSystem = new ClientAccount();
                                clientSystem.setDisplayName("DEFAULT_PROFESSIONAL_CLIENT");
                                clientSystem.setEmail("richotaru@irembo.com");
                                clientSystem.setPhoneNumber("+2347032804231");
                                clientSystem.setStatus(GenericStatusConstant.ACTIVE);
                                clientSystem.setCreatedAt(LocalDateTime.now());
                                clientSystem.setLastUpdatedAt(LocalDateTime.now());
                                clientSystem.setSubscriptionPlan(subscriptionPlan);

                                // GENERATING FAKE API-KEY
                                clientSystem.setApiKey("api-key-752ee7e5-2a9a428a");
                                appRepository.persist(clientSystem);
                                return null;
                            });

                // CREATING BASIC CLIENT
                clientAccountRepository.findByDisplayNameAndStatus("DEFAULT_BASIC_CLIENT", GenericStatusConstant.ACTIVE)
                        .orElseGet(() -> {
                            logger.info("Creating Default Client 2...");
                            SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findByName(PricingPlan.BASIC.name())
                                    .orElseGet(
                                            ()->{
                                                Long emailRequestLimitBasic= settingService.getLong("EMAIL_LIMIT_BASIC", 18000);
                                                Long smsRequestLimitBasic = settingService.getLong("SMS_LIMIT_BASIC", 12000);

                                                SubscriptionPlan plan =  new SubscriptionPlan();

                                                plan.setName(PricingPlan.BASIC.name());
                                                plan.setDescription(PricingPlan.BASIC.name() + " Service");
                                                plan.setMaxMonthlyEmailLimit(emailRequestLimitBasic);
                                                plan.setMaxMonthlySmsLimit(smsRequestLimitBasic);
                                                plan.setStatus(GenericStatusConstant.ACTIVE);
                                                plan.setDateDeactivated(LocalDateTime.now());
                                                plan.setCreatedAt(LocalDateTime.now());
                                                plan.setLastUpdatedAt(LocalDateTime.now());
                                                appRepository.persist(plan);
                                                return plan;
                                            });

                            ClientAccount clientSystem = new ClientAccount();
                            clientSystem.setDisplayName("DEFAULT_BASIC_CLIENT");
                            clientSystem.setEmail("richotaru@gmail.com");
                            clientSystem.setPhoneNumber("+2347032804231");
                            clientSystem.setStatus(GenericStatusConstant.ACTIVE);
                            clientSystem.setCreatedAt(LocalDateTime.now());
                            clientSystem.setLastUpdatedAt(LocalDateTime.now());
                            clientSystem.setSubscriptionPlan(subscriptionPlan);

                            // GENERATING FAKE API-KEY
                            clientSystem.setApiKey("api-key-2a9a428a-742ee7e5");
                            appRepository.persist(clientSystem);
                            return null;
                        });

            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            return null;
        });
    }
}
