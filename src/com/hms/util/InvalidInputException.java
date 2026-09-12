package com.hms.util;

/** Thrown for any validation failure in service-layer methods; keeps error handling explicit. */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
