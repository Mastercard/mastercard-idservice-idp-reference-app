package com.mastercard.dis.mids.reference.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.IDPClaim;
import org.openapitools.client.model.IDPIndividualClaim;

import java.util.Collections;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    //    REGEX Constant
    public static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}$");
    //    Interceptor Constant
    public static final String MIDS_TENANT = "MIDS";
    //    Configuration Constant
    public static final String X_ENCRYPTED_HEADER = "X-Encrypted-Payload";
    //    Example Input Constants
    public static final String COUNTRY_CODE = "US";
    public static final String LOCALE = "en-US";

    /**
     * If you have more than one IDPClaim you should create new Input Claims in this class
     * and add it to the {@link
     * com.mastercard.dis.mids.reference.example.IDPScopesAuthorizationExample}
     */
    public static final IDPClaim CLAIM_1 = new IDPClaim();

    static {
        // First IPDClaim setup
        CLAIM_1.setClaim("legalName:1:365");
        IDPIndividualClaim singleClaim1 = new IDPIndividualClaim();
        singleClaim1.setName("legalName");
        singleClaim1.setValue("TestLegalName");
        CLAIM_1.setValues(Collections.singletonList(singleClaim1));
        // Insert other claims bellow
    }

}
