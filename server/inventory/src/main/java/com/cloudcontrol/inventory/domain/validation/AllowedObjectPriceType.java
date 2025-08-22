package com.cloudcontrol.inventory.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllowedObjectPriceTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedObjectPriceType {

    String message() default "Price type must be either WHOLESALE or RETAIL";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
