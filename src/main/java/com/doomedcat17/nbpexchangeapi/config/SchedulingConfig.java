package com.doomedcat17.nbpexchangeapi.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "doomedcat17.nbp-exchange-api.scheduling", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SchedulingConfig {

}
