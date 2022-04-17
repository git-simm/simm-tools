package com.simm.common.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author simm
 **/
@Order(0)
@Configuration
@ConditionalOnProperty(name = "feign.enabled",matchIfMissing = true)
@EnableFeignClients(basePackages = "com.simm")
public class FeignConfiguration {
}
