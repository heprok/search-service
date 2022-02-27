package com.briolink.searchservice.api.graphql

import com.briolink.searchservice.api.types.Company
import com.briolink.searchservice.api.types.CompanyCard
import com.briolink.searchservice.api.types.CompanyService
import com.briolink.searchservice.api.types.CompanyServiceCard
import com.briolink.searchservice.api.types.Image
import com.briolink.searchservice.api.types.PlaceOfWork
import com.briolink.searchservice.api.types.User
import com.briolink.searchservice.api.types.UserCard
import com.briolink.searchservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.searchservice.common.jpa.read.entity.CompanyServiceReadEntity
import com.briolink.searchservice.common.jpa.read.entity.UserReadEntity

// ---------------------------User search---------------------------
fun User.Companion.fromEntity(entity: UserReadEntity.User) = User(
    id = entity.id.toString(),
    firstName = entity.firstName,
    lastName = entity.lastName,
    slug = entity.slug,
    image = entity.image?.let { Image(it) },
    description = entity.description,
    location = entity.locationInfo?.toString()
)

fun UserCard.Companion.fromEntity(entity: UserReadEntity) = UserCard(
    user = User.fromEntity(entity.data.user),
    currentPlaceWork = entity.data.currentPlaceOfWorkCompany?.let { PlaceOfWork.fromEntity(it) },
    numberOfVerification = entity.numberOfVerification
)

fun PlaceOfWork.Companion.fromEntity(entity: UserReadEntity.PlaceOfWork) = PlaceOfWork(
    company = Company.fromEntity(entity),
    jobPosition = entity.jobPositionTitle
)

fun Company.Companion.fromEntity(entity: UserReadEntity.PlaceOfWork) = Company(
    id = entity.companyId.toString(),
    name = entity.companyName,
    slug = entity.slug,
    logo = entity.logo?.let { Image(it) }
)

// ---------------------------Company search---------------------------
fun CompanyCard.Companion.fromEntity(entity: CompanyReadEntity) = CompanyCard(
    company = Company.fromEntity(entity),
    roles = entity.data.companyRoles.map { it.name },
    numberOfVerification = entity.numberOfVerification
)

fun Company.Companion.fromEntity(entity: CompanyReadEntity) = Company(
    id = entity.id.toString(),
    name = entity.name,
    slug = entity.data.slug,
    occupation = entity.data.occupationName,
    logo = entity.data.logo?.let { Image(it) },
    location = entity.data.location?.toString(),
    description = entity.data.shortDescription,
)

// ---------------------------CompanyServices search---------------------------
fun CompanyServiceCard.Companion.fromEntity(entity: CompanyServiceReadEntity) = CompanyServiceCard(
    companyService = CompanyService(
        id = entity.id.toString(),
        name = entity.name,
        slug = entity.data.slug,
        image = entity.data.image?.let { Image(it) },
        price = entity.price,
        description = entity.data.description,
        company = Company.fromEntity(entity.data.company),
    ),
    numberOfUses = entity.numberOfUses
)

fun Company.Companion.fromEntity(entity: CompanyServiceReadEntity.Company) = Company(
    id = entity.id.toString(),
    name = entity.name,
    slug = entity.slug,
    logo = entity.logo?.let { Image(it) }

)
