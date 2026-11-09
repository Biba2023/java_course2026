package validation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Validator {

    private Validator() {
    }

    public static ValidationResult validate(Object object) {

        Objects.requireNonNull(object);

        List<ValidationError> errors = new ArrayList<>();

        Class<?> currentClass = object.getClass();

        while (currentClass != null && currentClass != Object.class) {

            Field[] fields = currentClass.getDeclaredFields();

            for (Field field : fields) {

                if (Modifier.isStatic(field.getModifiers())
                        || field.isSynthetic()) {
                    continue;
                }

                checkField(object, field, errors);
            }

            currentClass = currentClass.getSuperclass();
        }

        return new ValidationResult(errors);
    }

    private static void checkField(
            Object object,
            Field field,
            List<ValidationError> errors
    ) {

        NotNull notNull = field.getAnnotation(NotNull.class);
        Size size = field.getAnnotation(Size.class);
        Range range = field.getAnnotation(Range.class);
        Email email = field.getAnnotation(Email.class);

        if (size != null) {
            checkSizeSettings(field, size);
        }

        if (range != null) {
            checkRangeSettings(field, range);
        }

        if (email != null &&
                !CharSequence.class.isAssignableFrom(field.getType())) {
            throw configError(field, "Wrong Email field type");
        }

        if (!field.trySetAccessible()) {
            throw configError(field, "Field is not accessible");
        }

        Object value;

        try {
            value = field.get(object);
        } catch (IllegalAccessException e) {
            throw new ValidationConfigurationException(
                    "Cannot read field: " + field.getName(), e
            );
        }

        if (notNull != null && value == null
                && !field.getType().isPrimitive()) {

            errors.add(new ValidationError(
                    field.getName(), "NotNull", notNull.message()
            ));
        }

        if (size != null && value != null) {

            String text = value.toString();

            if (text.length() < size.min()
                    || text.length() > size.max()) {

                errors.add(new ValidationError(
                        field.getName(), "Size", size.message()
                ));
            }
        }

        if (range != null && value != null) {

            double number = ((Number) value).doubleValue();

            if (Double.isNaN(number)
                    || number < range.min()
                    || number > range.max()) {

                errors.add(new ValidationError(
                        field.getName(), "Range", range.message()
                ));
            }
        }

        if (email != null && value != null) {

            String text = value.toString();

            if (!isEmailValid(text)) {

                errors.add(new ValidationError(
                        field.getName(), "Email", email.message()
                ));
            }
        }
    }

    private static void checkSizeSettings(Field field, Size size) {

        if (size.min() < 0 || size.min() > size.max()) {
            throw configError(field, "Wrong Size limits");
        }

        if (!CharSequence.class.isAssignableFrom(field.getType())) {
            throw configError(field, "Wrong Size field type");
        }
    }

    private static void checkRangeSettings(Field field, Range range) {

        if (Double.isNaN(range.min())
                || Double.isNaN(range.max())
                || Double.isInfinite(range.min())
                || Double.isInfinite(range.max())
                || range.min() > range.max()) {

            throw configError(field, "Wrong Range limits");
        }

        Class<?> type = field.getType();

        boolean isNumber =
                Number.class.isAssignableFrom(type)
                        || type == byte.class
                        || type == short.class
                        || type == int.class
                        || type == long.class
                        || type == float.class
                        || type == double.class;

        if (!isNumber) {
            throw configError(field, "Wrong Range field type");
        }
    }

    private static boolean isEmailValid(String text) {

        if (text.trim().isEmpty() || text.contains(" ")) {
            return false;
        }

        int at = text.indexOf('@');

        if (at <= 0 || at != text.lastIndexOf('@')
                || at == text.length() - 1) {
            return false;
        }

        String afterAt = text.substring(at + 1);

        return afterAt.contains(".")
                && !afterAt.startsWith(".")
                && !afterAt.endsWith(".");
    }

    private static ValidationConfigurationException configError(
            Field field,
            String message
    ) {

        return new ValidationConfigurationException(
                message + ": "
                        + field.getDeclaringClass().getName()
                        + "." + field.getName()
        );
    }
}