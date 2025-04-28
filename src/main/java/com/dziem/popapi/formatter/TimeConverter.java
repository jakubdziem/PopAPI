package com.dziem.popapi.formatter;


public class TimeConverter {
    public static String convertSecondsToTime(Long totalSeconds) {
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        return (hours < 10 ? "0" + hours  : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }
    public static String differenceOfTime(String time1, String time2) {
        Long time1Seconds = getSeconds(time1.split(":"));
        Long time2Seconds = getSeconds(time2.split(":"));
        return time1Seconds - time2Seconds > 0 ? convertSecondsToTime(time1Seconds - time2Seconds) : "00:00:00";
    }
    public static String sumOfTime(String time1, String time2) {
        Long time1Seconds = getSeconds(time1.split(":"));
        Long time2Seconds = getSeconds(time2.split(":"));
        return convertSecondsToTime(time1Seconds + time2Seconds);
    }

    public static Long getSeconds(String[] split1) {
        long hours = split1[0].charAt(0) == 0 ? Integer.parseInt(String.valueOf(split1[0].charAt(1))) : Integer.parseInt(split1[0]) * 3600L;
        int minutes = split1[1].charAt(0) == 0 ? Integer.parseInt(String.valueOf(split1[1].charAt(1))) : Integer.parseInt(split1[1]) * 60;
        int seconds = split1[2].charAt(0) == 0 ? Integer.parseInt(String.valueOf(split1[2].charAt(1))) : Integer.parseInt(split1[2]);
        return hours + minutes + seconds;
    }
}
