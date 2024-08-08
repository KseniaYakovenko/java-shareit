package ru.practicum.shareit.booking;

import java.util.Arrays;

public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED;

    public static BookingStatus from(String status) {
        return Arrays.stream(BookingStatus.values())
                .filter(value -> value.name().equals(status)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + status));
    }
}