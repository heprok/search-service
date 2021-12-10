package com.briolink.searchservice.common.jpa.read.repository

import com.briolink.searchservice.common.jpa.projection.IdNameProjection
import com.briolink.searchservice.common.jpa.read.entity.CompanyReadEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface CompanyReadRepository : JpaRepository<CompanyReadEntity, UUID> {
    @Query("SELECT c.id, c.name FROM CompanyReadEntity c WHERE (:query is null or function('fts_partial', c.name, :query) = true)")
    fun getAutoCompleteByName(@Param("query") query: String?, pageable: Pageable = Pageable.ofSize(10)): List<IdNameProjection>
}
