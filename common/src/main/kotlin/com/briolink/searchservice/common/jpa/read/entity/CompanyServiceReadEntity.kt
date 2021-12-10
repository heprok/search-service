package com.briolink.searchservice.common.jpa.read.entity

import com.briolink.searchservice.common.dto.location.LocationInfoDto
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Type
import java.net.URL
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
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
    var company_id: UUID
) : BaseReadEntity() {

    @Column(name = "name", nullable = false, length = 255)
    lateinit var name: String

    @Type(type = "pg-uuid")
    @Column(name = "industry_id")
    var industryId: UUID? = null

    @Column(name = "number_of_uses", nullable = false)
    var numberOfUses: Int = 0

    @Column(name = "country_id")
    var countryId: Int? = null

    @Column(name = "state_id")
    var stateId: Int? = null

    @Column(name = "city_id")
    var cityId: Int? = null

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
}

data class CompanyServiceKeywordsSearch(val stringKeywords: String) {
    var serviceName: String
    var companyName: String
    var industryName: String
    var location: String
    var description: String

    init {
        val keywords = stringKeywords.split("~;~")
        if (keywords.isEmpty()) {
            serviceName = ""
            companyName = ""
            industryName = ""
            location = ""
            description = ""
        } else {
            if (keywords.count() != 5) throw Exception("Wrong number of arguments in $stringKeywords must be 5 (serviceName~;~companyMame~;~industryName~;~location~;~description)") // ktlint-disable max-line-length
            serviceName = keywords[0]
            companyName = keywords[1]
            industryName = keywords[2]
            location = keywords[3]
            description = keywords[4]
        }
    }

    constructor(
        serviceName: String,
        companyName: String,
        industryName: String = "",
        location: String = "",
        description: String = "",
    ) : this("$serviceName~;~$companyName~;~$industryName~;~$location~;~$description")

    override fun toString(): String {
        return "$serviceName~;~$companyName~;~$industryName~;~$location~;~$description"
    }
}
