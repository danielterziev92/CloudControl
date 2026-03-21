package com.cloudcontrol.catalog.domain.counterparty.vo;

import com.cloudcontrol.catalog.domain.counterparty.rule.PinRules;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Bulgarian personal identification number (ЕГН — Единен граждански номер).
 *
 * <p>Validates only that the value is exactly 10 digits.
 * Accepts {@link PinRules.Bulgarian#ANONYMOUS_VALUE} ("9999999999") as a
 * sentinel when the person declines to provide their PIN.</p>
 *
 * @see com.cloudcontrol.catalog.domain.counterparty.rule.PinRules.Bulgarian for structure, weights, and message codes.
 */
public record BulgarianPin(String value) implements PersonalIdentificationNumber {

    private static final Pattern DIGITS_ONLY = Pattern.compile("\\d{10}");

    public BulgarianPin {
        if (value == null || value.isBlank())
            throw new InvalidValueException(PinRules.Bulgarian.BLANK);

        if (!DIGITS_ONLY.matcher(value).matches())
            throw new InvalidValueException(PinRules.Bulgarian.INVALID_FORMAT, value);
    }

    @Override
    public @NonNull String countryCode() {
        return PinRules.Bulgarian.COUNTRY_CODE;
    }

    @Override
    public boolean isAnonymous() {
        return PinRules.Bulgarian.ANONYMOUS_VALUE.equals(value);
    }

    /**
     * Attempts to extract the birthdate encoded in the ЕГН.
     * Returns {@link Optional#empty()} for anonymous PINs.
     */
    public Optional<LocalDate> birthDate() {
        if (isAnonymous()) return Optional.empty();

        int yy = Integer.parseInt(value.substring(0, 2));
        int mm = Integer.parseInt(value.substring(2, 4));
        int dd = Integer.parseInt(value.substring(4, 6));

        int year;
        if (mm > PinRules.Bulgarian.MONTH_OFFSET_2000) {
            year = 2000 + yy;
            mm -= PinRules.Bulgarian.MONTH_OFFSET_2000;
        } else if (mm > PinRules.Bulgarian.MONTH_OFFSET_1800) {
            year = 1800 + yy;
            mm -= PinRules.Bulgarian.MONTH_OFFSET_1800;
        } else {
            year = 1900 + yy;
        }

        return Optional.of(LocalDate.of(year, mm, dd));
    }
}
