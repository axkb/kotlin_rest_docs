### MockMvc REST documentation generation

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

### Instruction to run

1. Run the gradle build/testClasses to generate adoc files in build/generated-snippets (default path configuration)

    ```
    gradlew build
    ```

2. There are 3 options to generate documentation: openapi3, openapi, postman.
<br>openapi3 more preferable cause of test automation requirements  
<br>use one of the following depending on your needs

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

### Useful methods:

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

