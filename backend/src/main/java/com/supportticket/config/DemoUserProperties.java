package com.supportticket.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app.security")
public class DemoUserProperties {

    private List<DemoUser> demoUsers = new ArrayList<>();

    public List<DemoUser> getDemoUsers() {
        return demoUsers;
    }

    public void setDemoUsers(List<DemoUser> demoUsers) {
        this.demoUsers = demoUsers;
    }

    public static class DemoUser {

        private String username;
        private String password;
        private String role;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
