package com.algonet.algonetapi.controllers.advice;

import com.algonet.algonetapi.exceptions.AlreadyExistingUserException;
import com.algonet.algonetapi.exceptions.WrongAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomControllerAdvice {

    @ExceptionHandler(AlreadyExistingUserException.class)
    public ResponseEntity<?> conflict() {
        log.error("Already Existing User");
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(WrongAuthException.class)
    public ResponseEntity<?> unauthorized() {
        log.error("Bad Authentication");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}