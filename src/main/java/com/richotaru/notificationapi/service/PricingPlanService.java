package com.richotaru.notificationapi.service;

import com.richotaru.notificationapi.enums.MessageDeliveryChannelConstant;
import io.github.bucket4j.Bandwidth;

import java.util.List;

public interface PricingPlanService {
     List<Bandwidth> resolveBandWidthFromApiKeyAndType(String apiKey, MessageDeliveryChannelConstant type);
}
