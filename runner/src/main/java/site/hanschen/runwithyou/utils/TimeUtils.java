package site.hanschen.runwithyou.utils;

import java.util.TimeZone;

/**
 * @author HansChen
 */
public class TimeUtils {

    private static final int  SECONDS_IN_DAY = 60 * 60 * 24;
    private static final long MILLIS_IN_DAY  = 1000L * SECONDS_IN_DAY;

    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
        final long interval = ms1 - ms2;
        return (interval < MILLIS_IN_DAY) && (interval > -1L * MILLIS_IN_DAY) && (toDay(ms1) == toDay(ms2));
    }

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }
}
