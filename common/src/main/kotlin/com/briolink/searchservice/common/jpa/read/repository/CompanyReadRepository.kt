package com.briolink.searchservice.common.jpa.read.repository

import com.briolink.lib.location.model.LocationMinInfo
import com.briolink.searchservice.common.jpa.projection.IdNameProjection
import com.briolink.searchservice.common.jpa.read.entity.CompanyReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface CompanyReadRepository : JpaRepository<CompanyReadEntity, UUID> {
    @Query("SELECT jsonb_get(c.data, location) FROM CompanyReadEntity c WHERE c.id = ?1")
    fun getLocationInfoByCompanyId(id: UUID): LocationMinInfo?

    @Query("SELECT c.industryId as id, jsonb_get(c.data, industryName) as name FROM CompanyReadEntity c WHERE c.id = ?1")
    fun getIndustryByCompanyId(id: UUID): IdNameProjection?

    @Query("SELECT c.occupationId as id, jsonb_get(c.data, occupationName) as name FROM CompanyReadEntity c WHERE c.id = ?1")
    fun getOccupationByCompanyId(id: UUID): IdNameProjection?
}
