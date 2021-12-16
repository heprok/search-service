package com.briolink.searchservice.api.service.company.dto

import com.briolink.searchservice.api.dto.SortDirectionEnum

data class CompanySortDto(
    val key: CompanySortKeyEnum,
    val direction: SortDirectionEnum
)

enum class CompanySortKeyEnum(val field: String) {
    Name("name"),
    NumberOfVerification("numberOfVerification")
}
