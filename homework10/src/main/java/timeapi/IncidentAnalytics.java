package timeapi;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

public class IncidentAnalytics {

    public static Duration totalDowntime(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "Список инцидентов не должен быть null");

        Duration total = Duration.ZERO;
        for (Incident incident : incidents) {
            total = total.plus(incident.duration());
        }
        return total;
    }

    public static Duration averageDowntime(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "Список инцидентов не должен быть null");

        if (incidents.isEmpty()) {
            return Duration.ZERO;
        }

        BigInteger billion = BigInteger.valueOf(1_000_000_000L);
        BigInteger totalNanos = BigInteger.ZERO;
        for (Incident incident : incidents) {
            Duration d = incident.duration();
            BigInteger nanos = BigInteger.valueOf(d.getSeconds())
                    .multiply(billion)
                    .add(BigInteger.valueOf(d.getNano()));
            totalNanos = totalNanos.add(nanos);
        }


        BigInteger avgNanos = totalNanos.divide(BigInteger.valueOf(incidents.size()));

        BigInteger[] parts = avgNanos.divideAndRemainder(billion);
        return Duration.ofSeconds(parts[0].longValue(), parts[1].longValue());
    }

    public static Optional<Incident> longestIncident(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "Список инцидентов не должен быть null");

        Incident best = null;
        for (Incident incident : incidents) {
            if (best == null) {
                best = incident;
                continue;
            }

            int cmp = incident.duration().compareTo(best.duration());
            if (cmp > 0) {
                best = incident;
            } else if (cmp == 0) {

                int startCmp = incident.startedAt().compareTo(best.startedAt());
                if (startCmp < 0) {
                    best = incident;
                } else if (startCmp == 0 && incident.id().compareTo(best.id()) < 0) {
                    best = incident;
                }
            }
        }
        return Optional.ofNullable(best);
    }

    public static Map<LocalDate, List<Incident>> groupByStartDate(
            List<Incident> incidents, ZoneId zone) {
        Objects.requireNonNull(incidents, "Список инцидентов не должен быть null");
        Objects.requireNonNull(zone, "zone не должна быть null");

        Map<LocalDate, List<Incident>> result = new TreeMap<>();
        for (Incident incident : incidents) {
            LocalDate date = incident.startedAt().atZone(zone).toLocalDate();
            if (!result.containsKey(date)) {
                result.put(date, new ArrayList<>());
            }
            result.get(date).add(incident);
        }

        for (List<Incident> list : result.values()) {
            list.sort(Comparator.comparing(Incident::startedAt)
                    .thenComparing(Incident::id));
        }
        return result;
    }
}
