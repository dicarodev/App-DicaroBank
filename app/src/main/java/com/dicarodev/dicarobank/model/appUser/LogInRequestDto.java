package com.dicarodev.dicarobank.model.appUser;

public class LogInRequestDto {
    private String dni;
    private String password;

    public LogInRequestDto() {
    }

    public LogInRequestDto(String dni, String password) {
        this.dni = dni;
        this.password = password;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
