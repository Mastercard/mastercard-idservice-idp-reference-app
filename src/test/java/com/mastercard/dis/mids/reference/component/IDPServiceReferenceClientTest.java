package com.mastercard.dis.mids.reference.component;

import com.mastercard.dis.mids.reference.service.IDPAuthorizationClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IDPServiceReferenceClientTest {

    private final String ARID = "d22a5b3e-dbb5-4f77-ac74-30040fef4561";

    @InjectMocks
    private IDPServiceReferenceClient idpServiceReference;

    @Mock
    private IDPAuthorizationClientService idpScopesFulfillmentService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(idpServiceReference, "arid", ARID);
    }

    @Test
    void getRPRequestedScopes_success() {
        idpServiceReference.getRPRequestedScopes();
        idpServiceReference.getRPRequestedScopes(ARID);
        verify(idpScopesFulfillmentService, times(2)).getRPScopes(ARID);
    }

    @Test
    void fillRPScopesWithClaims_success() {
        idpServiceReference.fillRPScopesWithClaims();
        idpServiceReference.fillRPScopesWithClaims(ARID);
        verify(idpScopesFulfillmentService, times(2)).fillScopesFulfillment(any(), any(), anyBoolean());
    }
}
