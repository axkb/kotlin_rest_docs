package com.docs.autogen.controller

import com.docs.autogen.exception.NotFoundException
import com.docs.autogen.model.Author
import com.docs.autogen.model.Book
import com.docs.autogen.model.Genre
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import javax.servlet.RequestDispatcher


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@TestMethodOrder(MethodOrderer.Alphanumeric::class)
class BookControllerTest : BaseTest() {

    val author = Author(1, "Stephen", "King")
    val book = Book("It", 666, Genre.HORROR, author)

    @Test
    fun createBook() {
        val fields = ArrayList<FieldDescriptor>(bookFields)
        fields.addAll(authorFields)

        mockMvc.perform(post("/api/v1/books")
                .content(objectMapper.writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated)
                .andDo(MockMvcRestDocumentationWrapper.document(identifier = "book-create",
                        snippets = *arrayOf(resource(
                                ResourceSnippetParameters.builder()
                                        .tag("book-controller")
                                        .description("Create a book")
                                        .requestFields(fields)
                                        .responseFields(fields)
                                        .build()))
                ))
    }

    @Test
    fun getBook() {
        val fields = ArrayList<FieldDescriptor>(bookFields)
        fields.addAll(authorFields)

        mockMvc.perform(get("/api/v1/books/{bookName}", "Preloaded book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andDo(document("book-get", resource(
                        ResourceSnippetParameters.builder()
                                .tag("book-controller")
                                .description("Get a book")
                                .pathParameters(bookPathParameters)
                                .responseFields(fields)
                                .build()
                )))
    }

//    @Test
    @Throws(Exception::class)
    fun getBookWithError() {
        mockMvc.dispatcherServlet.setDetectAllHandlerExceptionResolvers(false)
        mockMvc.perform(get("/api/v1/books/unknown"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("timestamp", `is`(notNullValue())))
                .andExpect(jsonPath("status", `is`(404)))
//                .andExpect()
//                .andExpect(jsonPath("error", `is`("Not Found")))
//                .andExpect(jsonPath("message", `is`("Book with name: unknown, was not found")))
//                .andExpect(jsonPath("path", `is`("/api/v1/books/unknown")))
                .andDo(MockMvcRestDocumentationWrapper.document(identifier = "Error Response",
                        snippets = *arrayOf(resource(
                                ResourceSnippetParameters.builder()
                                        .tag("book-controller")
//                                        .pathParameters(bookPathParameters)
                                        .responseFields(exceptionFields)
                                        .build())))
                )
    }
}