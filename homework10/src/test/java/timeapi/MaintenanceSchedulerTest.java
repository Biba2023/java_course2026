package timeapi;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class MaintenanceSchedulerTest {

    private final ZoneId berlin = ZoneId.of("Europe/Berlin");
    private final Clock clock = Clock.fixed(Instant.parse("2024-03-30T00:00:00Z"), ZoneOffset.UTC);
    private final MaintenanceScheduler scheduler = new MaintenanceScheduler(clock);

    @Test
    void gapIsRejected() {
        assertThrows(DateTimeException.class, () -> scheduler.nextWindow(
                LocalDate.of(2024, 3, 31), LocalTime.of(2, 30),
                Duration.ofMinutes(30), berlin, OverlapPolicy.EARLIER));
    }

    @Test
    void overlapEarlierAndLater() {
        LocalDate date = LocalDate.of(2024, 10, 27);
        LocalTime time = LocalTime.of(2, 30);
        Duration duration = Duration.ofMinutes(30);

        MaintenanceWindow earlier = scheduler.nextWindow(
                date, time, duration, berlin, OverlapPolicy.EARLIER);
        MaintenanceWindow later = scheduler.nextWindow(
                date, time, duration, berlin, OverlapPolicy.LATER);

        assertEquals(ZoneOffset.ofHours(2), earlier.start().atZone(berlin).getOffset());
        assertEquals(ZoneOffset.ofHours(1), later.start().atZone(berlin).getOffset());

        assertEquals(Instant.parse("2024-10-27T00:30:00Z"), earlier.start());
        assertEquals(Instant.parse("2024-10-27T01:30:00Z"), later.start());
        assertEquals(Duration.ofHours(1), Duration.between(earlier.start(), later.start()));
    }

    @Test
    void normalWindowEndIsStartPlusDuration() {
        MaintenanceWindow window = scheduler.nextWindow(
                LocalDate.of(2024, 3, 30), LocalTime.of(3, 30),
                Duration.ofMinutes(30), berlin, OverlapPolicy.EARLIER);

        assertEquals(Instant.parse("2024-03-30T02:30:00Z"), window.start());
        assertEquals(Instant.parse("2024-03-30T03:00:00Z"), window.end());
        assertEquals(Duration.ofMinutes(30), window.duration());
    }

    @Test
    void twoDailyWindowsStartAtSameLocalTimeButDifferBy23Hours() {
        List<MaintenanceWindow> windows = scheduler.upcomingWindows(
                2, LocalTime.of(3, 30), Duration.ofMinutes(30),
                berlin, OverlapPolicy.EARLIER);

        assertEquals(2, windows.size());

        ZonedDateTime first = windows.get(0).start().atZone(berlin);
        ZonedDateTime second = windows.get(1).start().atZone(berlin);

        assertEquals(LocalDate.of(2024, 3, 30), first.toLocalDate());
        assertEquals(LocalDate.of(2024, 3, 31), second.toLocalDate());
        assertEquals(LocalTime.of(3, 30), first.toLocalTime());
        assertEquals(LocalTime.of(3, 30), second.toLocalTime());

        assertEquals(Duration.ofHours(23),
                Duration.between(windows.get(0).start(), windows.get(1).start()));
    }

    @Test
    void sameClockGivesSameResult() {
        List<MaintenanceWindow> first = scheduler.upcomingWindows(
                2, LocalTime.of(3, 30), Duration.ofMinutes(30),
                berlin, OverlapPolicy.EARLIER);
        List<MaintenanceWindow> second = scheduler.upcomingWindows(
                2, LocalTime.of(3, 30), Duration.ofMinutes(30),
                berlin, OverlapPolicy.EARLIER);

        assertEquals(first, second);
    }

    @Test
    void zeroDaysGivesEmptyList() {
        List<MaintenanceWindow> windows = scheduler.upcomingWindows(
                0, LocalTime.of(3, 30), Duration.ofMinutes(30),
                berlin, OverlapPolicy.EARLIER);
        assertTrue(windows.isEmpty());
    }

    @Test
    void negativeDaysIsError() {
        assertThrows(IllegalArgumentException.class, () -> scheduler.upcomingWindows(
                -1, LocalTime.of(3, 30), Duration.ofMinutes(30),
                berlin, OverlapPolicy.EARLIER));
    }

    @Test
    void durationMustBePositive() {
        assertThrows(IllegalArgumentException.class, () -> scheduler.nextWindow(
                LocalDate.of(2024, 3, 30), LocalTime.of(3, 30),
                Duration.ZERO, berlin, OverlapPolicy.EARLIER));
        assertThrows(IllegalArgumentException.class, () -> scheduler.nextWindow(
                LocalDate.of(2024, 3, 30), LocalTime.of(3, 30),
                Duration.ofMinutes(-5), berlin, OverlapPolicy.EARLIER));
    }

    @Test
    void nullArgumentsAreRejected() {
        assertThrows(NullPointerException.class, () -> new MaintenanceScheduler(null));
        assertThrows(NullPointerException.class, () -> scheduler.nextWindow(
                null, LocalTime.of(3, 30), Duration.ofMinutes(30),
                berlin, OverlapPolicy.EARLIER));
        assertThrows(NullPointerException.class, () -> scheduler.upcomingWindows(
                1, LocalTime.of(3, 30), Duration.ofMinutes(30), null, OverlapPolicy.EARLIER));
    }
}
