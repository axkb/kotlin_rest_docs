package com.docs.autogen.controller

import com.docs.autogen.exception.NotFoundException
import com.docs.autogen.model.Author
import com.docs.autogen.model.Book
import com.docs.autogen.model.Genre
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.lang.NonNull
import org.springframework.web.bind.annotation.*
import java.time.Instant

@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/api/v1/books")
class BookController {

    val preloadedAuthor = Author(0, "PreloadedName", "PreloadedSurname")
    val preloadedBook = Book("Preloaded book", 666, Genre.HORROR, preloadedAuthor)

    private val bookIdToBook = mutableMapOf<String, Book>(
            preloadedBook.name to preloadedBook
    )

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
        } else throw NotFoundException("Book with name: $bookName, was not found")
    }

    data class ApiError(val error: String,
                        val message: String,
                        val status: String,
                        val timestamp: Instant = Instant.now())

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException::class)
//    fun handleValidationExceptions(MethodArgumentNotValidException ex) : List<ApiError> {
//        return ex.getBindingResult()
//                .getAllErrors()
//                .map { ApiError(it.getCodes(), it.getDefaultMessage()) }
//                .toList()
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ConstraintViolationException::class)
//    fun handleValidationExceptions (ConstraintViolationException ex) : List<ApiError> {
//        return ex.getConstraintViolations()
//                .map { ApiError(it.getPropertyPath().toString(), it.getMessage()) }
//                .toList();
//    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundExceptions (ex: NotFoundException) : List<ApiError> {
        return listOf(ApiError("NotFoundException", ex.message!!, "404"))
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleOtherException (ex: Exception): List<ApiError> {
        return listOf(ApiError(ex.javaClass.canonicalName, ex.message!!, "500"));
    }
}