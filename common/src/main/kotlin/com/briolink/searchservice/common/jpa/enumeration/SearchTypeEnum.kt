package com.briolink.searchservice.common.jpa.enumeration

import com.fasterxml.jackson.annotation.JsonValue

enum class SearchTypeEnum(@JsonValue val value: Int) {
    CompanyIndustryName(0),
    CompanyOccupationName(1),
    CompanyRoleName(2),
    CompanyServiceName(3),
    CompanyName(4),
    JobPositionTitleName(5),
    FullNameUser(6);

    companion object {
        private val map = values().associateBy(SearchTypeEnum::value)
        fun fromInt(type: Int): SearchTypeEnum = map[type]!!
    }
}
