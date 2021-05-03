package com.richotaru.notificationapi.service;

import java.util.Map;

public interface TemplateEngine {

    String getAsString(String templatePath, Map<String, Object> bindings);

    byte[] getBytes(String templatePath, Map<String, Object> bindings);
}
