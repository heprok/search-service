package com.briolink.searchservice.api.service.company

import com.blazebit.persistence.CriteriaBuilderFactory
import com.blazebit.persistence.PagedList
import com.blazebit.persistence.ParameterHolder
import com.blazebit.persistence.WhereBuilder
import com.briolink.lib.location.enumeration.TypeLocationEnum
import com.briolink.searchservice.api.dto.SortDirectionEnum
import com.briolink.searchservice.api.service.company.dto.CompanyFiltersDto
import com.briolink.searchservice.api.service.company.dto.CompanySortDto
import com.briolink.searchservice.common.jpa.read.entity.CompanyReadEntity
import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import org.hibernate.jpa.TypedParameterValue
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
@Transactional
class CompanyService(
    private val entityManager: EntityManager,
    private val criteriaBuilderFactory: CriteriaBuilderFactory,
) {
    fun getList(
        filters: CompanyFiltersDto,
        sort: CompanySortDto? = null,
        limit: Int = 10,
        offset: Int = 0,
    ): PagedList<CompanyReadEntity> {
        val cbf = criteriaBuilderFactory.create(entityManager, CompanyReadEntity::class.java)
        val cb = cbf.from(CompanyReadEntity::class.java)

        setFilters(cb, filters)

        if (sort != null)
            cb.orderBy(sort.key.field, sort.direction == SortDirectionEnum.ASC)

        return cb.orderByDesc("id").page(offset, limit).resultList
    }

    private fun <T> setFilters(
        cb: T,
        filters: CompanyFiltersDto
    ): T where T : WhereBuilder<T>, T : ParameterHolder<T> {
        with(filters) {
            if (!companyRoleIds.isNullOrEmpty()) {
                cb
                    .whereExpression("array_contains(companyRoleIds, :companyRolesIds) = true")
                    .setParameter(
                        "companyRolesIds",
                        TypedParameterValue(UUIDArrayType.INSTANCE, companyRoleIds.toTypedArray())
                    )
            }
            if (!searchText.isNullOrBlank()) cb.whereExpression("function('fts_partial', _keywordsSearch, :searchText) = true")
                .setParameter("searchText", searchText)
            if (!companyIndustryIds.isNullOrEmpty()) cb.where("industryId").`in`(companyIndustryIds)
            if (!companyOccupationIds.isNullOrEmpty()) cb.where("occupationId").`in`(companyOccupationIds)
            if (!locationIds.isNullOrEmpty()) {
                val countryIds = locationIds.filter { it.type == TypeLocationEnum.Country }.map { it.id }
                val stateIds = locationIds.filter { it.type == TypeLocationEnum.State }.map { it.id }
                val cityIds = locationIds.filter { it.type == TypeLocationEnum.City }.map { it.id }

                cb.whereOr()
                    .where("countryId").`in`(countryIds)
                    .where("stateId").`in`(stateIds)
                    .where("cityId").`in`(cityIds)
                    .endOr()
            }
        }

        return cb
    }
}
