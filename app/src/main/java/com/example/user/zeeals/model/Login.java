package com.example.user.zeeals.model;


public class Login {
    private String email;
    private String password;
    private String device;

    public String getDevice() {
        return device;
    }

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
        this.device="$2y$10$Ty5GIOshIus/y18kZtDR3O6V9gKPk/Rhhv40zyMYVNeOKhC0QLzz6";
    }
}
