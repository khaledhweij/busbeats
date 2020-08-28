package com.unundefined.busbeats;

import androidx.annotation.NonNull;

import java.time.LocalDate;

class Date {
    private LocalDate localDate;

    Date(int year, int month, int day) {
        this.localDate = LocalDate.of(year, month, day);
    }

    public static Date toDate(String date) {
        // TODO
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return localDate.toString();
    }
}