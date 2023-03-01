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

package com.mastercard.dis.mids.reference.interceptor;

import com.mastercard.developer.utils.AuthenticationUtils;
import com.mastercard.dis.mids.reference.exception.ServiceException;
import com.mastercard.dis.mids.reference.util.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import javax.annotation.Nonnull;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.mastercard.dis.mids.reference.constants.Constants.X_ENCRYPTED_PAYLOAD;

@Slf4j
@RequiredArgsConstructor
public class EncryptionDecryptionInterceptor implements Interceptor {

    private final Resource encryptionCertificateFile;

    private final String encryptionCertificateFingerPrint;

    private final Resource decryptionKeystore;

    private final String decryptionKeystoreAlias;

    private final String decryptionKeystorePassword;

    private final boolean isEncryptionEnable;

    private final boolean isDecryptionEnable;

    private static PrivateKey signingKey;

    @Nonnull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = handleRequest(chain.request());
        Response response = chain.proceed(request);
        return handleResponse(request, response);
    }

    private Request handleRequest(Request request) {
        log.info("request.url(): {}", request.url().uri().getPath());
        if (isEncryptionEnable && isEncryptionAllowed(request)) {
            try {
                String body = bodyToString(request);
                String encryptedData = EncryptionUtils.jweEncrypt(body, encryptionCertificateFile, encryptionCertificateFingerPrint);
                JSONObject encryptedRequestJson = new JSONObject();
                encryptedRequestJson.put("encryptedData", encryptedData);
                log.info("Encrypted Payload sending to server: {}", encryptedRequestJson);
                Request.Builder put = request
                        .newBuilder()
                        .headers(request.headers())
                        .addHeader(X_ENCRYPTED_PAYLOAD, "true")
                        .put(RequestBody.create(encryptedRequestJson.toJSONString(), MediaType.parse("application/json")));
                return put.build();
            } catch (Exception e) {
                log.error("Unable to encrypt request data to server", e);
                throw new ServiceException("Unable to encrypt request data to server", e);
            }
        }

        return request;
    }

    private synchronized Response handleResponse(Request request, Response encryptedResponse) {
        if (isDecryptionEnable && isDecryptionAllowed(request)) {
            try {
                if (encryptedResponse.code() != 200) {
                    return encryptedResponse; // We will receive encrypted payload only for 200 response
                }
                ResponseBody responseBody = encryptedResponse.body();
                String encryptedResponseStr = Objects.requireNonNull(responseBody).string();
                JSONObject encryptedResponseJson = (JSONObject) JSONValue.parse(encryptedResponseStr);
                if (null == encryptedResponseJson.getAsString("encryptedData")){
                    log.info("The decryption is set to 'true', but there is no encrypted response");
                    return encryptedResponse.newBuilder().body(ResponseBody.create(encryptedResponseStr, MediaType.parse("application/json"))).build();
                }
                log.info("Encrypted Payload received from server: {}", encryptedResponseStr);

                if (signingKey == null) {
                    signingKey = AuthenticationUtils
                            .loadSigningKey(Files.newInputStream(decryptionKeystore.getFile().toPath()),
                                    decryptionKeystoreAlias, decryptionKeystorePassword);
                }

                String decryptedPayload = EncryptionUtils
                        .jweDecrypt(encryptedResponseJson.getAsString("encryptedData"), (RSAPrivateKey) signingKey);

                Response.Builder responseBuilder = encryptedResponse.newBuilder();
                ResponseBody decryptedBody = ResponseBody.create(decryptedPayload, responseBody.contentType());

                return responseBuilder
                        .body(decryptedBody)
                        .header("Content-Length", String.valueOf(decryptedBody.contentLength()))
                        .build();
            } catch (Exception e) {
                log.error("Unable to decrypt response from server", e);
                throw new ServiceException("Unable to process response from server", e);
            }
        }

        return encryptedResponse;
    }

    private boolean isEncryptionAllowed(Request request) {
        List<String> encryptionEndpoints = Collections.singletonList(
                "/idservice-idp/scope-fulfillments");
        return encryptionEndpoints.stream().anyMatch(entry -> request.url().uri().getPath().contains(entry));
    }

    private boolean isDecryptionAllowed(Request request) {
        List<String> decryptionEndpoints = Collections.singletonList(
                "/idservice-idp/scope-fulfillments");
        return decryptionEndpoints.stream().anyMatch(entry -> request.url().uri().getPath().contains(entry));
    }

    private String bodyToString(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            Objects.requireNonNull(copy.body()).writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}