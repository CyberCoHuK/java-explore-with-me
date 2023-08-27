package ru.practicum.ewm_service.exceptions;

public class ObjectAlreadyExistException extends RuntimeException {
    public ObjectAlreadyExistException(final String message) {
        super(message);
    }
}
