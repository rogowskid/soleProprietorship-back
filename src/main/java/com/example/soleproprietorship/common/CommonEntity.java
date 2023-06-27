package com.example.soleproprietorship.common;

public interface CommonEntity {

    /**
     * Metoda służy do parsowania adresu mailowego
     * @param email
     * @return
     */
    default String parseEmail(String email) {
        return email.replaceAll("[^a-zA-Z0-9@.]", "");
    }

    /**
     * Metoda służy do parsowania telefonu komórkowego
     * @param phoneNumber
     * @return
     */
    default String parsePhoneNumber(String phoneNumber){
        return phoneNumber.replaceAll("[^\\d-]", "");
    }

}
