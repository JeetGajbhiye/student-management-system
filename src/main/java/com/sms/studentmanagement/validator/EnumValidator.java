package com.sms.studentmanagement.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Set<String> acceptedValues;
    private boolean ignoreCase;

    @Override
    public void initialize(ValidEnum annotation) {
        ignoreCase = annotation.ignoreCase();
        acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(e -> ignoreCase ? e.name().toUpperCase() : e.name())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;
        String check = ignoreCase ? value.toUpperCase() : value;
        boolean valid = acceptedValues.contains(check);
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Invalid value '" + value + "'. Accepted: " + acceptedValues
            ).addConstraintViolation();
        }
        return valid;
    }
}
