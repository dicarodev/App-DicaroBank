package com.dicarodev.dicarobank.model.appUser;

import java.util.List;

public class LogInResponseDto {
    private String name;
    private List<String> authorities;
    private String token;

    public LogInResponseDto() {
    }

    public LogInResponseDto(String name, List<String> authorities, String token) {
        this.name = name;
        this.authorities = authorities;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
