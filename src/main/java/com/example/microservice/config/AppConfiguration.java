package com.example.microservice.config;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Data
@RegisterForReflection
public class AppConfiguration {

    @ConfigProperty(name = "app.name")
    public String appName;

    @ConfigProperty(name = "app.environment")
    public String environment;

    @ConfigProperty(name = "app.security.jwt-secret")
    public String jwtSecret;

    @ConfigProperty(name = "app.security.token-expiration")
    public long tokenExpiration;

    public boolean isProduction() {
        return "production".equalsIgnoreCase(environment);
    }

    public boolean isDevelopment() {
        return "development".equalsIgnoreCase(environment);
    }
}
