package com.harri.training1.exceptions;

public class InvoiceNotExistException extends RuntimeException{
    public InvoiceNotExistException(String message) {
        super(message);
    }
}
