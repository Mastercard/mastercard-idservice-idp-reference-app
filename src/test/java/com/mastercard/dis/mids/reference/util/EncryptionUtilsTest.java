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

import com.mastercard.dis.mids.reference.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class EncryptionUtilsTest {

    @Mock
    File file;

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
}
