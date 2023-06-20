package com.example.soleproprietorship.config.services;

public class TotpDto {

    public boolean isUsed;
    public String qr;

    public TotpDto(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public TotpDto(boolean isUsed, String qr) {
        this.isUsed = isUsed;
        this.qr = qr;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }
}
