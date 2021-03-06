package com.briolink.searchservice.api.service.companyservice

import com.blazebit.persistence.CriteriaBuilderFactory
import com.blazebit.persistence.PagedList
import com.blazebit.persistence.ParameterHolder
import com.blazebit.persistence.WhereBuilder
import com.briolink.lib.location.enumeration.TypeLocationEnum
import com.briolink.searchservice.api.dto.SortDirectionEnum
import com.briolink.searchservice.api.service.companyservice.dto.CompanyServiceFiltersDto
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
        filters: CompanyServiceFiltersDto,
        sort: CompanyServiceSortDto? = null,
        offset: Int = 0,
        limit: Int = 10,
    ): PagedList<CompanyServiceReadEntity> {
        val cbf = criteriaBuilderFactory.create(entityManager, CompanyServiceReadEntity::class.java)
        val cb = cbf.from(CompanyServiceReadEntity::class.java)

        setFilters(cb, filters)

        if (sort != null)
            cb.orderBy(sort.key.field, sort.direction == SortDirectionEnum.ASC)

        return cb.orderByDesc("id").page(offset, limit).resultList
    }

    private fun <T> setFilters(
        cb: T,
        filters: CompanyServiceFiltersDto
    ): T where T : WhereBuilder<T>, T : ParameterHolder<T> {
        with(filters) {
            if (!searchText.isNullOrBlank()) cb.whereExpression("function('fts_partial', _keywordsSearch, :searchText) = true")
                .setParameter("searchText", searchText)

            if (!companyIndustryIds.isNullOrEmpty()) cb.where("industryId").`in`(companyIndustryIds)
            if (!companyIds.isNullOrEmpty()) cb.where("companyId").`in`(companyIds)
            if (!serviceNames.isNullOrEmpty()) cb.where("name").`in`(serviceNames)
            if (priceMax != null || priceMin != null) {
                if (priceMax == null)
                    cb.where("price").ge(priceMin)
                else if (priceMin == null)
                    cb.where("price").le(priceMax)
                else cb.whereExpression("price BETWEEN :min AND :max")
                    .setParameter("min", priceMin)
                    .setParameter("max", priceMax)
            }
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
