package com.mastercard.dis.mids.reference.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants{

    public static final String MIDS_TENANT = "MIDS";
    public static final String X_ENCRYPTED_HEADER ="X-Encrypted-Payload";
    public static final String COUNTRY_CODE = "US";
    public static final String LOCALE = "en-US";
    public static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}$");
}
