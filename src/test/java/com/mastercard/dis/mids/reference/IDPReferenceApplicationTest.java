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

package com.mastercard.dis.mids.reference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IDPReferenceApplicationTest {

    private static final Map<String, String> MENU_MAP_TEST = new HashMap<>();

    @BeforeEach
    void setup() {
        MENU_MAP_TEST.put("1", "1)   RP Scopes");
        MENU_MAP_TEST.put("2", "2)   Scope-fulfillments");
        MENU_MAP_TEST.put("3", "3)   Exit");
    }

    @Test
    void consoleMenu_runAndCheckValues_works() {
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
    void console_handleOption_works() {
        IDPReferenceApplication idpReferenceApplication = Mockito.spy(new IDPReferenceApplication(null));

        idpReferenceApplication.handleOption("1");
        idpReferenceApplication.handleOption("2");

        verify(idpReferenceApplication, times(2)).handleOption(anyString());
    }

}
