package com.example.boardroombookingsystem.model;

import java.util.Optional;

/**
 * Constructor. 3 overloading constructors
 * 1. With argument of type T indicating success and the return value. Status is set to success, message to empty string and value to the argument.
 * 2. With status and message. value set to empty optional. Status and message set to corresponding arguments.
 * 3. No arguments. Implies success with no return value. status set to success, message to empty string and value to empty Optional.
 */
public class Result<T> {
    private final Status status;
    private final String message;
    private final Optional<T> value;

    public Result() {
        this.status = Status.SUCCESS;
        this.message = "";
        this.value = Optional.empty();
    }

    public Result(final Optional<T> value) {
        this.status = Status.SUCCESS;
        this.message = "";
        this.value = value;
    }

    public Result(final Status status, final String message) {
        this.status = status;
        this.message = message;
        this.value = Optional.empty();
    }

    public Result(final Status status, final String message, final Optional<T> value) {
        this.status = status;
        this.message = message;
        this.value = value;
    }

    public Status getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public Optional<T> getValue() {
        return this.value;
    }
}
