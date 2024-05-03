package com.example.testassignment.validation;

import jakarta.validation.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = {PhoneValidator.class})
@ReportAsSingleViolation
public @interface Phone {
    String message() default "The phone number should have format: " +
            "+1234567890 or +123 456 7890." +
            "should have amount of digits from 6 to 14 after the + symbol. " +
            "Spaces are allowed after the digits.";
    String customMessage() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

class PhoneValidator implements ConstraintValidator<Phone, String> {
    private static final String PHONE_PATTERN = "^\\+(?:[0-9] ?){6,14}[0-9]$";
    private String customMessage;


    @Override
    public void initialize(Phone constraintAnnotation) {
        this.customMessage = constraintAnnotation.customMessage();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || "".equals(value)) {
            return true;
        }
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
