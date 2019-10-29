# MockMvc REST documentation generation

- [Build Configuration](#build-configuration)
- [Test Configuration](#test-configuration)
- [Instruction How to run](#how-to-run)
- [Useful methods](#useful-methods)
- [Documentation Links](#documentation-links)

## Build Configuration:

Configure the Gradle build kts file with repositories, plugins, generation tasks:

```$kotlin
buildscript {
    val epagesRestDocsVer = "0.9.5"
    repositories {
        mavenCentral()
        jcenter()
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        ...
        classpath("com.epages:restdocs-api-spec-gradle-plugin:$epagesRestDocsVer")
    }
}

apply {
    plugin("com.epages.restdocs-api-spec")
}

dependencies {
    val junitJupiterVersion = "5.5.2"

    testCompile("com.google.guava:guava:23.0") // ?
    testCompile("com.epages:restdocs-api-spec-mockmvc:0.9.5")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitJupiterVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter:${junitJupiterVersion}")
}

repositories {
    mavenCentral()
    jcenter()
}

val snippetsDir = file("build/generated-snippets")
val apiHost = "localhost"
val apiPort = "8080"
val apiVersion = "0.1.0"
val apiTitle = "Custom API"
val apiDescription = "Sample demonstrating restdocs-api-spec"
val apiFormat = "json"

configure<com.epages.restdocs.apispec.gradle.OpenApiExtension> {
    host = "$apiHost:$apiPort"
    basePath = "/api/v1/"
    title = apiTitle
    description = apiDescription
    version = apiVersion
    format = apiFormat
}

configure<com.epages.restdocs.apispec.gradle.OpenApi3Extension> {
    setServer("http://$apiHost:$apiPort")
    title = apiTitle
    description = apiDescription
    version = apiVersion
    format = apiFormat
    tagDescriptionsPropertiesFile = "src/test/resources/tags.yaml"
}

configure<com.epages.restdocs.apispec.gradle.PostmanExtension> {
    title = apiTitle
    version = apiVersion
    baseUrl = "http://$apiHost:$apiPort"
}
```

## Test Configuration:

To enable the configuration use either auto configuration with annotations:

```
@AutoConfigureMockMvc
@AutoConfigureRestDocs
```
    
Or the manual configuration:
1. add extend with to your test class 
    ```
    @ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
    ```
  
2. add set up function in your test
    ```
    @BeforeEach
    fun setUp(webApplicationContext: WebApplicationContext, restDocumentation: RestDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
                .alwaysDo<DefaultMockMvcBuilder>(MockMvcRestDocumentationWrapper.document(
                        "{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .build()
    }
    ```  

## Instruction How to run

1. Create the tags.yaml configuration file. It will be used as a map <String, String>
   <br>Key is the name of the controller displayed on the swagger.html page
   <br>Value is the description of this controller, also will be displayed on the right side of the swagger.html page
   <br>For example:
       ```
       book-controller: Book Controller
       author-controller: Author Controller
       ``` 
   Two controllers will be displayed, each can be expanded and the list of related endpoints will be provided. 

2. Run the gradle build/testClasses to generate adoc files in build/generated-snippets (default path configuration)

    ```
    gradlew build
    ```

3. There are 3 options to generate documentation: openapi3, openapi, postman.
<br>openapi3 more preferable cause of test automation requirements  
<br>The destination output folder is build/api-spec/
<br>use one of the following depending on your needs. 

    ```
    gradlew openapi3
    // will generate OPEN API 3.0 specification
    ```

    ```
    gradlew openapi
    // will generate OPEN API 2.0 specification
    ```

    ```
    gradlew postman
    // will generate POSTMAN collections
    ```

4. Put the generated file into the resource folder:

    ```
    resources/static/openapi3.json
    ```

5. Provide the location of the file to the property config:
    ```
    swagger-ui.location=/openapi3.json
    ```

UPD: since swagger 2.9.2 cannot render examples in OpenAPI3 use lates swagger with docker:

```shell
# copy json into your folder and run:
docker run -p 80:8080 -v c:/Users:/data -e SWAGGER_JSON=/data/openapi3.json swaggerapi/swagger-ui
```  

## Useful methods:

* org.springframework.restdocs.request.RequestDocumentation.**subsectionWithPath** - 
use to document just part of the fields

* org.springframework.restdocs.request.RequestDocumentation.**requestParameters** - 
use to document query parameters 

* org.springframework.restdocs.request.RequestDocumentation.**pathParameters** - 
use to document path parameters

* org.springframework.restdocs.request.RequestDocumentation.**requestFields/responseFields** - 
use to document we have to provide all the fields that in bodies (request/response)

* org.springframework.restdocs.headers.HeaderDocumentation.**requestHeaders** - 
Documentation of the request headers 

* org.springframework.restdocs.headers.HeaderDocumentation.**responseHeaders** - 
Documentation of the response headers

* com.epages.restdocs.apispec.ResourceSnippetParameters.**tag** - 
Parameter that responsible for choosing which Swagger block will contain the resource

## Documentation Links: 

* [Spring RestDocs Reference](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/)
* [Rest Docs Extension for Openapi Spec](https://github.com/ePages-de/restdocs-api-spec)
* [Openapi Specification Guide](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.0.md)