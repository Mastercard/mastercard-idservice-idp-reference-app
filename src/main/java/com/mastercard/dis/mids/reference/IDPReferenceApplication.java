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
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Map;
import java.util.Scanner;
import static com.mastercard.dis.mids.reference.constants.Constants.UUID_REGEX;

@Slf4j
@SpringBootApplication
public class IDPReferenceApplication implements CommandLineRunner {

    private static final String ERROR = "Error : ";
    public static final String TYPE_A_VALID_ARID_OR_PRESS_ENTER_TO_USE_THE_PROPERTIES_ONE = "---> Type a valid arid or press ENTER to use the properties one:";
    private final IDPServiceReferenceClient idpServiceReference;
    private final Scanner scanner;
    private boolean exit = false;

    public IDPReferenceApplication(IDPServiceReferenceClient idpServiceReference) {
        this.idpServiceReference = idpServiceReference;
        scanner = new Scanner(System.in, "UTF-8");
    }

    public static void main(String[] args) {
        SpringApplication.run(IDPReferenceApplication.class);
        System.exit(0);
    }

    @Override
    public void run(String... args) {
        while (!exit) {
            showMenu();
            String option = scanner.nextLine();
            handleOption(option);
            pressAnyKey();
        }
        scanner.close();
        System.exit(0);
    }

    void showMenu() {
        log.info(" <--- Welcome to ID Reference APP --->");
        for (Map.Entry<String, String> entry : new Menu().get().entrySet()) {
            log.info(entry.getValue());
        }
        log.info(" ---> Type your option and press ENTER: ");
    }

    void handleOption(String option) {
        log.info("Your option : " + option);

        switch (option) {
            case "0":
                exit = true;
                break;
            case "1":
                //get scopes
                getRPScopes();
                break;
            case "2":
                //scope fulfillment
                postScopeFulfillment();
                break;
            case "3":
                //ClaimShareInsights
                enableClaimShareInsights();
                break;
            default:
                log.info("Invalid option!");
        }
    }

    void getRPScopes() {
        try {
            log.info("<<--- RetrieveRPScopes Started --->>");
            log.info(TYPE_A_VALID_ARID_OR_PRESS_ENTER_TO_USE_THE_PROPERTIES_ONE);
            String inputtedArid = scanner.nextLine();
            if (UUID_REGEX.matcher(inputtedArid).matches()) {
                log.info("<<--- RetrieveRPScopes Using typed arid --->>");
                idpServiceReference.getRPRequestedScopes(inputtedArid);
            } else {
                log.info("<<--- RetrieveRPScopes Using properties' arid --->>");
                idpServiceReference.getRPRequestedScopes();
            }
            log.info("<<--- RetrieveRPScopes Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- RetrieveRPScopes Failed Ended --->>");
        }
    }

    void postScopeFulfillment() {
        try {
            log.info("<<--- ScopesFulfillment Started --->>");
            log.info(TYPE_A_VALID_ARID_OR_PRESS_ENTER_TO_USE_THE_PROPERTIES_ONE);
            String inputtedArid = scanner.nextLine();
            if (UUID_REGEX.matcher(inputtedArid).matches()) {
                log.info("<<--- ScopesFulfillment Using typed arid --->>");
                idpServiceReference.fillRPScopesWithClaims(inputtedArid);
            } else {
                log.info("<<--- ScopesFulfillment Using properties' arid --->>");
                idpServiceReference.fillRPScopesWithClaims();
            }
            log.info("<<--- ScopesFulfillment Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- ScopesFulfillment Failed Ended --->>");
        }
    }

    void enableClaimShareInsights(){
        try {
            log.info("<<--- ClaimShareInsights Started --->>");
            log.info(TYPE_A_VALID_ARID_OR_PRESS_ENTER_TO_USE_THE_PROPERTIES_ONE);
            String inputtedArid = scanner.nextLine();
            if (Pattern.compile(UUID_REGEX).matcher(inputtedArid).matches()) {
                log.info("<<--- ClaimShareInsights Using typed arid --->>");
                idpServiceReference.enableClaimInsights(inputtedArid);
            } else {
                log.info("<<--- ClaimShareInsights Using properties' arid --->>");
                idpServiceReference.enableClaimInsights();
            }
            log.info("<<--- ClaimShareInsights Successfully Ended --->>");
        } catch (Exception e) {
            log.info(ERROR + e.getMessage());
            log.info("<<--- ClaimShareInsights Failed Ended --->>");
        }
    }

    void pressAnyKey() {
        log.info("Press ENTER to continue...");
        scanner.nextLine();
    }

}
