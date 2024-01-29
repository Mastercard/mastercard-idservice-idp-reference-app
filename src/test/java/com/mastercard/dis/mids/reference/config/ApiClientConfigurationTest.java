package com.mastercard.dis.mids.reference.config;

import com.mastercard.dis.mids.reference.exception.ServiceException;
import com.mastercard.dis.mids.reference.interceptor.AuthenticationInterceptor;
import com.mastercard.dis.mids.reference.interceptor.EncryptionDecryptionInterceptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiClient;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ApiClientConfigurationTest {

    @InjectMocks
    ApiClientConfiguration apiClientConfiguration;

    @Test
    void Should_throw_service_exception_when_keyFile_and_consumerKey_are_empty(){
        String message = ".p12 file or consumerKey does not exist, please add details in application.properties";
        Exception exception = assertThrows(ServiceException.class, () -> apiClientConfiguration.initialize());
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void Should_return_service_exception_when_any_error_occurs() {
        String message = "Error occurred while configuring ApiClient";
        EncryptionDecryptionInterceptor encDecInterceptor = mock(EncryptionDecryptionInterceptor.class);

        Exception exception = assertThrows(ServiceException.class, () -> apiClientConfiguration.apiClient(encDecInterceptor, null));
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void Should_return_api_client_when_method_is_called() {
        EncryptionDecryptionInterceptor encDecInterceptor = mock(EncryptionDecryptionInterceptor.class);
        AuthenticationInterceptor authInterceptor = mock(AuthenticationInterceptor.class);

        ApiClient apiClient = apiClientConfiguration.apiClient(encDecInterceptor, authInterceptor);

        assertNotNull(apiClient);
        assertEquals(apiClient.getClass(), ApiClient.class);
    }

}


