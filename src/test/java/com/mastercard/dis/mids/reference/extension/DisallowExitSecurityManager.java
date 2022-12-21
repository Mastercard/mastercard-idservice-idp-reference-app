package com.mastercard.dis.mids.reference.extension;

import java.security.Permission;

public class DisallowExitSecurityManager extends SecurityManager{

    @Override
    public void checkExit(int status) throws SystemExitException {
        throw new SystemExitException(status);
    }

    @Override
    public void checkPermission(final Permission perm) {}
}
