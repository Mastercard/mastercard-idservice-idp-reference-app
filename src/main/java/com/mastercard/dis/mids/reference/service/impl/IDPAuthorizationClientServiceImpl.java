/*
 Copyright (c) 2023 Mastercard

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

package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.service.IDPAuthorizationClientService;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.IdpScopesFulfillmentApi;
import org.openapitools.client.api.IdpScopesRequestApi;
import org.openapitools.client.model.IDPScopesAuthorization;
import org.openapitools.client.model.IDPScopesAuthorizationData;
import org.openapitools.client.model.RPScopes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IDPAuthorizationClientServiceImpl implements IDPAuthorizationClientService {

    private final IdpScopesRequestApi idpScopesRequestApi;
    private final IdpScopesFulfillmentApi idpScopesFulfillmentApi;
    private final ExceptionUtil exceptionUtil;

    @Autowired
    public IDPAuthorizationClientServiceImpl(ApiClient apiClient, ExceptionUtil exceptionUtil) {
        idpScopesFulfillmentApi = new IdpScopesFulfillmentApi(apiClient);
        idpScopesRequestApi = new IdpScopesRequestApi(apiClient);
        this.exceptionUtil = exceptionUtil;
    }

    @Override
    public RPScopes getRPScopes(String arid) {
        try {
            return idpScopesRequestApi.retrieveRPScopes(arid);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }
    }

    @Override
    public IDPScopesAuthorizationData fillScopesFulfillment(String arid, IDPScopesAuthorization requestData, boolean encryptedPayload) {
        try {
            IDPScopesAuthorization idpScopesAuthorizationRequestData = new IDPScopesAuthorization()
                    .claims(requestData.getClaims())
                    .userConsent(requestData.getUserConsent())
                    .countryCode(requestData.getCountryCode())
                    .locale(requestData.getLocale());
            return idpScopesFulfillmentApi.processClaimsRequestedScopes(arid, idpScopesAuthorizationRequestData, encryptedPayload);
        } catch (ApiException e) {
            throw exceptionUtil.logAndConvertToServiceException(e);
        }catch (IllegalArgumentException e){
            if(null != e.getMessage() && e.getMessage().contains("The field `encryptedData` in the JSON string is not defined in the `IDPScopesAuthorizationData` properties.")){
                return null;
            }
            throw new IllegalArgumentException(e);
        }
    }
}
