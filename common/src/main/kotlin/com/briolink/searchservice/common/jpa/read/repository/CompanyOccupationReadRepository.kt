package com.briolink.searchservice.common.jpa.read.repository

import com.briolink.searchservice.common.jpa.read.entity.CompanyOccupationReadEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface CompanyOccupationReadRepository : JpaRepository<CompanyOccupationReadEntity, UUID> {
    @Query("SELECT c FROM CompanyOccupationReadEntity c WHERE (:query is null or function('fts_partial', c.name, :query) = true)")
    fun getAutoCompleteByName(@Param("query") query: String?, pageable: Pageable = Pageable.ofSize(10)): List<CompanyOccupationReadEntity>
}