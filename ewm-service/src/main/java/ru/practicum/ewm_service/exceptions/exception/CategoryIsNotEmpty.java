package ru.practicum.ewm_service.exceptions.exception;

public class CategoryIsNotEmpty extends RuntimeException {
    public CategoryIsNotEmpty(String message) {
        super(message);
    }
}
