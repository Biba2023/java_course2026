package timeapi;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class IncidentAnalyticsTest {

    private final Incident inc1 = IncidentParser.parse(
            "INC-1;billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z"); // 150 минут
    private final Incident inc2 = IncidentParser.parse(
            "INC-2;gateway;2024-03-30T23:30:00Z;2024-03-31T00:15:00Z"); // 45 минут

    @Test
    void totalDowntime() {
        Duration total = IncidentAnalytics.totalDowntime(List.of(inc1, inc2));
        assertEquals(Duration.ofMinutes(195), total);
    }

    @Test
    void averageDowntimeRoundsDown() {

        Instant start = Instant.parse("2024-01-01T10:00:00Z");
        Incident a = new Incident("A", "svc", start, start.plusSeconds(1));
        Incident b = new Incident("B", "svc", start, start.plusSeconds(1).plusNanos(1));


        assertEquals(Duration.ofSeconds(2, 1), IncidentAnalytics.totalDowntime(List.of(a, b)));
        assertEquals(Duration.ofSeconds(1), IncidentAnalytics.averageDowntime(List.of(a, b)));
    }

    @Test
    void averageDowntimeNormalCase() {

        Duration avg = IncidentAnalytics.averageDowntime(List.of(inc1, inc2));
        assertEquals(Duration.ofMinutes(97).plusSeconds(30), avg);
    }

    @Test
    void longestIncident() {
        Optional<Incident> longest = IncidentAnalytics.longestIncident(List.of(inc2, inc1));
        assertTrue(longest.isPresent());
        assertEquals("INC-1", longest.get().id());
    }

    @Test
    void longestIncidentTieTakesEarlierStart() {
        Instant t1 = Instant.parse("2024-01-01T10:00:00Z");
        Instant t2 = Instant.parse("2024-01-02T10:00:00Z");
        Incident later = new Incident("A", "svc", t2, t2.plusSeconds(60));
        Incident earlier = new Incident("Z", "svc", t1, t1.plusSeconds(60));

        Incident result = IncidentAnalytics.longestIncident(List.of(later, earlier)).get();
        assertEquals("Z", result.id());
    }

    @Test
    void longestIncidentTieTakesSmallerId() {
        Instant t = Instant.parse("2024-01-01T10:00:00Z");
        Incident b = new Incident("B", "svc", t, t.plusSeconds(60));
        Incident a = new Incident("A", "svc", t, t.plusSeconds(60));

        Incident result = IncidentAnalytics.longestIncident(List.of(b, a)).get();
        assertEquals("A", result.id());
    }

    @Test
    void emptyList() {
        List<Incident> empty = new ArrayList<>();
        assertEquals(Duration.ZERO, IncidentAnalytics.totalDowntime(empty));
        assertEquals(Duration.ZERO, IncidentAnalytics.averageDowntime(empty));
        assertEquals(Optional.empty(), IncidentAnalytics.longestIncident(empty));
        assertTrue(IncidentAnalytics.groupByStartDate(empty, ZoneId.of("UTC")).isEmpty());
    }

    @Test
    void groupByStartDateInBerlin() {
        ZoneId berlin = ZoneId.of("Europe/Berlin");

        Map<LocalDate, List<Incident>> grouped =
                IncidentAnalytics.groupByStartDate(List.of(inc2, inc1), berlin);

        assertEquals(2, grouped.size());
        assertEquals(List.of(inc1), grouped.get(LocalDate.of(2024, 3, 30)));
        assertEquals(List.of(inc2), grouped.get(LocalDate.of(2024, 3, 31)));
    }

    @Test
    void groupByStartDateInUtcPutsBothInOneDay() {

        Map<LocalDate, List<Incident>> grouped =
                IncidentAnalytics.groupByStartDate(List.of(inc1, inc2), ZoneId.of("UTC"));

        assertEquals(1, grouped.size());
        assertEquals(List.of(inc1, inc2), grouped.get(LocalDate.of(2024, 3, 30)));
    }

    @Test
    void groupedDatesAndIncidentsAreSorted() {
        Instant day1 = Instant.parse("2024-05-01T10:00:00Z");
        Instant day2 = Instant.parse("2024-05-02T10:00:00Z");
        Incident a = new Incident("A", "svc", day1, day1.plusSeconds(10));
        Incident b = new Incident("B", "svc", day1, day1.plusSeconds(10));
        Incident c = new Incident("C", "svc", day2, day2.plusSeconds(10));

        Map<LocalDate, List<Incident>> grouped =
                IncidentAnalytics.groupByStartDate(List.of(c, b, a), ZoneId.of("UTC"));

        assertEquals(List.of(LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 2)),
                new ArrayList<>(grouped.keySet()));
        assertEquals(List.of(a, b), grouped.get(LocalDate.of(2024, 5, 1)));
    }

    @Test
    void doesNotChangeInputList() {
        List<Incident> input = new ArrayList<>(List.of(inc2, inc1));
        IncidentAnalytics.groupByStartDate(input, ZoneId.of("UTC"));
        assertEquals(List.of(inc2, inc1), input);
    }
}
