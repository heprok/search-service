package com.briolink.searchservice.api.service.companyservice.dto

import com.briolink.searchservice.api.dto.SortDirectionEnum

data class CompanyServiceSortDto(
    val key: CompanyServiceSortKeyEnum,
    val direction: SortDirectionEnum
)

enum class CompanyServiceSortKeyEnum(val field: String) {
    Price("price"),
    NumberOfUses("numberOfUses")
}
