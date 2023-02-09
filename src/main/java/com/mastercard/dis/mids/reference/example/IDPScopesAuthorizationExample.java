package com.mastercard.dis.mids.reference.example;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.IDPClaim;
import org.openapitools.client.model.IDPScopesAuthorization;

import java.util.ArrayList;
import java.util.List;

import static com.mastercard.dis.mids.reference.constants.Constants.CLAIM_1;
import static com.mastercard.dis.mids.reference.constants.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.LOCALE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IDPScopesAuthorizationExample {

    public static IDPScopesAuthorization getIDPScopesAuthorizationRequestData() {
        IDPScopesAuthorization requestData = new IDPScopesAuthorization();

        List<IDPClaim> claims = new ArrayList<>();

        claims.add(CLAIM_1);

        requestData.setClaims(claims);
        requestData.setUserConsent(IDPScopesAuthorization.UserConsentEnum.ACCEPT);
        requestData.setCountryCode(COUNTRY_CODE);
        requestData.setLocale(LOCALE);
        return requestData;
    }
}
