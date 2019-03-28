package com.example.user.zeeals.model;

public class Registration {
    private String email;
    private String password;
    private String password_confirmation;
    private String device;

    public Registration(String email, String password, String password_confirmation, String device) {
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.device = device;
    }

}

