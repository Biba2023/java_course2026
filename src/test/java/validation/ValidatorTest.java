package validation;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ValidatorTest {

    static class GoodUser {

        @NotNull
        @Size(min = 2, max = 20)
        private String name = "Alex";

        @NotNull
        @Email
        private String email = "alex@mail.com";

        @Range(min = 0, max = 120)
        private Integer age = 20;
    }

    static class BadUser {

        @NotNull
        private String name;

        @Size(min = 2, max = 5)
        private String nickname = "A";

        @Range(min = 1, max = 10)
        private int age = 20;

        @Email
        private String email = "wrong";
    }

    static class Parent {

        @NotNull
        private String parentName = null;
    }

    static class Child extends Parent {
        private static String ignored = null;
    }

    static class WrongType {

        @Size(min = 1, max = 3)
        private Integer number = null;
    }

    @Test
    void goodObjectHasNoErrors() {
        ValidationResult result = Validator.validate(new GoodUser());

        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void collectsAllErrors() {
        ValidationResult result = Validator.validate(new BadUser());

        assertFalse(result.isValid());
        assertEquals(4, result.getErrors().size());
    }

    @Test
    void checksParentFields() {
        ValidationResult result = Validator.validate(new Child());

        assertEquals(1, result.getErrors().size());
        assertEquals("parentName",
                result.getErrors().get(0).fieldName());
    }

    @Test
    void nullObjectIsNotAllowed() {
        assertThrows(NullPointerException.class,
                () -> Validator.validate(null));
    }

    @Test
    void unsupportedTypeIsConfigurationError() {
        assertThrows(ValidationConfigurationException.class,
                () -> Validator.validate(new WrongType()));
    }

    @Test
    void errorsListCannotBeChanged() {
        ValidationResult result = new ValidationResult(List.of());

        assertThrows(UnsupportedOperationException.class,
                () -> result.getErrors().add(
                        new ValidationError("name", "NotNull", "Error")));
    }

    @Test
    void sizeBoundariesAreAllowed() {
        class Example {
            @Size(min = 2, max = 4)
            private String text = "ABCD";
        }

        assertTrue(Validator.validate(new Example()).isValid());
    }

    @Test
    void rangeBoundariesAreAllowed() {
        class Example {
            @Range(min = 1, max = 5)
            private int number = 5;
        }

        assertTrue(Validator.validate(new Example()).isValid());
    }
}
