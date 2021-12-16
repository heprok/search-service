package com.briolink.searchservice.api.service.companyservice

import com.blazebit.persistence.CriteriaBuilderFactory
import com.blazebit.persistence.PagedList
import com.briolink.searchservice.api.dto.SortDirectionEnum
import com.briolink.searchservice.api.service.companyservice.dto.CompanyServiceSortDto
import com.briolink.searchservice.common.jpa.read.entity.CompanyServiceReadEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
@Transactional
class CompanyServiceService(
    private val entityManager: EntityManager,
    private val criteriaBuilderFactory: CriteriaBuilderFactory
) {
    fun getList(
        sort: CompanyServiceSortDto? = null,
        offset: Int = 0,
        limit: Int = 10,
    ): PagedList<CompanyServiceReadEntity> {
        val cbf = criteriaBuilderFactory.create(entityManager, CompanyServiceReadEntity::class.java)
        val cb = cbf.from(CompanyServiceReadEntity::class.java)

        if (sort != null)
            cb.orderBy(sort.key.field, sort.direction == SortDirectionEnum.ASC)

        return cb.orderByDesc("id").page(offset, limit).resultList
    }
}
