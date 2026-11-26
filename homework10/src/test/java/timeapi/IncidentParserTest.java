package timeapi;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class IncidentParserTest {

    @Test
    void parsesCorrectLine() {
        Incident incident = IncidentParser.parse(
                "INC-1;billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z");

        assertEquals("INC-1", incident.id());
        assertEquals("billing", incident.service());
        assertEquals(Instant.parse("2024-03-30T22:30:00Z"), incident.startedAt());
        assertEquals(Instant.parse("2024-03-31T01:00:00Z"), incident.resolvedAt());
    }

    @Test
    void durationOfFirstExampleIs150Minutes() {
        Incident incident = IncidentParser.parse(
                "INC-1;billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z");

        assertEquals(Duration.ofMinutes(150), incident.duration());
    }

    @Test
    void rejectsWrongTimestamp() {
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(
                "INC-1;billing;вчера;2024-03-31T01:00:00Z"));
    }

    @Test
    void rejectsLocalDateTimeWithoutZ() {
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(
                "INC-1;billing;2024-03-30T22:30:00;2024-03-31T01:00:00Z"));
    }

    @Test
    void rejectsEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(
                ";billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z"));
    }

    @Test
    void rejectsEmptyService() {
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(
                "INC-1; ;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z"));
    }

    @Test
    void rejectsResolvedBeforeStarted() {
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(
                "INC-1;billing;2024-03-31T01:00:00Z;2024-03-30T22:30:00Z"));
    }

    @Test
    void rejectsWrongNumberOfFields() {
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(
                "INC-1;billing;2024-03-30T22:30:00Z"));
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(
                "INC-1;billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z;extra"));
    }

    @Test
    void rejectsNullLine() {
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(null));
    }
}
