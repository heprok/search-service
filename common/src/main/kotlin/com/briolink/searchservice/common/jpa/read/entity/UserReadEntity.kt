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

@Table(name = "User", schema = "read")
@Entity
class UserReadEntity(
    @Id
    @Type(type = "pg-uuid")
    @Column(name = "id", nullable = false)
    val id: UUID
) : BaseReadEntity() {

    @Column(name = "full_name", nullable = false, length = 255)
    lateinit var fullName: String

    @Type(type = "pg-uuid")
    @Column(name = "industry_id")
    var industryId: UUID? = null

    @Type(type = "pg-uuid")
    @Column(name = "current_place_of_work_company_id")
    var currentPlaceOfWorkCompanyId: UUID? = null

    @Type(type = "uuid-array")
    @Column(name = "previous_place_of_work_company_ids", nullable = false, columnDefinition = "uuid[]")
    var previousPlaceOfWorkCompanyIds: MutableList<UUID> = mutableListOf()

    @Type(type = "pg-uuid")
    @Column(name = "position_title_id")
    var positionTitleId: UUID? = null

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

    var keywordsSearch: UserKeywordsSearch
        get() = UserKeywordsSearch(_keywordsSearch)
        set(value) {
            _keywordsSearch = value.toString()
        }

    @Type(type = "jsonb")
    @Column(name = "data", nullable = false, columnDefinition = "jsonb")
    lateinit var data: Data

    data class Data(
        @JsonProperty
        var user: User,
        @JsonProperty
        var titlePosition: String? = null,
        @JsonProperty
        var industryName: String? = null,
        @JsonProperty
        var currentPlaceOfWorkCompany: PlaceOfWork? = null,
        @JsonProperty
        var previousPlaceOfWorkCompanies: ArrayList<PlaceOfWork>
    )

    data class PlaceOfWork(
        @JsonProperty
        var companyId: UUID,
        @JsonProperty
        var companyName: String,
        @JsonProperty
        var slug: String,
        @JsonProperty
        var logo: String
    )

    data class User(
        @JsonProperty
        var firstName: String,
        @JsonProperty
        var lastName: String,
        @JsonProperty
        var description: String? = null,
        @JsonProperty
        var location: LocationInfoDto? = null,
        @JsonProperty
        var slug: String,
        @JsonProperty
        var image: URL? = null
    )
}

data class UserKeywordsSearch(val stringKeywords: String) {
    var currentPlaceCompanyName: String
    var industryName: String
    var location: String
    var positionTitle: String
    var description: String

    init {
        val keywords = stringKeywords.split("~;~")
        if (keywords.isEmpty()) {
            currentPlaceCompanyName = ""
            industryName = ""
            location = ""
            positionTitle = ""
            description = ""
        } else {
            if (keywords.count() != 5) throw Exception("Wrong number of arguments in $stringKeywords must be 6 (currentPlaceCompanyName~;~industryName~;~location~;~positionTitle~;~description") // ktlint-disable max-line-length
            currentPlaceCompanyName = keywords[0]
            industryName = keywords[1]
            location = keywords[2]
            positionTitle = keywords[3]
            description = keywords[4]
        }
    }

    constructor(
        currentPlaceCompanyName: String = "",
        industryName: String = "",
        location: String = "",
        positionTitle: String = "",
        description: String = "",
    ) : this("$currentPlaceCompanyName~;~$industryName~;~$location~;~$positionTitle~;~$description")

    override fun toString(): String {
        return "$currentPlaceCompanyName~;~$industryName~;~$location~;~$positionTitle~;~$description"
    }
}
