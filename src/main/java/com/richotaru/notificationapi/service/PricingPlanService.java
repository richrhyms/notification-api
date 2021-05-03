package com.richotaru.notificationapi.service;

import com.richotaru.notificationapi.enums.NotificationType;
import io.github.bucket4j.Bandwidth;

public interface PricingPlanService {
    public Bandwidth resolveBandWidthFromApiKeyAndType(String apiKey, NotificationType type);
}
