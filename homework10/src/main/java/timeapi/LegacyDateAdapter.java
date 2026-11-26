package timeapi;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class LegacyDateAdapter {

    public static Instant toInstant(Date date) {
        Objects.requireNonNull(date, "date не должна быть null");
        return date.toInstant();
    }

    public static Date toDate(Instant instant) {
        Objects.requireNonNull(instant, "instant не должен быть null");
        return Date.from(instant);
    }

    public static ZonedDateTime toZonedDateTime(GregorianCalendar calendar) {
        Objects.requireNonNull(calendar, "calendar не должен быть null");
        return calendar.toZonedDateTime();
    }

    public static GregorianCalendar toGregorianCalendar(ZonedDateTime dateTime) {
        Objects.requireNonNull(dateTime, "dateTime не должен быть null");

        return GregorianCalendar.from(dateTime);
    }
}
