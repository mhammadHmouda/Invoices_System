package com.harri.training1.handler;


import com.harri.training1.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(LogsException.class)
    public ResponseEntity<?> handleLogs(LogsException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    @ExceptionHandler(UserFoundException.class)
    public ResponseEntity<?> handleUserFound(UserFoundException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<?> handleLoginFailedException(LoginFailedException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
    @ExceptionHandler(NoInvoicesForThisPageException.class)
    public ResponseEntity<?> handleNoInvoicesForThisPage(NoInvoicesForThisPageException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(InvoiceNotExistException.class)
    public ResponseEntity<?> handleInvoiceNotExist(InvoiceNotExistException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(InvoiceNotAddedException.class)
    public ResponseEntity<?> handleInvoiceNotAdded(InvoiceNotAddedException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong when try add invoice!");
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong: " + e.getMessage());
    }


}