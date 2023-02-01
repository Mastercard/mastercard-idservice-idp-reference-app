package com.mastercard.dis.mids.reference.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    //    Interceptor Constant
    public static final String MIDS_TENANT = "MIDS";
    //    Configuration Constant
    public static final String X_ENCRYPTED_HEADER = "X-Encrypted-Payload";
    //    Example Input Constants
    public static final String COUNTRY_CODE = "US";
    public static final String LOCALE = "en-US";
    //    REGEX Constant
    public static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}$");
}
