package timeapi;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneOffsetTransition;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MaintenanceScheduler {

    private final Clock clock;

    public MaintenanceScheduler(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock не должен быть null");
    }

    public MaintenanceWindow nextWindow(
            LocalDate date, LocalTime localStart, Duration duration,
            ZoneId zone, OverlapPolicy policy) {
        Objects.requireNonNull(date, "date не должна быть null");
        Objects.requireNonNull(localStart, "localStart не должно быть null");
        Objects.requireNonNull(duration, "duration не должна быть null");
        Objects.requireNonNull(zone, "zone не должна быть null");
        Objects.requireNonNull(policy, "policy не должна быть null");

        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("Длительность окна должна быть положительной");
        }

        LocalDateTime localDateTime = date.atTime(localStart);

        ZoneOffsetTransition transition = zone.getRules().getTransition(localDateTime);
        if (transition != null && transition.isGap()) {
            throw new DateTimeException("Локальное время " + localDateTime
                    + " не существует в зоне " + zone);
        }

        ZonedDateTime zoned = ZonedDateTime.of(localDateTime, zone);

        if (policy == OverlapPolicy.EARLIER) {
            zoned = zoned.withEarlierOffsetAtOverlap();
        } else {
            zoned = zoned.withLaterOffsetAtOverlap();
        }

        Instant start = zoned.toInstant();
        Instant end = start.plus(duration);
        return new MaintenanceWindow(start, end);
    }

    public List<MaintenanceWindow> upcomingWindows(
            int days, LocalTime localStart, Duration duration,
            ZoneId zone, OverlapPolicy policy) {
        if (days < 0) {
            throw new IllegalArgumentException("days не может быть отрицательным");
        }
        Objects.requireNonNull(localStart, "localStart не должно быть null");
        Objects.requireNonNull(duration, "duration не должна быть null");
        Objects.requireNonNull(zone, "zone не должна быть null");
        Objects.requireNonNull(policy, "policy не должна быть null");

        List<MaintenanceWindow> result = new ArrayList<>();
        LocalDate today = LocalDate.now(clock.withZone(zone));

        for (int i = 0; i < days; i++) {
            LocalDate date = today.plusDays(i);
            result.add(nextWindow(date, localStart, duration, zone, policy));
        }
        return result;
    }
}
