package com.briolink.searchservice.common.jpa.read.entity

import com.briolink.searchservice.common.dto.location.LocationInfoDto
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

@Table(name = "company_service", schema = "read")
@Entity
class CompanyServiceReadEntity(
    @Id
    @Type(type = "pg-uuid")
    @Column(name = "id", nullable = false)
    val id: UUID,

    @Type(type = "pg-uuid")
    @Column(name = "company_id", nullable = false)
    var companyId: UUID
) : BaseReadEntity() {

    @Column(name = "name", nullable = false, length = 255)
    lateinit var name: String

    @Type(type = "pg-uuid")
    @Column(name = "industry_id")
    var industryId: UUID? = null

    @Type(type = "pg-uuid")
    @Column(name = "occupation_id")
    var occupationId: UUID? = null

    @Column(name = "number_of_uses", nullable = false)
    var numberOfUses: Int = 0

    @Column(name = "price")
    var price: Double? = null

    @Column(name = "country_id")
    var countryId: Int? = null

    @Column(name = "state_id")
    var stateId: Int? = null

    @Column(name = "city_id")
    var cityId: Int? = null

    @Column(name = "hidden")
    var hidden: Boolean = false

    @Column(name = "keywords_search")
    private lateinit var _keywordsSearch: String

    var keywordsSearch: CompanyServiceKeywordsSearch
        get() = CompanyServiceKeywordsSearch(_keywordsSearch)
        set(value) {
            _keywordsSearch = value.toString()
        }

    @Type(type = "jsonb")
    @Column(name = "data", nullable = false, columnDefinition = "jsonb")
    lateinit var data: Data

    data class Data(
        @JsonProperty
        var slug: String,
        @JsonProperty
        var description: String? = null,
        @JsonProperty
        var location: LocationInfoDto? = null,
        @JsonProperty
        var image: URL? = null,
        @JsonProperty
        var industryName: String? = null,
        @JsonProperty
        var occupationName: String? = null,
        @JsonProperty
        var company: Company
    )

    data class Company(
        @JsonProperty
        val id: UUID,
        @JsonProperty
        val name: String,
        @JsonProperty
        val slug: String,
        @JsonProperty
        val logo: URL? = null
    )

    @PrePersist
    @PreUpdate
    fun genKeywordSearch() {
        keywordsSearch = CompanyServiceKeywordsSearch(
            serviceName = name,
            companyName = data.company.name,
            occupationName = data.occupationName,
            industryName = data.industryName,
            location = data.location?.toString(),
            description = data.description
        )
    }
}

data class CompanyServiceKeywordsSearch(val stringKeywords: String) {
    var serviceName: String
    var companyName: String
    var occupationName: String
    var industryName: String
    var location: String
    var description: String

    init {
        val keywords = stringKeywords.split("~;~")
        if (keywords.isEmpty()) {
            serviceName = ""
            companyName = ""
            occupationName = ""
            industryName = ""
            location = ""
            description = ""
        } else {
            if (keywords.count() != 6) throw Exception("Wrong number of arguments in $stringKeywords must be 5 (serviceName~;~companyName~;~occupationName~;~industryName~;~location~;~description)") // ktlint-disable max-line-length
            serviceName = keywords[0]
            companyName = keywords[1]
            occupationName = keywords[2]
            industryName = keywords[3]
            location = keywords[4]
            description = keywords[5]
        }
    }

    constructor(
        serviceName: String,
        companyName: String,
        industryName: String? = null,
        occupationName: String? = null,
        location: String? = null,
        description: String? = null,
    ) : this("$serviceName~;~$companyName~;~${occupationName.orEmpty()}~;~${industryName.orEmpty()}~;~${location.orEmpty()}~;~${description.orEmpty()}") // ktlint-disable max-line-length

    override fun toString(): String {
        return "$serviceName~;~$companyName~;~$occupationName~;~$industryName~;~$industryName~;~$location~;~$description"
    }
}
