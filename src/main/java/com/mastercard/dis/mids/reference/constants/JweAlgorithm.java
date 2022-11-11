package com.mastercard.dis.mids.reference.constants;

@SuppressWarnings("SameParameterValue")
public enum JweAlgorithm {

    RSA_OAEP_256("RSA-OAEP-256");

    private final String value;

    JweAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}