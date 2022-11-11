package com.mastercard.dis.mids.reference.example;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.IDPClaim;
import org.openapitools.client.model.IDPIndividualClaim;
import org.openapitools.client.model.IDPScopesAuthorization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mastercard.dis.mids.reference.constants.Constants.COUNTRY_CODE;
import static com.mastercard.dis.mids.reference.constants.Constants.LOCALE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IDPScopesAuthorizationExample {

    public static IDPScopesAuthorization getIDPScopesAuthorizationRequestData() {
        IDPScopesAuthorization requestData = new IDPScopesAuthorization();

        List<IDPClaim> claims = new ArrayList<>();

        IDPClaim claim1 = new IDPClaim();
        claim1.setClaim("legalName:1:365");
        IDPIndividualClaim singleClaim1 = new IDPIndividualClaim();
        singleClaim1.setName("legalName");
        singleClaim1.setValue("TestLegalName");

        claim1.setValues(Collections.singletonList(singleClaim1));
        claims.add(claim1);

        requestData.setClaims(claims);
        requestData.setUserConsent(IDPScopesAuthorization.UserConsentEnum.ACCEPT);
        requestData.setCountryCode(COUNTRY_CODE);
        requestData.setLocale(LOCALE);
        return requestData;
    }
}
