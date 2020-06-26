package com.kakaopay.moneyscatter.exception.handler;

import org.springframework.http.HttpStatus;

public class ErrorMessage {
    private String status;
    private String message;

    public ErrorMessage(HttpStatus st, String msg) {
        this.status = st.toString();
        this.message = msg;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
