package fr.galaxio.portalevent.utils;

import fr.galaxio.portalevent.Config;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class CheckTime {
    public static boolean checkTime(int openDay, int openHour, int openMinute, int closeDay, int closeHour, int closeMinute) {
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfWeek().getValue();
        int hour = now.getHour();
        int minute = now.getMinute();

        int nowTotal = day*24*60 + hour*60 + minute;
        int openTotal = openDay*24*60 + openHour*60 + openMinute;
        int closeTotal = closeDay*24*60 + closeHour*60 + closeMinute;

        return nowTotal >= openTotal && nowTotal < closeTotal;
    }

    public static long getNextOpenTimestamp(PortalType portal) {
        int day = (portal == PortalType.NETHER ? Config.NETHER_OPEN_DAY.get() : Config.END_OPEN_DAY.get());
        int hour = (portal == PortalType.NETHER ? Config.NETHER_OPEN_HOUR.get() : Config.END_OPEN_HOUR.get());
        int minute = (portal == PortalType.NETHER ? Config.NETHER_OPEN_MINUTE.get() : Config.END_OPEN_MINUTE.get());

        return getNextTimestamp(day, hour, minute);
    }

    public static long getNextCloseTimestamp(PortalType portal) {
        int day = (portal == PortalType.NETHER ? Config.NETHER_CLOSE_DAY.get() : Config.END_CLOSE_DAY.get());
        int hour = (portal == PortalType.NETHER ? Config.NETHER_CLOSE_HOUR.get() : Config.END_CLOSE_HOUR.get());
        int minute = (portal == PortalType.NETHER ? Config.NETHER_CLOSE_MINUTE.get() : Config.END_CLOSE_MINUTE.get());

        return getNextTimestamp(day, hour, minute);
    }

    private static long getNextTimestamp(int targetDay, int targetHour, int targetMinute) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime target = now
                .with(DayOfWeek.of(targetDay))
                .withHour(targetHour)
                .withMinute(targetMinute)
                .withSecond(0)
                .withNano(0);

        if (target.isBefore(now)) {
            target = target.plusWeeks(1);
        }

        return target.toInstant().toEpochMilli();
    }

    public static String formatRemaining(long ms) {
        long hours = ms / 3600000;
        long minutes = (ms % 3600000) / 60000;
        long seconds = (ms % 60000) / 1000;

        return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
    }
}
