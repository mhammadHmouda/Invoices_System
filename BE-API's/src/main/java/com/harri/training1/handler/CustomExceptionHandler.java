package com.harri.training1.handler;


import com.harri.training1.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class responsible for handling the exception throws in the services layer to controller layer
 */
@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(IsValidPasswordException.class)
    public ResponseEntity<?> handlePassword(IsValidPasswordException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleArguments(MethodArgumentNotValidException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
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
    public ResponseEntity<?> handleInvoiceNotAdded(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong when try add invoice!");
    }
}
