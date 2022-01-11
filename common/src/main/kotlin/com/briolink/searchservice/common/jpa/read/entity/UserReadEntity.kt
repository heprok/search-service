package com.briolink.searchservice.common.jpa.read.entity

import com.briolink.searchservice.common.dto.location.LocationInfoDto
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

    @Column(name = "job_position_title", length = 255)
    var jobPositionTitle: String? = null

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
        var industryName: String? = null,
        @JsonProperty
        var currentPlaceOfWorkCompany: PlaceOfWork? = null,
        @JsonProperty
        var previousPlaceOfWorkCompanies: ArrayList<PlaceOfWork> = arrayListOf()
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class PlaceOfWork(
        @JsonProperty
        var companyId: UUID,
        @JsonProperty
        var userJobPositionId: UUID,
        @JsonProperty
        var jobPositionTitle: String,
        @JsonProperty
        var companyName: String,
        @JsonProperty
        var slug: String,
        @JsonProperty
        var logo: URL? = null
    )

    data class User(
        @JsonProperty
        var id: UUID,
        @JsonProperty
        var firstName: String,
        @JsonProperty
        var lastName: String,
        @JsonProperty
        var description: String? = null,
        @JsonProperty
        var locationInfo: LocationInfoDto? = null,
        @JsonProperty
        var location: String? = null,
        @JsonProperty
        var slug: String,
        @JsonProperty
        var image: URL? = null
    )

    @PrePersist
    @PreUpdate
    fun genKeywordSearch() {
        keywordsSearch = UserKeywordsSearch(
            fullName = fullName,
            currentPlaceCompanyName = data.currentPlaceOfWorkCompany?.companyName,
            industryName = data.industryName,
            location = data.user.locationInfo?.toString(),
            positionTitle = data.currentPlaceOfWorkCompany?.jobPositionTitle,
            description = data.user.description,
        )
        data.user.location = data.user.locationInfo?.toString()
    }
}

data class UserKeywordsSearch(val stringKeywords: String) {
    var fullName: String
    var currentPlaceCompanyName: String
    var industryName: String
    var location: String
    var positionTitle: String
    var description: String

    init {
        val keywords = stringKeywords.split("~;~")
        if (keywords.isEmpty()) {
            fullName = ""
            currentPlaceCompanyName = ""
            industryName = ""
            location = ""
            positionTitle = ""
            description = ""
        } else {
            if (keywords.count() != 6) throw Exception("Wrong number of arguments in $stringKeywords must be 7 (fullName~;~currentPlaceCompanyName~;~industryName~;~location~;~positionTitle~;~description") // ktlint-disable max-line-length
            fullName = keywords[0]
            currentPlaceCompanyName = keywords[1]
            industryName = keywords[2]
            location = keywords[3]
            positionTitle = keywords[4]
            description = keywords[5]
        }
    }

    constructor(
        fullName: String? = null,
        currentPlaceCompanyName: String? = null,
        industryName: String? = null,
        location: String? = null,
        positionTitle: String? = null,
        description: String? = null,
    ) : this(
        fullName.orEmpty() +
            "~;~" + currentPlaceCompanyName.orEmpty() +
            "~;~" + industryName.orEmpty() +
            "~;~" + location.orEmpty() +
            "~;~" + positionTitle.orEmpty() +
            "~;~" + description.orEmpty()
    )

    override fun toString(): String {
        return "$fullName~;~$currentPlaceCompanyName~;~$industryName~;~$location~;~$positionTitle~;~$description"
    }
}
