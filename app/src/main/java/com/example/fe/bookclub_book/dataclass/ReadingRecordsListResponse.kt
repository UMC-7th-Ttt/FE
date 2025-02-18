package com.example.fe.bookclub_book.dataclass

data class ReadingRecordsListResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val readingRecords: List<ReadingRecord>
    ) {
        data class ReadingRecord(
            val id: Int,
            val imgUrl: String,
            val memberNickName: String
        )
    }
}
