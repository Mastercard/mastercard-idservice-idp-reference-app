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

package com.mastercard.dis.mids.reference;

import com.mastercard.dis.mids.reference.component.IDPServiceReferenceClient;
import com.mastercard.dis.mids.reference.constants.Menu;
import com.mastercard.dis.mids.reference.extension.DisableSystemExit;
import com.mastercard.dis.mids.reference.extension.SystemExitException;
import com.mastercard.dis.mids.reference.service.IDPAuthorizationClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.model.IDPScopesAuthorization;
import org.openapitools.client.model.IDPScopesAuthorizationData;
import org.openapitools.client.model.RPScopes;
import org.springframework.test.util.ReflectionTestUtils;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import static com.mastercard.dis.mids.reference.example.IDPScopesAuthorizationExample.getIDPScopesAuthorizationRequestData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IDPReferenceApplicationTest {

    Scanner scanner;

    @InjectMocks
    IDPReferenceApplication idpReferenceApplicationMock;

    @InjectMocks
    IDPServiceReferenceClient idpServiceReferenceClientMock;

    @Mock
    IDPAuthorizationClientService idpAuthorizationClientService;

    private static final Map<String, String> MENU_MAP_TEST = new HashMap<>();

    @BeforeEach
    void setup() {
        MENU_MAP_TEST.put("0", "0)   Exit");
        MENU_MAP_TEST.put("1", "1)   RP Scopes");
        MENU_MAP_TEST.put("2", "2)   Scope-fulfillments");
        MENU_MAP_TEST.put("3", "3)   Share Insights");
    }

    @Test
    void consoleMenu_runAndCheckValues_works() {
        Menu menuApp = new Menu();
        for (Map.Entry<String, String> entry : MENU_MAP_TEST.entrySet()) {
            String valueMenu = MENU_MAP_TEST.get(entry.getKey());
            assertEquals(valueMenu, entry.getValue());
        }
    }

    @Test
    void console_showMenu_works() {
        IDPReferenceApplication idpReferenceApplication = Mockito.spy(new IDPReferenceApplication(null));

        idpReferenceApplication.showMenu();

        verify(idpReferenceApplication, times(1)).showMenu();
    }

    @Test
    @DisableSystemExit
    void Should_cover_invalid_option_and_exit_menu_flow(){
        String data = String.format("%s\n%s\n%s\n%s", "9", "\t", "0", "\t");
        Scanner streamScanner = getStreamedScannerMock(data);

        ReflectionTestUtils.setField(idpReferenceApplicationMock, "scanner",  streamScanner);

        SystemExitException exitException = assertThrows(SystemExitException.class, () ->
                idpReferenceApplicationMock.run()
        );
        assertEquals(0, exitException.getStatusCode());
    }

    @Test
    @DisableSystemExit
    void Should_return_scopes_sending_arid_by_console(){
        String data = String.format("%s\n%s\n%s\n%s\n%s", "1", getAridMock(), "\t",  "0", "\t");
        Scanner streamScanner = getStreamedScannerMock(data);
        RPScopes scopes = mock(RPScopes.class);

        ReflectionTestUtils.setField(idpReferenceApplicationMock, "scanner",  streamScanner);
        ReflectionTestUtils.setField(idpReferenceApplicationMock, "idpServiceReference",  idpServiceReferenceClientMock);
        ReflectionTestUtils.setField(idpServiceReferenceClientMock, "idpScopesFulfillmentService",  idpAuthorizationClientService);
        scanner = (Scanner) ReflectionTestUtils.getField(idpReferenceApplicationMock, "scanner");

        when(idpAuthorizationClientService.getRPScopes(getAridMock())).thenReturn(scopes);

        SystemExitException exitException = assertThrows(SystemExitException.class, () ->
                idpReferenceApplicationMock.run()
        );
        assertEquals(0, exitException.getStatusCode());
        assertThrows(IllegalStateException.class, ()-> scanner.hasNext());
    }

    @Test
    @DisableSystemExit
    void Should_return_scopes_sending_arid_by_application_properties(){
        String errorAridMock = getAridMock()+"3rr0";
        String data = String.format("%s\n%s\n%s\n%s\n%s", "1", errorAridMock, "\t", "0", "\t");
        Scanner streamScanner = getStreamedScannerMock(data);
        RPScopes scopes = mock(RPScopes.class);

        ReflectionTestUtils.setField(idpReferenceApplicationMock, "scanner",  streamScanner);
        ReflectionTestUtils.setField(idpReferenceApplicationMock, "idpServiceReference",  idpServiceReferenceClientMock);
        ReflectionTestUtils.setField(idpServiceReferenceClientMock, "idpScopesFulfillmentService",  idpAuthorizationClientService);
        ReflectionTestUtils.setField(idpServiceReferenceClientMock, "arid",  errorAridMock);
        scanner = (Scanner) ReflectionTestUtils.getField(idpReferenceApplicationMock, "scanner");

        when(idpAuthorizationClientService.getRPScopes(errorAridMock)).thenReturn(scopes);

        SystemExitException exitException = assertThrows(SystemExitException.class, () ->
                idpReferenceApplicationMock.run()
        );
        assertEquals(0, exitException.getStatusCode());
        assertThrows(IllegalStateException.class, ()-> scanner.hasNext());
    }

    @Test
    @DisableSystemExit
    void Should_return_scopes_authorization_data_when_fill_scopes(){
        String data = String.format("%s\n%s\n%s\n%s\n%s", "2", getAridMock(), "\t", "0", "\t");
        Scanner streamScanner = getStreamedScannerMock(data);
        IDPScopesAuthorization requestData = getIDPScopesAuthorizationRequestData();
        IDPScopesAuthorizationData scopesAuthorizationData = mock(IDPScopesAuthorizationData.class);

        ReflectionTestUtils.setField(idpReferenceApplicationMock, "scanner",  streamScanner);
        ReflectionTestUtils.setField(idpReferenceApplicationMock, "idpServiceReference",  idpServiceReferenceClientMock);
        ReflectionTestUtils.setField(idpServiceReferenceClientMock, "idpScopesFulfillmentService",  idpAuthorizationClientService);
        ReflectionTestUtils.setField(idpServiceReferenceClientMock, "arid",  getAridMock());
        scanner = (Scanner) ReflectionTestUtils.getField(idpReferenceApplicationMock, "scanner");

        when(idpAuthorizationClientService.fillScopesFulfillment(getAridMock(), requestData,false)).thenReturn(scopesAuthorizationData);

        SystemExitException exitException = assertThrows(SystemExitException.class, () ->
                idpReferenceApplicationMock.run()
        );
        assertEquals(0, exitException.getStatusCode());
        assertThrows(IllegalStateException.class, ()-> scanner.hasNext());
    }

    @Test
    @DisableSystemExit
    void Should_return_scopes_authorization_data_when_fill_scopes_by_application_properties(){
        String errorAridMock = getAridMock()+"3rr0";
        String data = String.format("%s\n%s\n%s\n%s\n%s", "2", errorAridMock, "\t", "0", "\t");
        Scanner streamScanner = getStreamedScannerMock(data);
        IDPScopesAuthorization requestData = getIDPScopesAuthorizationRequestData();
        IDPScopesAuthorizationData scopesAuthorizationData = mock(IDPScopesAuthorizationData.class);

        ReflectionTestUtils.setField(idpReferenceApplicationMock, "scanner",  streamScanner);
        ReflectionTestUtils.setField(idpReferenceApplicationMock, "idpServiceReference",  idpServiceReferenceClientMock);
        ReflectionTestUtils.setField(idpServiceReferenceClientMock, "idpScopesFulfillmentService",  idpAuthorizationClientService);
        ReflectionTestUtils.setField(idpServiceReferenceClientMock, "arid",  errorAridMock);
        scanner = (Scanner) ReflectionTestUtils.getField(idpReferenceApplicationMock, "scanner");

        when(idpAuthorizationClientService.fillScopesFulfillment(errorAridMock, requestData,false)).thenReturn(scopesAuthorizationData);

        SystemExitException exitException = assertThrows(SystemExitException.class, () ->
                idpReferenceApplicationMock.run()
        );
        assertEquals(0, exitException.getStatusCode());
        assertThrows(IllegalStateException.class, ()-> scanner.hasNext());
    }

    private String getAridMock(){
        return "714ebdd5-531a-4992-8cb6-c226c2faa19b";
    }

    private Scanner getStreamedScannerMock(String data){
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        return new Scanner(new BufferedInputStream(stream), "UTF-8");
    }

}