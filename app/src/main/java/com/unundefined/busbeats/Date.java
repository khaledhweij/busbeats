package com.unundefined.busbeats;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;

class Date {
    private LocalDate localDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
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