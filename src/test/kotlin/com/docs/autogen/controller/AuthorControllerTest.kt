package com.docs.autogen.controller

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceDocumentation
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@TestMethodOrder(MethodOrderer.Alphanumeric::class)
class AuthorControllerTest : BaseTest() {

    @Test
    fun createAuthor() {
        mockMvc.perform(post("/api/v1/authors")
                .content(objectMapper.writeValueAsString(author))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andDo(MockMvcRestDocumentationWrapper.document(identifier = "author-create",
                        snippets = *arrayOf(ResourceDocumentation.resource(
                                ResourceSnippetParameters.builder()
                                        .tag("author-controller")
                                        .description("Create an author")
                                        .requestFields(authorFields)
                                        .responseFields(authorFields)
                                        .build()))
                ))
    }

    @Test
    fun getAuthorList() {
        mockMvc.perform(get("/api/v1/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(document("author-get", ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                                .tag("author-controller")
                                .description("Get the list of the authors")
                                .responseFields(getAuthorsResponseFields)
                                .build()
                )))
    }

    @Test
    fun getAuthor() {
        mockMvc.perform(get("/api/v1/authors/{authorId}", 0L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(document("author-get-by-id", ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                                .tag("author-controller")
                                .description("Get a author by identifier.")
                                .pathParameters(authorPathParameters)
                                .responseFields(authorFields)
                                .build()
                )))
    }

    @Test
    fun deleteAuthor() {
        val fields = ArrayList<FieldDescriptor>(bookFields)
        fields.addAll(authorFields)

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/authors/{authorId}", "0")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(document("author-delete-by-id", ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                                .tag("author-controller")
                                .description("Delete author by identifier.")
                                .pathParameters(authorPathParameters)
                                .build()
                )))
    }
}