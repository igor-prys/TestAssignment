package com.example.testassignment.validation;

import jakarta.validation.*;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = {NotBlankOrNullValidator.class})
@ReportAsSingleViolation
public @interface NotEmptyOrNull {
    String message() default "The value of field can not be empty string";
    String customMessage() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

class NotBlankOrNullValidator implements ConstraintValidator<NotEmptyOrNull, String> {
    private String customMessage;

    @Override
    public void initialize(NotEmptyOrNull constraintAnnotation) {
        this.customMessage = constraintAnnotation.customMessage();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null && value.isEmpty()) {
            return false;
        }
        return true;
    }
}

