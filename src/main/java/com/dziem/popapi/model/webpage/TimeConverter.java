package com.dziem.popapi.model.webpage;

import lombok.Data;

@Data
public class TimeConverter {
    public static String convertSecondsToTime(Long totalSeconds) {
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        return (hours < 10 ? "0" + hours  : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }
}
