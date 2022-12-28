# ID for Identity Providers Reference Implementation
[![](https://developer.mastercard.com/_/_/src/global/assets/svg/mcdev-logo-dark.svg)](https://developer.mastercard.com/)

[![](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](./LICENSE)
[![](https://sonarcloud.io/api/project_badges/measure?project=Mastercard_mastercard-idservice-idp-reference-app&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Mastercard_mastercard-idservice-idp-reference-app)
[![](https://sonarcloud.io/api/project_badges/measure?project=Mastercard_mastercard-idservice-idp-reference-app&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Mastercard_mastercard-idservice-idp-reference-app)
[![](https://sonarcloud.io/api/project_badges/measure?project=Mastercard_mastercard-idservice-idp-reference-app&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Mastercard_mastercard-idservice-idp-reference-app)

## Table of Contents
- [Overview](#overview)
  * [Compatibility](#compatibility)
  * [References](#references)
- [Usage](#usage)
  * [Prerequisites](#prerequisites)
  * [Configuration](#configuration)
  * [Integrating with OpenAPI Generator](#integrating-with-openapi-generator)
  * [OpenAPI Generator Plugin Configuration](#openAPI_generator_plugin_configuration)
  * [Generating The API Client Sources](#generating_the_API_client_sources)
  * [Test Case Execution](#test-case-execute)
  * [Use Cases](#use-cases)
- [API Reference](#api-reference)
  * [Authorization](#authorization)
  * [Encryption and Decryption](#encryption-decryption)
  * [Request Examples](#request-examples)
  * [Recommendation](#recommendation)
- [Support](#support)
- [License](#license)

## Overview <a name="overview"></a>
ID is a digital identity service from Mastercard that helps you apply for, enroll in, log in to, and access services more simply, securely and privately. Rather than manually providing your information when you are trying to complete tasks online or in apps, ID enables you to share your verified information automatically, more securely, and with your consent and control. ID also enables you to do away with passwords and protects your personal information. Please see here for more details on the API: [Mastercard Developers](https://developer.mastercard.com/mastercard-id-service/documentation/).

For more information regarding the program refer to [Id Service](https://idservice.com/)

### Compatibility <a name="compatibility"></a>
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html).

### References <a name="references"></a>
* [Mastercard’s OAuth Signer library](https://github.com/Mastercard/oauth1-signer-java)
* [Using OAuth 1.0a to Access Mastercard APIs](https://developer.mastercard.com/platform/documentation/using-oauth-1a-to-access-mastercard-apis/)
* [Mastercard’s Payload Encryption/Decryption library](https://github.com/Mastercard/client-encryption-java)
* [Using Payload Encryption](https://developer.mastercard.com/platform/documentation/security-and-authentication/securing-sensitive-data-using-payload-encryption/)

## Usage <a name="usage"></a>
### Prerequisites <a name="prerequisites"></a>
* [Mastercard Developers Account](https://developer.mastercard.com/dashboard) with access to ID for Identity Providers API
* A text editor or IDE
* [Spring Boot 2.2+](https://spring.io/projects/spring-boot)
* [Apache Maven 3.3+](https://maven.apache.org/download.cgi)
* Set up the `JAVA_HOME` environment variable to match the location of your Java installation.

### Configuration <a name="configuration"></a>
* Create an account at [Mastercard Developers](https://developer.mastercard.com/account/sign-up).  
* Create a new project and add `ID for Identity Providers` API to your project.   
* Configure project and download all the keys. It will download multiple files.  
* Select all `.p12` files, `.pem` file and copy it to `src/main/resources` in the project folder.
* Open `${project.basedir}/src/main/resources/application.properties` and configure below parameters.
    
    >**mastercard.api.base.path=corresponding MC ID Service Url, example : https://sandbox.api.mastercard.com/mcidservice**, it's a static field, will be used as a host to make API calls.
    
    **The properties below will be required for authentication of API calls.**
    
    >**mastercard.api.key.file=**, this refers to .p12 file found in the signing key. Please place .p12 file at src\main\resources in the project folder and add classpath for .p12 file.
    
    >**mastercard.api.consumer.key=**, this refers to your consumer key. Copy it from "Keys" section on your project page in [Mastercard Developers](https://developer.mastercard.com/dashboard)
      
    >**mastercard.api.keystore.alias=keyalias**, this is the default value of key alias. If it is modified, use the updated one from keys section in [Mastercard Developers](https://developer.mastercard.com/dashboard).
    
    >**mastercard.api.keystore.password=keystorepassword**, this is the default value of key alias. If it is modified, use the updated one from keys section in [Mastercard Developers](https://developer.mastercard.com/dashboard).

    >**arid=**, this is the request id generated in the original relying party request. For more information about its usage you can find in the API reference section in [Mastercard IDP API Reference](https://developer.mastercard.com/mastercard-id-for-idp/documentation/api-reference/).
  
    >**idp.userIdentifier=**, this is the unique identifier which IDP provides upon authenticating an end-user. This value forms 'alias' claim in the JWT token to be generated by IDP while invoking Mastercard's 
    IDP API endpoints. For more information on this unique identifier please refer at [Mastercard IDP API Reference app usage](https://developer.mastercard.com/mastercard-id-for-idp/documentation/reference-app/).

### Integrating with OpenAPI Generator <a name="integrating-with-openapi-generator"></a>
[OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator) generates API client libraries from [OpenAPI Specs](https://github.com/OAI/OpenAPI-Specification). 
It provides generators and library templates for supporting multiple languages and frameworks.

See also:
* [OpenAPI Generator (maven Plugin)](https://mvnrepository.com/artifact/org.openapitools/openapi-generator-maven-plugin)
* [OpenAPI Generator (executable)](https://mvnrepository.com/artifact/org.openapitools/openapi-generator-cli)
* [CONFIG OPTIONS for java](https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/java.md)

#### OpenAPI Generator Plugin Configuration <a name="openAPI_generator_plugin_configuration"></a>
```xml
<!-- https://mvnrepository.com/artifact/org.openapitools/openapi-generator-maven-plugin -->
<plugin>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator-maven-plugin</artifactId>
    <version>${openapi-generator.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <inputSpec>${project.basedir}/src/main/resources/mastercard-idservice-reference_api_spec.yaml</inputSpec>
                <generatorName>java</generatorName>
                <library>okhttp-gson</library>
                <generateApiTests>false</generateApiTests>
                <generateModelTests>false</generateModelTests>
                <configOptions>
                    <sourceFolder>src/gen/main/java</sourceFolder>
                    <dateLibrary>java8</dateLibrary>
                </configOptions>
            </configuration>
        </execution>
    </executions>
</plugin>
```

#### Generating The API Client Sources <a name="generating_the_API_client_sources"></a>
Now that you have all the dependencies you need, you can generate the sources. To do this, use one of the following two methods:

`Using IDE`
* **Method 1**<br/>
  In IntelliJ IDEA, open the Maven window **(View > Tool Windows > Maven)**. Click the icons `Reimport All Maven Projects` and `Generate Sources and Update Folders for All Projects`

* **Method 2**<br/>

  In the same menu, navigate to the commands **({Project name} > Lifecycle)**, select `clean` and `compile` then click the icon `Run Maven Build`. 

`Using Terminal`
* Navigate to the root directory of the project within a terminal window and execute `mvn clean compile` command.

### Test Case Execution <a name="test-case-execute"></a>
Navigate to the test package and right click to  `Run All Tests`

### Use cases <a name="use-cases"></a>

Main use cases in ID for Identity Providers Reference APIs are Personal Data Storage, SMS One Time Password, Email One Time Password, Document Verification, Initiate Authentications, Re-Authentication, Claims Sharing, Audit Events, User profiles                                                               

Below are the different APIs available in ID for Identity Providers Reference application:

## API Reference <a name="api-reference"></a>

### Authorization <a name="authorization"></a>
The `com.mastercard.dis.mids.reference.config` package will provide you API client. This class will take care of adding the correct `Authorization` header before sending the request.

### Request Examples <a name="request-examples"></a>
You can change the default input passed to APIs, modify values in following file:
* `com.mastercard.dis.mids.reference.util.Constants`

### Recommendation <a name="recommendation"></a>
It is recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Support <a name="support"></a>
If you would like further information, please send an email to `IDservicepilothelp@mastercard.com`
- For information regarding licensing, refer to the [LICENSE](LICENSE.md).
- For copyright information, refer to the [COPYRIGHT](COPYRIGHT.md).
- For instructions on how to contribute to this project, refer to the [CONTRIBUTING](CONTRIBUTING.md).
- For changelog information, refer to the [CHANGELOG](CHANGELOG.md).

## License <a name="license"></a>
Copyright 2023 Mastercard
 
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.