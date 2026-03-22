package com.cloudcontrol.catalog.domain.counterparty.vo;

import com.cloudcontrol.catalog.domain.counterparty.rule.PhoneNumberRules;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;

/**
 * Bulgarian phone number.
 *
 * <p>Accepts both local ({@code 08XXXXXXXX}) and E.164 ({@code +3598XXXXXXXX}) formats.
 * Always normalized to E.164 internally.</p>
 *
 * @see com.cloudcontrol.catalog.domain.counterparty.rule.PhoneNumberRules.Bulgarian for format rules and constraints.
 */
public record BulgarianPhoneNumber(String value) implements PhoneNumber {
    public BulgarianPhoneNumber {
        if (value == null || value.isBlank())
            throw new InvalidValueException(PhoneNumberRules.Bulgarian.BLANK);

        boolean isLocal = PhoneNumberRules.Bulgarian.LOCAL_PATTERN.matcher(value).matches();
        boolean isE164 = PhoneNumberRules.Bulgarian.E164_PATTERN.matcher(value).matches();

        if (!isLocal && !isE164)
            throw new InvalidValueException(PhoneNumberRules.Bulgarian.INVALID, value);
    }

    @Override
    public String e164() {
        if (value.startsWith("0"))
            return PhoneNumberRules.Bulgarian.COUNTRY_PREFIX + value.substring(1);
        return value; // already E.164
    }

    @Override
    public String countryCode() {
        return PhoneNumberRules.Bulgarian.COUNTRY_CODE;
    }
}
