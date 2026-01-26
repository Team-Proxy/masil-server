package com.beyond.masilbe.common.util;

import java.time.Duration;
import java.time.Instant;

public class TimeAgoUtils {

    public static String toTimeAgo(Instant time) {
        Instant now = Instant.now();
        Duration duration = Duration.between(time, now);

        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) {
            return "방금 전";
        }
        if (minutes < 60) {
            return minutes + "분 전";
        }
        if (hours < 24) {
            return hours + "시간 전";
        }
        return days + "일 전";
    }

    private TimeAgoUtils() {}
}
