package timeapi;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.Test;

class LegacyDateAdapterTest {

    @Test
    void instantToDateAndBack() {
        Instant instant = Instant.parse("2024-03-31T01:00:00.123Z");

        Date date = LegacyDateAdapter.toDate(instant);
        assertEquals(instant.toEpochMilli(), date.getTime());
        assertEquals(instant, LegacyDateAdapter.toInstant(date));
    }

    @Test
    void dateToInstantAndBack() {
        Date date = new Date(1_711_846_800_000L);

        Instant instant = LegacyDateAdapter.toInstant(date);
        assertEquals(1_711_846_800_000L, instant.toEpochMilli());
        assertEquals(date, LegacyDateAdapter.toDate(instant));
    }

    @Test
    void zonedDateTimeToCalendarAndBack() {
        ZoneId berlin = ZoneId.of("Europe/Berlin");
        ZonedDateTime original = ZonedDateTime.of(2024, 3, 31, 3, 0, 0, 0, berlin);

        GregorianCalendar calendar = LegacyDateAdapter.toGregorianCalendar(original);
        assertEquals(original.toInstant().toEpochMilli(), calendar.getTimeInMillis());
        assertEquals("Europe/Berlin", calendar.getTimeZone().getID());

        ZonedDateTime back = LegacyDateAdapter.toZonedDateTime(calendar);
        assertEquals(original.toInstant(), back.toInstant());
        assertEquals(berlin, back.getZone());
        assertEquals(original, back);
    }

    @Test
    void calendarToZonedDateTimeKeepsInstantAndZone() {
        GregorianCalendar calendar = new GregorianCalendar(
                java.util.TimeZone.getTimeZone("Europe/Berlin"));
        calendar.setTimeInMillis(1_711_846_800_000L);

        ZonedDateTime zdt = LegacyDateAdapter.toZonedDateTime(calendar);
        assertEquals(1_711_846_800_000L, zdt.toInstant().toEpochMilli());
        assertEquals(ZoneId.of("Europe/Berlin"), zdt.getZone());
    }
}
