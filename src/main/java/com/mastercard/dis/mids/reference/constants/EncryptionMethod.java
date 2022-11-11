package com.mastercard.dis.mids.reference.constants;

@SuppressWarnings("SameParameterValue")
public enum EncryptionMethod {

    A256GCM("A256GCM");

    private final String value;

    EncryptionMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
