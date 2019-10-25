package com.docs.autogen.controller

import com.docs.autogen.model.Author
import org.springframework.http.ResponseEntity
import org.springframework.lang.NonNull
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.collections.HashMap

@RestController
@RequestMapping("/api/v1/authors")
class AuthorController {

    private val authorIdToAuthor = HashMap<Long, Author>()
    private val random: Random = Random()

    @PostMapping
    fun create(@RequestBody @NonNull author: Author): ResponseEntity<Author> {
        authorIdToAuthor[author.id] = author
        author.id = random.nextLong()
        return ResponseEntity.ok(author)
    }

    @GetMapping("/{authorId}")
    fun fetch(@PathVariable authorId: Long): ResponseEntity<Author> {
        val author = authorIdToAuthor[authorId]
        return if (author != null) ResponseEntity.ok(author)
        else ResponseEntity.notFound().build()
    }

    @GetMapping
    fun fetchAll(): ResponseEntity<List<Author>> = ResponseEntity.ok(authorIdToAuthor.values.toList())

    @DeleteMapping("/{authorId}")
    fun delete(@PathVariable authorId: Long): ResponseEntity<Unit> {
        val author = authorIdToAuthor[authorId]
        return if (author != null) {
            authorIdToAuthor.remove(authorId)
            ResponseEntity.ok().build()
        }
        else ResponseEntity.notFound().build()
    }
}