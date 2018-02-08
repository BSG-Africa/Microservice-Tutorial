package za.co.bsg.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "za.co.bsg")
public class FeignConfiguration {

}
