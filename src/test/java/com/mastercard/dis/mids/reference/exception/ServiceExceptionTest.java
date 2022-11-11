package com.mastercard.dis.mids.reference.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.ApiErrors;

class ServiceExceptionTest {

    private static final String ERROR_MESSAGE = "message1";

    @Test
    void create_withStringAndException_works() {
        Exception exception = new Exception();
        ServiceException serviceException = new ServiceException(ERROR_MESSAGE, exception);
        Assertions.assertEquals(ERROR_MESSAGE, serviceException.getMessage());
    }

    @Test
    void create_withStringAndErrorReponseErros_works() {
        ApiErrors errorResponseErrors = new ApiErrors();
        ServiceException serviceException = new ServiceException(ERROR_MESSAGE, errorResponseErrors);

        Assertions.assertEquals(serviceException.getServiceErrors().getErrors().getError(),
                errorResponseErrors.getError());

        Assertions.assertEquals(ERROR_MESSAGE, serviceException.getMessage());
    }

    @Test
    void create_withExceptionAndErrorReponseErros_works() {
        ApiErrors errorResponseErrors = new ApiErrors();
        Exception exception = new Exception(ERROR_MESSAGE);
        ServiceException serviceException = new ServiceException(exception, errorResponseErrors);
        Assertions.assertEquals(serviceException.getCause(), exception);
    }
}