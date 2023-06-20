package com.example.soleproprietorship.config;

/**
 * Klasa służy do generowania wiadomości, które są przesyłane w ResponseEntity, jako odpowiedź serwera.
 */
public class MessageResponse {

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
