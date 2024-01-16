package com.jie1234.jieapiclientsdk;

import com.jie1234.jieapiclientsdk.client.JieApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jieapi.client")
@Data
@ComponentScan
public class JieApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public JieApiClient jieApiClient() {
        return new JieApiClient(accessKey, secretKey);
    }
}
