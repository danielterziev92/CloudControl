package com.cloudcontrol.inventory.domain.validation;

import com.cloudcontrol.inventory.domain.enums.ProductPriceType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AllowedObjectPriceTypeValidator implements ConstraintValidator<AllowedObjectPriceType, ProductPriceType> {
    @Override
    public boolean isValid(ProductPriceType value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value == ProductPriceType.WHOLESALE || value == ProductPriceType.RETAIL;
    }
}
