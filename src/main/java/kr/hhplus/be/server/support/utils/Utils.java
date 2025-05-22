package kr.hhplus.be.server.support.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Utils {
    public static long DateTimeToMilli(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
