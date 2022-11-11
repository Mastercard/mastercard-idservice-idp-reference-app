package com.mastercard.dis.mids.reference.exception;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openapitools.client.ApiException;
import org.openapitools.client.JSON;

class ExceptionUtilTest {

    private static final String ERROR_MESSAGE = "message1";

    @BeforeAll
    public static void whenTheClassStartTheTests_GsonIsSet() {
        JSON.setGson(new Gson());
    }

    @Test
    void create_withApiException_works() {
        ApiException apiException = new ApiException();
        ExceptionUtil exceptionUtil = new ExceptionUtil();
        ServiceException serviceException = exceptionUtil.logAndConvertToServiceException(apiException);
        Assertions.assertNotNull(serviceException);
    }

    @Test
    void create_withApiExceptionWithMessage_works() {
        ApiException apiException = new ApiException(ERROR_MESSAGE);

        ExceptionUtil exceptionUtil = new ExceptionUtil();
        ServiceException serviceException = exceptionUtil.logAndConvertToServiceException(apiException);
        Assertions.assertNotNull(serviceException);
    }

    @Test
    void create_withApiExceptionWithThrowable_works() {
        ApiException apiException = new ApiException(new Exception(ERROR_MESSAGE));
        ExceptionUtil exceptionUtil = new ExceptionUtil();
        ServiceException serviceException = exceptionUtil.logAndConvertToServiceException(apiException);
        Assertions.assertNotNull(serviceException);
    }

    @Test
    void create_withApiExceptionWithCodeAndString_works() {
        ApiException apiException = new ApiException(0, ERROR_MESSAGE);
        ExceptionUtil exceptionUtil = new ExceptionUtil();
        ServiceException serviceException = exceptionUtil.logAndConvertToServiceException(apiException);
        Assertions.assertNotNull(serviceException);
        Assertions.assertEquals(0, apiException.getCode());
        Assertions.assertTrue(apiException.getMessage().contains(String.format("Message: %s", ERROR_MESSAGE)));
    }

}
