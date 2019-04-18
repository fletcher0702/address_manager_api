package com.fantech.addressmanager.api.dto;

public class UserPasswordUpdateDto {

    private String userUuid;
    private String oldPassword;
    private String newPassword;

    public String getUserUuid() {
        return userUuid;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
