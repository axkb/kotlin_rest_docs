package com.docs.autogen.controller

import com.docs.autogen.model.Author
import com.docs.autogen.model.Book
import com.docs.autogen.model.Genre
import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


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
                .andDo(document("book-create", resource(
                        ResourceSnippetParameters.builder()
                                .description("Create a book")
                                .requestFields(fields)
                                .responseFields(fields)
                                .build()
                )))
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
                                .description("Get a book")
                                .pathParameters(bookPathParameters)
                                .responseFields(fields)
                                .build()
                )))
    }
}
//    @Test
//    @Throws(Exception::class)
//    fun getBookWithError() {
//        mockMvc
////                .perform(get("/api/v1/books/{bookName}", "unknown22")
////                .contentType(MediaType.APPLICATION_JSON)
////                .accept(MediaType.APPLICATION_JSON))
//                .perform(get("/error")
//                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404)
//                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/api/v1/books/{bookName}")
//                        .requestAttr(RequestDispatcher.ERROR_MESSAGE,
//                                "The tag 'http://localhost:8080/tags/123' does not exist"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isNotFound)
//                .andExpect(jsonPath("timestamp", `is`(notNullValue())))
//                .andExpect(jsonPath("status", `is`(404)))
//                .andExpect(jsonPath("error", `is`("Not Found")))
//                .andExpect(jsonPath("message", `is`("Book with name: unknown, was not found")))
//                .andExpect(jsonPath("path", `is`("/api/v1/books/unknown")))
//                .andDo(document("Error Response", resource(
//                        ResourceSnippetParameters.builder()
//                                .pathParameters(bookPathParameters)
//                                .responseFields(exceptionFields)
//                                .build()
//                        )))
//    }
