package timeapi;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record MaintenanceWindow(Instant start, Instant end) {

    public MaintenanceWindow {
        Objects.requireNonNull(start, "start не должен быть null");
        Objects.requireNonNull(end, "end не должен быть null");

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Окончание окна не может быть раньше начала");
        }
    }

    public Duration duration() {
        return Duration.between(start, end);
    }
}
