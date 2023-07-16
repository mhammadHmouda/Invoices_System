package com.harri.training1.exceptions;

public class NoInvoicesForThisPageException extends RuntimeException {
    public NoInvoicesForThisPageException(String message) {
        super(message);
    }
}
