package com.briolink.searchservice.api.service.company

import com.blazebit.persistence.CriteriaBuilderFactory
import com.blazebit.persistence.PagedList
import com.briolink.searchservice.api.dto.SortDirectionEnum
import com.briolink.searchservice.api.service.company.dto.CompanySortDto
import com.briolink.searchservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.searchservice.common.jpa.read.repository.CompanyReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
@Transactional
class CompanyService(
    private val entityManager: EntityManager,
    private val criteriaBuilderFactory: CriteriaBuilderFactory,
    private val companyReadRepository: CompanyReadRepository,
) {
    fun getList(
        sort: CompanySortDto? = null,
        limit: Int = 10,
        offset: Int = 0,
    ): PagedList<CompanyReadEntity> {
        val cbf = criteriaBuilderFactory.create(entityManager, CompanyReadEntity::class.java)
        val cb = cbf.from(CompanyReadEntity::class.java)

        if (sort != null)
            cb.orderBy(sort.key.field, sort.direction == SortDirectionEnum.ASC)

        return cb.orderByDesc("id").page(offset, limit).resultList
    }
}
