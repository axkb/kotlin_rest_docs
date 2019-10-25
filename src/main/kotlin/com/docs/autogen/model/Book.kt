package com.docs.autogen.model

data class Book (
        var name: String,
        var pages: Int,
        var genre: Genre,
        var author: Author)

enum class Genre(name: String) {
    DETECTIVE("Detective"),
    HORROR("Horror"),
    SCIENCE_FICTION("Science Fiction"),
    FANTASY("Fantasy")
}