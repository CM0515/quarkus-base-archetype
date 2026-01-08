package com.example.microservice.config;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.config.inject.ConfigProperty;

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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public long getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public boolean isProduction() {
        return "production".equalsIgnoreCase(environment);
    }

    public boolean isDevelopment() {
        return "development".equalsIgnoreCase(environment);
    }
}
