package com.example.fe.bookclub_book.dataclass

data class BookclubByMonth(
    val month:String = ""
)

data class BookclubMember(
    val name:String = "",
    val coverImg:Int? = null
)

data class BookclubParticipation(
    val book:String = ""
)
