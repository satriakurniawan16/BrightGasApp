package com.pertamina.brightgasapp;

public class ChangePassword {
    private String old_password;
    private String new_password;
    private String new_password_confirm;

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getNew_password_confirm() {
        return new_password_confirm;
    }

    public void setNew_password_confirm(String new_password_confirm) {
        this.new_password_confirm = new_password_confirm;
    }
}
