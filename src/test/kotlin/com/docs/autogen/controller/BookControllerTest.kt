package com.docs.autogen.controller

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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@TestMethodOrder(MethodOrderer.Alphanumeric::class)
class BookControllerTest : BaseTest() {

    @Test
    fun createBook() {
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
                                        .requestFields(bookFields)
                                        .responseFields(bookFields)
                                        .build()))
                ))
    }

    @Test
    fun getBookList() {
        mockMvc.perform(get("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andDo(document("book-get", resource(
                        ResourceSnippetParameters.builder()
                                .tag("book-controller")
                                .description("Get the list of the books")
                                .responseFields(getBooksResponseFields)
                                .build()
                )))
    }

    @Test
    fun getBook() {
        mockMvc.perform(get("/api/v1/books/{bookName}", "Preloaded book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andDo(document("book-get-by-name", resource(
                        ResourceSnippetParameters.builder()
                                .tag("book-controller")
                                .description("Get a book by name.")
                                .pathParameters(bookPathParameters)
                                .responseFields(bookFields)
                                .build()
                )))
    }

    @Test
    fun deleteBook() {
        val fields = ArrayList<FieldDescriptor>(bookFields)
        fields.addAll(authorFields)

        mockMvc.perform(delete("/api/v1/books/{bookName}", "Preloaded book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andDo(document("book-delete-by-name", resource(
                        ResourceSnippetParameters.builder()
                                .tag("book-controller")
                                .description("Delete book by name.")
                                .pathParameters(bookPathParameters)
                                .build()
                )))
    }

    @Test
    fun getBookWithError() {
        mockMvc.perform(get("/api/v1/books/{bookName}", "unknown"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound)
//                .andExpect(jsonPath("status", `is`(404)))
//                .andExpect()
//                .andExpect(jsonPath("error", `is`("Not Found")))
//                .andExpect(jsonPath("message", `is`("Book with name: unknown, was not found")))
//                .andExpect(jsonPath("path", `is`("/api/v1/books/unknown")))
                .andDo(MockMvcRestDocumentationWrapper.document(identifier = "{method-name}",
                        snippets = *arrayOf(resource(
                                ResourceSnippetParameters.builder()
                                        .tag("book-controller")
//                                        .pathParameters(bookPathParameters)
                                        .responseFields(exceptionFields)
                                        .build())))
                )
    }
}