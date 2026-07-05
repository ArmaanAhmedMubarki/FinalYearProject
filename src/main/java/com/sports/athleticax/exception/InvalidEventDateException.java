package com.sports.athleticax.exception;

public class InvalidEventDateException extends RuntimeException
{
    public InvalidEventDateException(String message)
    {
        super(message);
    }

    public InvalidEventDateException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
