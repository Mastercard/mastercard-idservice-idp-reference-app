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

package com.mastercard.dis.mids.reference.util;

import com.mastercard.developer.encryption.JweConfigBuilder;
import com.mastercard.developer.encryption.jwe.JweObject;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Objects;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EncryptionUtilsTest {

    @Mock
    File file;

    @Mock
    JweObject jweObject;

    @Test
    void testJweEncryptThrowsServiceException() {
        ClassPathResource encryptionCertificateFile = new ClassPathResource("/certificate_not_found.pem");
        String encryptionCertificateFingerPrint = "336b870e55e33c6d278a5661006a6017ef88430bc8d00cc5fd7989a4c077bfe2";

        assertThatThrownBy(() -> EncryptionUtils.jweEncrypt(null, encryptionCertificateFile, encryptionCertificateFingerPrint))
                .isInstanceOf(ServiceException.class);
    }

    @Test
    void whenTryToDecryptWithInvalidCertificateAndFingerPrint_thenThrowsServiceException() {
        Mockito.doReturn("pathToFile").when(file).getPath();
        Resource createdResource = new FileSystemResource(file);

        assertThatThrownBy(() -> EncryptionUtils.jweEncrypt("{\"dataKey\":\"dataValue\"}", createdResource, "encryptionCertificateFingerPrint"))
                .isInstanceOf(ServiceException.class);
    }

    @Test
    void Should_return_service_exception_when_cert_file_not_found() {
        String filePath = "cert_file_name_not_found_mock.cert";
        Resource createdResource = new FileSystemResource(filePath);

        Exception exception = assertThrows(ServiceException.class, () -> {
            EncryptionUtils.jweEncrypt("{\"dataKey\":\"dataValue\"}", createdResource, "encryptionCertificateFingerPrint");
        });

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(filePath));
    }
}
