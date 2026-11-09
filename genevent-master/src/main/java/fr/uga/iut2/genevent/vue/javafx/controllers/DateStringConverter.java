package fr.uga.iut2.genevent.vue.javafx.controllers;

import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @author theo
 * @project genevent
 * @date 16/06/2026 14:30
 */
public class DateStringConverter extends StringConverter<LocalDate> {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Runnable onUpdate, onError;

    public DateStringConverter(Runnable onUpdate, Runnable onError) {
        this.onUpdate = onUpdate;
        this.onError = onError;
    }

    @Override
    public String toString(LocalDate date) {
        if (date != null) {
            return FORMATTER.format(date);
        }

        return "";
    }

    @Override
    public LocalDate fromString(String string) {
        if (string != null && !string.trim().isEmpty()) {
            try {
                LocalDate parsed = LocalDate.parse(string, FORMATTER);
                onUpdate.run();
                return parsed;
            } catch (DateTimeParseException e) {
                onError.run();
                return null;
            }
        }

        return null;
    }
}
