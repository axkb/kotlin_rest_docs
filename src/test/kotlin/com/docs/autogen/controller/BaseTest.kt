package com.docs.autogen.controller

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.web.servlet.MockMvc
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.web.context.WebApplicationContext
import org.junit.jupiter.api.BeforeEach
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.test.web.servlet.ResultHandler
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder


abstract class BaseTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    val objectMapper: ObjectMapper = ObjectMapper()

    val bookFields = listOf(
            fieldWithPath("name").description("The name of the book."),
            fieldWithPath("pages").description("Pages count."),
            fieldWithPath("genre").description("Related genre."),
            fieldWithPath("author").description("Author of the book.")
    )

    val authorFields = listOf(
            fieldWithPath("author.id").description("Author identifier."),
            fieldWithPath("author.name").description("The name of the author."),
            fieldWithPath("author.surname").description("The surname of the author.")
    )

    val exceptionFields = listOf(
            fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred"),
            fieldWithPath("status").description("The HTTP status code"),
            fieldWithPath("error").description("Error short message"),
            fieldWithPath("message").description("Error full message"),
            fieldWithPath("path").description("The path to which the request was made")
    )

    val bookPathParameters = listOf(
            parameterWithName("bookName").description("Book name")
    )

    val errorBookExample: String = """
        {
            "timestamp": "2019-10-25T11:59:50.903+0000",
            "status": 404,
            "error": "Not Found",
            "message": "Book with name: unknown, was not found",
            "path": "/api/v1/books/unknown"
        }
    """.trimIndent()

//    @BeforeEach
//    fun setUp(webApplicationContext: WebApplicationContext, restDocumentation: RestDocumentationContextProvider) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
//                .alwaysDo<DefaultMockMvcBuilder>(MockMvcRestDocumentationWrapper.document(
//                        "{method-name}",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint())))
//                .build()
//    }

    fun document(identifier: String, snippet: Snippet): RestDocumentationResultHandler {
//        return MockMvcRestDocumentationWrapper.document(identifier, "", false, snippet)
        return MockMvcRestDocumentationWrapper.document(identifier = identifier, snippets = *arrayOf(snippet))
    }
}