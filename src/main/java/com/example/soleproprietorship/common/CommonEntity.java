package com.example.soleproprietorship.common;

public interface CommonEntity {

    default String parseEmail(String email) {
        return email.replaceAll("[^a-zA-Z0-9@.]", "");
    }

    default String parsePhoneNumber(String phoneNumber){
        return phoneNumber.replaceAll("[^\\d-]", "");
    }

}
