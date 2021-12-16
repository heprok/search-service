package com.briolink.searchservice.api.service.user.dto

import com.briolink.searchservice.api.dto.SortDirectionEnum

data class UserSortDto(
    val key: UserSortKeyEnum,
    val direction: SortDirectionEnum
)

enum class UserSortKeyEnum(val field: String) {
    Name("fullName"),
    NumberOfVerification("numberOfVerification")
}
