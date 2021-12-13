package com.briolink.searchservice.common.jpa.read.repository

import com.briolink.searchservice.common.jpa.projection.ArrayIdNameProjection
import com.briolink.searchservice.common.jpa.read.entity.CompanyServiceReadEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface CompanyServiceReadRepository : JpaRepository<CompanyServiceReadEntity, UUID> {
    @Query("SELECT array_agg(cs.id), cs.name FROM CompanyServiceReadEntity cs WHERE (:query is null or function('fts_partial', cs.name, :query) = true) GROUP BY cs.name") // ktlint-disable max-line-length
    fun getAutoCompleteByName(
        @Param("query") query: String?,
        pageable: Pageable = Pageable.ofSize(10)
    ): List<ArrayIdNameProjection>

    @Modifying
    @Query(
        """UPDATE CompanyServiceReadEntity c
           SET c.countryId = :countryId, 
               c.stateId = :stateId, 
               c.cityId = :cityId, 
               c.industryId = :industryId,
               c.data = function('jsonb_sets', c.data,
                    '{company,name}', :name, text,
                    '{company,slug}', :slug, text,
                    '{company,logo}', :logo, text,
                    '{industryName}', :industryName, text,
                    '{location}', :locationJson, jsonb
           ), 
                c._keywordsSearch = concat_ws('~;~', c.name, :name, :industryName, :location, jsonb_get(c.data, description))
            WHERE c.companyId = :companyId""",
    )
    fun updateCompany(
        @Param("companyId") companyId: UUID,
        @Param("name") name: String,
        @Param("slug") slug: String,
        @Param("logo") logo: String?,
        @Param("countryId") countryId: Int?,
        @Param("stateId") stateId: Int?,
        @Param("cityId") cityId: Int?,
        @Param("locationJson") locationJson: String,
        @Param("location") location: String?,
        @Param("industryId") industryId: UUID?,
        @Param("industryName") industryName: String?
    )

    @Modifying
    @Query("DELETE FROM CompanyServiceReadEntity c WHERE c.id = ?1")
    override fun deleteById(id: UUID)

    @Modifying
    @Query("UPDATE CompanyServiceReadEntity c SET c.hidden = ?2 WHERE c.id = ?1")
    fun setHidden(id: UUID, hidden: Boolean)
}
