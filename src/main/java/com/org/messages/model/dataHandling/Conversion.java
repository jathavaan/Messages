package com.org.messages.model.dataHandling;

import java.time.LocalDateTime;

public class Conversion {
    public static String dateToString(LocalDateTime date) {
        if (date == null) throw new IllegalArgumentException("Date cannot be null");

        String hour = null;
        String minute = null;

        // Converts hour
        if (date.getHour() < 10) {
            hour = "0" + date.getHour();
        } else {
            hour = String.valueOf(date.getHour());
        }

        // Converts minute
        if (date.getMinute() < 10) {
            minute = "0" + date.getMinute();
        } else {
            minute = String.valueOf(date.getMinute());
        }

        return hour + ":" + minute;
    }
}
