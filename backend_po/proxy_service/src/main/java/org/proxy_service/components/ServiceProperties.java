package org.proxy_service.components;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services")
public class ServiceProperties {

    private String authServiceName;

    private String authRegistrationEndpoint;

    private String authAuthorizationEndpoint;

    private String userManagementDeleteUserEndpoint;

    private String sessionServiceName;

    private String createSessionEndpoint;

    private String joinSessionEndpoint;

    private String deleteSessionEndpoint;

    public String getAuthServiceName() {
        return authServiceName;
    }

    public void setAuthServiceName(String authServiceName) {
        this.authServiceName = authServiceName;
    }

    public String getAuthRegistrationEndpoint() {
        return authRegistrationEndpoint;
    }

    public void setAuthRegistrationEndpoint(String authRegistrationEndpoint) {
        this.authRegistrationEndpoint = authRegistrationEndpoint;
    }

    public String getAuthAuthorizationEndpoint() {
        return authAuthorizationEndpoint;
    }

    public void setAuthAuthorizationEndpoint(String authAuthorizationEndpoint) {
        this.authAuthorizationEndpoint = authAuthorizationEndpoint;
    }

    public String getUserManagementDeleteUserEndpoint() {
        return userManagementDeleteUserEndpoint;
    }

    public void setUserManagementDeleteUserEndpoint(String userManagementDeleteUserEndpoint) {
        this.userManagementDeleteUserEndpoint = userManagementDeleteUserEndpoint;
    }

    public String getSessionServiceName() {
        return sessionServiceName;
    }

    public void setSessionServiceName(String sessionServiceName) {
        this.sessionServiceName = sessionServiceName;
    }

    public String getCreateSessionEndpoint() {
        return createSessionEndpoint;
    }

    public void setCreateSessionEndpoint(String createSessionEndpoint) {
        this.createSessionEndpoint = createSessionEndpoint;
    }

    public String getJoinSessionEndpoint() {
        return joinSessionEndpoint;
    }

    public void setJoinSessionEndpoint(String joinSessionEndpoint) {
        this.joinSessionEndpoint = joinSessionEndpoint;
    }

    public String getDeleteSessionEndpoint() {
        return deleteSessionEndpoint;
    }

    public void setDeleteSessionEndpoint(String deleteSessionEndpoint) {
        this.deleteSessionEndpoint = deleteSessionEndpoint;
    }
}
