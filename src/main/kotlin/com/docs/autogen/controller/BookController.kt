package com.docs.autogen.controller

import com.docs.autogen.exception.NotFoundException
import com.docs.autogen.model.Author
import com.docs.autogen.model.Book
import com.docs.autogen.model.Genre
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.lang.NonNull
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/books")
class BookController {

    val preloadedAuthor = Author(0, "PreloadedName", "PreloadedSurname")
    val preloadedBook = Book("Preloaded book", 666, Genre.HORROR, preloadedAuthor)

    private val bookIdToBook = mutableMapOf<String, Book>(
            preloadedBook.name to preloadedBook
    )
    private val random: Random = Random()

    @PostMapping
    fun create(@RequestBody @NonNull book: Book): ResponseEntity<Book> {
        bookIdToBook[book.name] = book
        return ResponseEntity(book, HttpStatus.CREATED)
    }

    @GetMapping("/{bookName}")
    fun fetch(@PathVariable bookName: String): ResponseEntity<Any> {
        val book = bookIdToBook[bookName]
        return if (book != null) ResponseEntity.ok(book)
        else throw NotFoundException("Book with name: $bookName, was not found")
    }

    @GetMapping
    fun fetchAll(): ResponseEntity<List<Book>> = ResponseEntity.ok(bookIdToBook.values.toList())

    @GetMapping("/genres")
    fun fetchAllGenres(): ResponseEntity<List<String>> = ResponseEntity.ok(Genre.values().map { it.name })

    @DeleteMapping("/{bookName}")
    fun delete(@PathVariable bookName: String): ResponseEntity<Any> {
        val book = bookIdToBook[bookName]
        return if (book != null) {
//            bookIdToBook.remove(bookName)
            ResponseEntity.ok().build()
        }
        else throw NotFoundException("Book with name: $bookName, was not found")
    }
}