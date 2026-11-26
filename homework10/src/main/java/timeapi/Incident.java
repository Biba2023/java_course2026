package timeapi;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record Incident(
        String id,
        String service,
        Instant startedAt,
        Instant resolvedAt) {

    public Incident {
        Objects.requireNonNull(id, "id не должен быть null");
        Objects.requireNonNull(service, "service не должен быть null");
        Objects.requireNonNull(startedAt, "startedAt не должен быть null");
        Objects.requireNonNull(resolvedAt, "resolvedAt не должен быть null");

        if (id.isBlank()) {
            throw new IllegalArgumentException("id не может быть пустым");
        }
        if (service.isBlank()) {
            throw new IllegalArgumentException("Название сервиса не может быть пустым");
        }
        if (resolvedAt.isBefore(startedAt)) {
            throw new IllegalArgumentException("Окончание не может быть раньше начала");
        }
    }

    public Duration duration() {
        return Duration.between(startedAt, resolvedAt);
    }
}
