package com.kakaopay.moneyscatter.exception.handler;

import com.kakaopay.moneyscatter.exception.NotValidScatterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ScatterApiControllerAdvice {

    private Logger logger = LoggerFactory.getLogger(ScatterApiControllerAdvice.class);

    @ExceptionHandler(NotValidScatterException.class)
    protected ResponseEntity<?> handle(NotValidScatterException e) {
        logger.error("NotValidScatterException", e);
        ErrorMessage em = new ErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.badRequest().body(em);
    }
}
