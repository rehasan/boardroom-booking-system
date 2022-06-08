package com.example.boardroombookingsystem.helper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Converter {
    public static LocalDateTime stringToDateTime(String pattern, String dateTimeInString) {
        LocalDateTime localDateTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        try {
            localDateTime = LocalDateTime.parse(dateTimeInString, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        return localDateTime;
    }

    public static LocalTime stringToTime(String pattern, String timeInString) {
        LocalTime localTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        try {
            localTime = LocalTime.parse(timeInString, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        return localTime;
    }
}