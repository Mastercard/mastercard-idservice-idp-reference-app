package com.mastercard.dis.mids.reference.service.impl;

import com.mastercard.dis.mids.reference.config.ApiClientConfiguration;
import com.mastercard.dis.mids.reference.exception.ExceptionUtil;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import okhttp3.Call;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.IdpScopesFulfillmentApi;
import org.openapitools.client.model.IDPScopesAuthorization;
import org.openapitools.client.model.IDPScopesAuthorizationData;
import org.openapitools.client.model.RPScopes;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IDPAuthorizationClientServiceImplTest {

    public static final String X_MIDS_USERAUTH_SESSIONID = "x-mids-userauth-sessionid";
    private final String ARID = "5d9140df-4fe5-4ff7-8317-dc4e85f409b5";
    private  final boolean ENCRYPTEDPAYLOAD = false;
    Map<String, List<String>> headers;
    List<String> headersList;
    @InjectMocks
    private IDPAuthorizationClientServiceImpl iDPScopesFulfillmentServiceImpl;
    @Mock
    private IdpScopesFulfillmentApi mockIdpScopesFulfillmentApi;
    @Mock
    private ExceptionUtil exceptionUtil;
    @Mock
    private ApiClientConfiguration apiClientConfiguration;
    @Mock
    private ApiClient apiClientMock;

    @BeforeEach
    void setUp() throws Exception {
        headers = new HashMap<>();
        headersList = new ArrayList<>();
        headersList.add(X_MIDS_USERAUTH_SESSIONID);
        headers.put(X_MIDS_USERAUTH_SESSIONID, headersList);
        when(apiClientMock.buildCall(any(), anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(), any(String[].class), any())).thenReturn(mock(Call.class));
        when(apiClientMock.escapeString(anyString())).thenReturn(ARID);
    }

    @Test
    void fillScopes_success() throws ApiException {

        when(apiClientMock.execute(any(), any())).thenReturn(new ApiResponse<>(200, headers, getRPScopes()));

        RPScopes response = iDPScopesFulfillmentServiceImpl.getRPScopes(ARID);
        assertNotNull(response);
        verify(apiClientMock, times(1)).execute(any(Call.class), any(Type.class));
    }

    @Test
    void fillScopes_exception() throws ApiException {

        when(apiClientMock.execute(any(), any())).thenThrow(new ApiException());
        when(exceptionUtil.logAndConvertToServiceException(any())).thenReturn(new ServiceException("Test"));

        try {
            iDPScopesFulfillmentServiceImpl.getRPScopes(ARID);
            fail("Expecting ServiceException");
        } catch (ServiceException e) {
            verify(exceptionUtil, times(1)).logAndConvertToServiceException(any());
        }
    }

    private RPScopes getRPScopes() {
        RPScopes responseData = new RPScopes();
        responseData.setRpLogoUrl("url");
        responseData.setRpName("rpName");
        responseData.setScopes(Collections.singletonList("scopes"));
        return responseData;
    }

    @Test
    void fillScopesFulfillment_success() throws ApiException {

        IDPScopesAuthorization requestData = new IDPScopesAuthorization();

        when(apiClientMock.execute(any(), any())).thenReturn(new ApiResponse<>(200, headers, getIDPScopesAuthorizationResponseData()));

        IDPScopesAuthorizationData response = iDPScopesFulfillmentServiceImpl.fillScopesFulfillment(ARID, requestData, ENCRYPTEDPAYLOAD);
        assertNotNull(response.getRedirectUri());
        verify(apiClientMock, times(1)).execute(any(Call.class), any(Type.class));
    }

    @Test
    void fillScopesFulfillment_exception() throws ApiException {

        IDPScopesAuthorization requestData = new IDPScopesAuthorization();

        when(apiClientMock.execute(any(), any())).thenThrow(new ApiException());
        when(exceptionUtil.logAndConvertToServiceException(any())).thenReturn(new ServiceException("Test"));

        try {
            iDPScopesFulfillmentServiceImpl.fillScopesFulfillment(ARID, requestData, ENCRYPTEDPAYLOAD);
            fail("Expecting ServiceException");
        } catch (ServiceException e) {
            verify(exceptionUtil, times(1)).logAndConvertToServiceException(any());
        }
    }

    private IDPScopesAuthorizationData getIDPScopesAuthorizationResponseData() {
        IDPScopesAuthorizationData responseData = new IDPScopesAuthorizationData();
        responseData.setRedirectUri("url");
        return responseData;
    }

}
