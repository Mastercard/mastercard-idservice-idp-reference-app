/*
 Copyright (c) 2021 Mastercard

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.mastercard.dis.mids.reference.component;

import com.mastercard.dis.mids.reference.example.IDPScopesAuthorizationExample;
import com.mastercard.dis.mids.reference.service.IDPAuthorizationClientService;
import lombok.RequiredArgsConstructor;
import org.openapitools.client.model.IDPScopesAuthorization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IDPServiceReferenceClient {

    private final IDPAuthorizationClientService idpScopesFulfillmentService;

    @Value("${arid}")
    private String arid;

    @Value("${mastercard.client.encryption.enable}")
    private boolean encryptedPayload;

    public void getRPRequestedScopes(String inputtedArid) {
        idpScopesFulfillmentService.getRPScopes(inputtedArid);
    }

    public void getRPRequestedScopes() {
        idpScopesFulfillmentService.getRPScopes(arid);
    }

    public void fillRPScopesWithClaims(String inputtedArid) {
        IDPScopesAuthorization requestData = IDPScopesAuthorizationExample.getIDPScopesAuthorizationRequestData();
        idpScopesFulfillmentService.fillScopesFulfillment(inputtedArid, requestData, encryptedPayload);
    }

    public void fillRPScopesWithClaims() {
        IDPScopesAuthorization requestData = IDPScopesAuthorizationExample.getIDPScopesAuthorizationRequestData();
        idpScopesFulfillmentService.fillScopesFulfillment(arid, requestData, encryptedPayload);
    }

}
