package com.richotaru.notificationapi.configuration;



import com.richotaru.notificationapi.enums.MessageDeliveryChannelConstant;
import com.richotaru.notificationapi.service.PricingPlanService;
import io.github.bucket4j.*;
import io.github.bucket4j.grid.GridBucketState;
import io.github.bucket4j.grid.ProxyManager;
import io.github.bucket4j.grid.jcache.JCache;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.cache.Cache;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.function.Supplier;


public class RateLimitInterceptor implements HandlerInterceptor {

    private final ProxyManager<String> buckets;
    private final MessageDeliveryChannelConstant messageDeliveryChannelConstant;
    private final PricingPlanService pricingPlanService;

    public RateLimitInterceptor(Cache<String, GridBucketState> cache,
                                MessageDeliveryChannelConstant type,
                                PricingPlanService pricingPlanService) {
        this.buckets = Bucket4j.extension(JCache.class)
                .proxyManagerForCache(cache);
        this.messageDeliveryChannelConstant =type;
        this.pricingPlanService = pricingPlanService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        String apiKey = request.getHeader("X-api-key");
        Bucket tokenBucket = resolveBucket(apiKey);
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                    "You have exhausted your API Request Quota");
            return false;
        }
    }
    public Bucket resolveBucket(String apiKey) {
        return buckets.getProxy(apiKey + messageDeliveryChannelConstant.ordinal(),
                getConfigurationsFromName(apiKey));
    }
    private Supplier<BucketConfiguration> getConfigurationsFromName(String apiKey) {
        ConfigurationBuilder configurationBuilder = Bucket4j.configurationBuilder();
        List<Bandwidth> bandwidths = pricingPlanService.resolveBandWidthFromApiKeyAndType(apiKey, messageDeliveryChannelConstant);
        for(Bandwidth bandwidth: bandwidths){
            configurationBuilder.addLimit(bandwidth);
        }
        return configurationBuilder::build;
    }
}
