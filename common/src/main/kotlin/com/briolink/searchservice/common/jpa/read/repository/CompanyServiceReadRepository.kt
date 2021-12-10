package com.briolink.searchservice.common.jpa.read.repository

import com.briolink.searchservice.common.jpa.projection.IdNameProjection
import com.briolink.searchservice.common.jpa.read.entity.CompanyServiceReadEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface CompanyServiceReadRepository : JpaRepository<CompanyServiceReadEntity, UUID> {
    @Query("SELECT cs.id, cs.name FROM CompanyServiceReadEntity cs WHERE (:query is null or function('fts_partial', cs.name, :query) = true)") // ktlint-disable max-line-length
    fun getAutoCompleteByName(@Param("query") query: String?, pageable: Pageable = Pageable.ofSize(10)): List<IdNameProjection>
}
