package com.briolink.searchservice.common.jpa.read.entity

import com.briolink.searchservice.common.jpa.enumeration.CompanyRoleTypeEnum
import org.hibernate.annotations.Type
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "company_role", schema = "read")
@Entity
class CompanyRoleReadEntity(
    @Id
    @Type(type = "pg-uuid")
    @Column(name = "id", nullable = false)
    val id: UUID,

    @Column(name = "name", nullable = false, length = 255)
    var name: String,

) : BaseReadEntity() {
    @Column(name = "type")
    private var _type: Int = 0

    var type: CompanyRoleTypeEnum
        get() = CompanyRoleTypeEnum.fromInt(_type)
        set(value) {
            _type = value.value
        }
}
