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

package com.mastercard.dis.mids.reference.config;

import com.mastercard.dis.mids.reference.exception.ServiceException;
import com.mastercard.dis.mids.reference.interceptor.AuthenticationInterceptor;
import com.mastercard.dis.mids.reference.interceptor.EncryptionDecryptionInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.KeyStore;
import java.security.cert.CertificateException;

import static com.mastercard.dis.mids.reference.constants.Constants.X_ENCRYPTED_HEADER;

/**
 * This is ApiClient configuration, it will read properties from application.properties and create instance of ApiClient.
 */
@Slf4j
@Configuration
public class ApiClientConfiguration {

    @Value("${mastercard.api.base.path}")
    private String basePath;

    @Value("${mastercard.api.consumer.key}")
    private String consumerKey;

    @Value("${mastercard.api.keystore.alias}")
    private String keystoreAlias;

    @Value("${mastercard.api.keystore.password}")
    private String keystorePassword;

    @Value("${mastercard.api.key.file}")
    private Resource keyFile;

    @Value("${idp.userIdentifier}")
    private String userIdentifier;

    //this header needs to be enabled to test encryption, use the same property for encryptionHeader
    @Value("${mastercard.client.encryption.enable:false}")
    private boolean encryptionEnabled;

    @PostConstruct
    public void initialize() {
        if (null == keyFile || StringUtils.isEmpty(consumerKey)) {
            throw new ServiceException(".p12 file or consumerKey does not exist, please add details in application.properties");
        }
    }

    @Bean
    public ApiClient apiClient(EncryptionDecryptionInterceptor encryptionDecryptionInterceptor) {
        ApiClient client = new ApiClient();
        try {
            PrivateKey signingKey = getPrivateKeyFromP12(Paths.get(keyFile.getURI()), keystoreAlias, keystorePassword);
            client.setBasePath(basePath);
            client.setDebugging(true);
            client.setReadTimeout(40000);
            client.addDefaultHeader(X_ENCRYPTED_HEADER, encryptionEnabled ? Boolean.TRUE.toString() : Boolean.FALSE.toString());

            return client.setHttpClient(client.getHttpClient()
                    .newBuilder()
                    .addInterceptor(new AuthenticationInterceptor(consumerKey, signingKey, userIdentifier))
                    .addInterceptor(encryptionDecryptionInterceptor)
                    .build()
            );
        } catch (Exception e) {
            log.error("Error occurred while configuring ApiClient", e);
            throw new ServiceException("Error occurred while configuring ApiClient", e);
        }
    }

    private PrivateKey getPrivateKeyFromP12(Path pkcs12KeyFilePath,
                                            String signingKeyAlias,
                                            String signingKeyPassword) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore pkcs12KeyStore = KeyStore.getInstance("PKCS12");
        pkcs12KeyStore.load(Files.newInputStream(pkcs12KeyFilePath), signingKeyPassword.toCharArray());
        return (PrivateKey) pkcs12KeyStore.getKey(signingKeyAlias, signingKeyPassword.toCharArray());
    }

}
