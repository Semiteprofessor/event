package com.event.events.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private Paystack paystack = new Paystack();
    private String nodeEnv = "development";

    public static class Paystack {
        private String secretKey;

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }
    }

    public Paystack getPaystack() {
        return paystack;
    }

    public void setPaystack(Paystack paystack) {
        this.paystack = paystack;
    }

    public String getNodeEnv() {
        return nodeEnv;
    }

    public void setNodeEnv(String nodeEnv) {
        this.nodeEnv = nodeEnv;
    }
}
