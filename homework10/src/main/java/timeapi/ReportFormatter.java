package timeapi;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class ReportFormatter {

    public static String formatWindow(MaintenanceWindow window, ZoneId zone, Locale locale) {
        Objects.requireNonNull(window, "window не должно быть null");
        Objects.requireNonNull(zone, "zone не должна быть null");
        Objects.requireNonNull(locale, "locale не должна быть null");

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm XXX (VV)", locale);

        String start = window.start().atZone(zone).format(formatter);
        String end = window.end().atZone(zone).format(formatter);
        return start + " – " + end;
    }
}
