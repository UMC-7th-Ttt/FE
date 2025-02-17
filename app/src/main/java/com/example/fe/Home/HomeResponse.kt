package com.example.fe.Home

import com.google.gson.annotations.SerializedName

data class HomeResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: HomeResult
)

data class HomeResult(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileUrl") val profileUrl: String,
    @SerializedName("mainBannerList") val mainBannerList: List<BannerItem>,
    @SerializedName("bookClubList") val bookClubList: List<BookClubItem>,
    @SerializedName("bookLetterList") val bookLetterList: List<BookLetterItem>,
    @SerializedName("remindReviewList") val remindReviewList: List<RemindReviewItem>
)


data class BookClubItem(
    @SerializedName("bookClubId") val bookClubId: Int,
    @SerializedName("bookId") val bookId: Int,
    @SerializedName("bookTitle") val bookTitle: String,
    @SerializedName("bookCover") val bookCover: String,
    @SerializedName("completionRate") val completionRate: Int
)

data class BookLetterItem(
    @SerializedName("bookLetterId") val bookLetterId: Int,
    @SerializedName("bookLetterTitle") val bookLetterTitle: String,
    @SerializedName("bookList") val bookList: List<BookItem>
)

data class BookItem(
    @SerializedName("bookCoverImg") val bookCoverImg: String
)

data class RemindReviewItem(
    @SerializedName("bookId") val bookId: Int,
    @SerializedName("bookTitle") val bookTitle: String,
    @SerializedName("bookCover") val bookCover: String,
    @SerializedName("writeDate") val writeDate: String,
    @SerializedName("content") val content: String
)
