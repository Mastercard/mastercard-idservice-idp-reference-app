package com.mastercard.dis.mids.reference.interceptor;

import com.mastercard.dis.mids.reference.exception.ServiceException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request.Builder;
import okhttp3.Response;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.Date;
import java.util.UUID;

import static com.mastercard.dis.mids.reference.constants.Constants.MIDS_TENANT;

@Slf4j
public class AuthenticationInterceptor implements Interceptor {

    private final String userIdentifier;
    private final String consumerKey;
    private final PrivateKey signingKey;

    public AuthenticationInterceptor(String consumerKey, PrivateKey signingKey, String userIdentifier) {
        this.consumerKey = consumerKey;
        this.signingKey = signingKey;
        this.userIdentifier = userIdentifier;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Builder builder = chain.request().newBuilder();
        StringBuilder header = new StringBuilder("Bearer ");
        try {
            header.append(getSignedJWT());
        } catch (JOSEException e) {
            log.error("Error occurred while configuring ApiClient", e);
            throw new ServiceException("Error occurred while configuring ApiClient", e);
        }
        builder.addHeader("Authorization", header.toString());
        return chain.proceed(builder.build());
    }

    private String getSignedJWT() throws JOSEException {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 900000; // 15 minutes
        Date exp = new Date(expMillis);
        JWSSigner signer = new RSASSASigner(this.signingKey);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("not_available")
                .claim("alias", userIdentifier)
                .claim("tid", MIDS_TENANT)
                .issueTime(now)
                .expirationTime(exp)
                .jwtID(UUID.randomUUID().toString())
                .notBeforeTime(now)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .contentType("JWS")
                .keyID(this.consumerKey)
                .build(), claims);
        signedJWT.sign(signer);
        String authToken = signedJWT.serialize();
        log.info("Auth Header token: {}", authToken);
        return authToken;
    }
}
