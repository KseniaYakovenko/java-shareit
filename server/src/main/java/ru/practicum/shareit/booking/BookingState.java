package ru.practicum.shareit.booking;

import java.util.Arrays;

public enum BookingState {
    ALL,
    CURRENT,
    FUTURE,
    PAST,
    REJECTED,
    WAITING;

    public static BookingState from(String state) {
        return Arrays.stream(BookingState.values())
                .filter(value -> value.name().equals(state)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
    }
}