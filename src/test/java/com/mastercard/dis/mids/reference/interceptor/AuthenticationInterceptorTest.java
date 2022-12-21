package com.mastercard.dis.mids.reference.interceptor;

import com.mastercard.dis.mids.reference.exception.ServiceException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.io.IOException;
import java.security.PrivateKey;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class AuthenticationInterceptorTest {

    @InjectMocks
    private  AuthenticationInterceptor authenticationInterceptor;


    private  String userIdentifier = "123456789";

    private  String consumerKey = "Y02hWEOY6YFcGb3KsS6b6YycoO1XIVIuy3pvrlv0b4b18c5a!98b1dd6dcf334b44b7fca00f26f323610000000000000000";

    @Mock
    private  PrivateKey signingKey ;

    @Mock
    Interceptor.Chain chain  ;

    @BeforeEach
    void setUp(){

        ReflectionTestUtils.setField(authenticationInterceptor, "consumerKey", consumerKey);
        ReflectionTestUtils.setField(authenticationInterceptor, "userIdentifier", userIdentifier);
        ReflectionTestUtils.setField(authenticationInterceptor, "signingKey", signingKey);
    }


    @Test
    void intercept_ok() throws IOException , ServiceException{

        try {
            Response responseCreated = create_response();
            doReturn(responseCreated.request()).when(chain).request();
            doReturn("RSA").when(signingKey).getAlgorithm();
            authenticationInterceptor.intercept(chain);
        }catch (ServiceException serviceException){
            Assert.assertNotNull(serviceException);
        }

    }

    private Response create_response (){

        Request request = new Request.Builder()
                .url("https://google.com")
                .header("User-Agent", "OkHttp Example")
                .build();

        return new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_2)
                .code(401) // status code
                .message("mock")
                .body(ResponseBody.create("application/json; charset=utf-8", MediaType.parse("{}")))
                .build();
    }

}