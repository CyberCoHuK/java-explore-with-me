package ru.practicum.ewm_service.exceptions;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(final String message) {
        super(message);
    }
}
