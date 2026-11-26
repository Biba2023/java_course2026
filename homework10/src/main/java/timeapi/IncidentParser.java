package timeapi;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class IncidentParser {


    public static Incident parse(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Строка не должна быть null");
        }


        String[] parts = line.split(";", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException(
                    "Ожидалось 4 поля, а получено " + parts.length + ": " + line);
        }

        String id = parts[0].trim();
        String service = parts[1].trim();
        String started = parts[2].trim();
        String resolved = parts[3].trim();

        if (id.isEmpty()) {
            throw new IllegalArgumentException("Пустой id в строке: " + line);
        }
        if (service.isEmpty()) {
            throw new IllegalArgumentException("Пустое название сервиса в строке: " + line);
        }
        if (started.isEmpty() || resolved.isEmpty()) {
            throw new IllegalArgumentException("Пустая метка времени в строке: " + line);
        }

        Instant startedAt = parseInstant(started);
        Instant resolvedAt = parseInstant(resolved);


        return new Incident(id, service, startedAt, resolvedAt);
    }

    private static Instant parseInstant(String text) {
        try {
            return Instant.parse(text);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверная метка времени: " + text, e);
        }
    }
}
