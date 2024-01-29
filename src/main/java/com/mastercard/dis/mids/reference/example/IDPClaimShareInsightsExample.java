package com.mastercard.dis.mids.reference.example;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.client.model.IDPClaimShareInsights;
import org.openapitools.client.model.IDPClaimShareInsightsUserActivityOutcome;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IDPClaimShareInsightsExample {
    public static IDPClaimShareInsights getIDPClaimShareInsightEvents() {
        IDPClaimShareInsights claimEvent = new IDPClaimShareInsights();
        IDPClaimShareInsightsUserActivityOutcome idpClaimShareInsightsUserActivityOutcome = new IDPClaimShareInsightsUserActivityOutcome();
        idpClaimShareInsightsUserActivityOutcome.setOutcome("DECLINED_BY_SYSTEM_FRAUD");
        idpClaimShareInsightsUserActivityOutcome.setReason("SUSPICIOUS_DEVICE");

        claimEvent.setClaimShareDuration(30);
        claimEvent.setDeviceMake("iPhone 11");
        claimEvent.setUserActivity("NEW_ID_ENROLLED");
        claimEvent.setUserActivityOutcome(idpClaimShareInsightsUserActivityOutcome);
        return claimEvent;
    }
}
