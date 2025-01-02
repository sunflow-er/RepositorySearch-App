package com.masonk.repositorysearch.model

import com.google.gson.annotations.SerializedName

data class UserDto( // Data Transfer Object, 데이터를 한 번에 주지 않고 변환을 거쳐서 줌
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("items")
    val items: List<User>
)
