package com.briolink.searchservice.common.jpa.read.repository

import com.briolink.searchservice.common.jpa.read.entity.CompanyServiceReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface CompanyServiceReadRepository : JpaRepository<CompanyServiceReadEntity, UUID> {
    @Modifying
    @Query(
        """UPDATE CompanyServiceReadEntity c
           SET c.countryId = :countryId, 
               c.stateId = :stateId, 
               c.cityId = :cityId, 
               c.industryId = :industryId,
               c.occupationId = :occupationId,
               c.data = function('jsonb_sets', c.data,
                    '{company,name}', :name, text,
                    '{company,slug}', :slug, text,
                    '{company,logo}', :logo, text,
                    '{industryName}', :industryName, text,
                    '{occupationName}', :occupationName, text,
                    '{location}', :locationJson, jsonb
           ), 
                c._keywordsSearch = concat_ws('~;~', c.name, :name, :occupationName, :industryName, :location, jsonb_get(c.data, description))
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
        @Param("industryName") industryName: String?,
        @Param("occupationId") occupationId: UUID?,
        @Param("occupationName") occupationName: String?
    )

    @Modifying
    @Query("DELETE FROM CompanyServiceReadEntity c WHERE c.id = ?1")
    override fun deleteById(id: UUID)

    @Modifying
    @Query("UPDATE CompanyServiceReadEntity c SET c.hidden = ?2 WHERE c.id = ?1")
    fun setHidden(id: UUID, hidden: Boolean)

    @Modifying
    @Query("UPDATE CompanyServiceReadEntity c SET c.numberOfUses = ?2 WHERE c.id = ?1")
    fun updateNumberOfUses(id: UUID, numberOfUses: Int)
}
