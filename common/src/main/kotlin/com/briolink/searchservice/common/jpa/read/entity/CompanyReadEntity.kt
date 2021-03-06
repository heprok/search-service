package com.briolink.searchservice.common.jpa.read.entity

import com.briolink.lib.location.model.LocationMinInfo
import com.briolink.searchservice.common.jpa.enumeration.CompanyRoleTypeEnum
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Type
import java.net.URL
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Table

@Table(name = "company", schema = "read")
@Entity
class CompanyReadEntity(
    @Id
    @Type(type = "pg-uuid")
    @Column(name = "id", nullable = false)
    val id: UUID
) : BaseReadEntity() {

    @Column(name = "name", nullable = false, length = 255)
    lateinit var name: String

    @Type(type = "pg-uuid")
    @Column(name = "industry_id")
    var industryId: UUID? = null

    @Type(type = "pg-uuid")
    @Column(name = "occupation_id")
    var occupationId: UUID? = null

    @Type(type = "uuid-array")
    @Column(name = "company_role_ids", columnDefinition = "uuid[]")
    var companyRoleIds: List<UUID> = listOf()

    @Column(name = "number_of_verification", nullable = false)
    var numberOfVerification: Int = 0

    @Column(name = "country_id")
    var countryId: Int? = null

    @Column(name = "state_id")
    var stateId: Int? = null

    @Column(name = "city_id")
    var cityId: Int? = null

    @Column(name = "keywords_search")
    private lateinit var _keywordsSearch: String

    var keywordsSearch: CompanyKeywordsSearch
        get() = CompanyKeywordsSearch(_keywordsSearch)
        set(value) {
            _keywordsSearch = value.toString()
        }

    @Type(type = "jsonb")
    @Column(name = "data", nullable = false, columnDefinition = "jsonb")
    lateinit var data: Data

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Data(
        @JsonProperty
        var slug: String,
        @JsonProperty
        var website: URL? = null,
        @JsonProperty
        var shortDescription: String? = null,
        @JsonProperty
        var location: LocationMinInfo? = null,
        @JsonProperty
        var companyRoles: List<CompanyRole> = listOf(),
        @JsonProperty
        var logo: URL? = null,
        @JsonProperty
        var industryName: String? = null,
        @JsonProperty
        var occupationName: String? = null,
    )

    data class CompanyRole(
        @JsonProperty
        val id: UUID,
        @JsonProperty
        val name: String,
        @JsonProperty
        val type: CompanyRoleTypeEnum
    )

    @PrePersist
    @PreUpdate
    fun genKeywordSearch() {
        keywordsSearch = CompanyKeywordsSearch(
            companyName = name,
            industryName = data.industryName,
            occupationName = data.occupationName,
            location = data.location?.toString(),
            shortDescription = data.shortDescription,
            companyRoles = data.companyRoles.map { it.name }
        )
    }
}

data class CompanyKeywordsSearch(val stringKeywords: String) {
    var companyName: String
    var industryName: String
    var occupationName: String
    var location: String
    var shortDescription: String
    var companyRoles: List<String>

    init {
        val keywords = stringKeywords.split("~;~")
        if (keywords.isEmpty()) {
            companyName = ""
            industryName = ""
            occupationName = ""
            location = ""
            shortDescription = ""
            companyRoles = listOf()
        } else {
            if (keywords.count() != 6) throw Exception("Wrong number of arguments in $stringKeywords must be 6 (companyName~;~industryName~;~occupationName~;~location~;~description~;~companyRoles") // ktlint-disable max-line-length
            companyName = keywords[0]
            industryName = keywords[1]
            occupationName = keywords[2]
            location = keywords[3]
            shortDescription = keywords[4]
            companyRoles = keywords[5].split(":")
        }
    }

    constructor(
        companyName: String,
        industryName: String? = null,
        occupationName: String? = null,
        location: String? = null,
        shortDescription: String? = null,
        companyRoles: List<String> = listOf()
    ) : this(
        "$companyName~;~" +
            "${industryName.orEmpty()}~;~" +
            "${occupationName.orEmpty()}~;~" +
            "${location.orEmpty()}~;~" +
            "${shortDescription.orEmpty()}~;~" +
            companyRoles.joinToString { ":" }
    )

    override fun toString(): String {
        return "$companyName~;~$industryName~;~$occupationName~;~$location~;~$shortDescription~;~ ${companyRoles.joinToString { ":" }}"
    }
}
