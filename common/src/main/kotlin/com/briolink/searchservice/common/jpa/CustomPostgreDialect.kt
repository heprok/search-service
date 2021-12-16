package com.briolink.searchservice.common.jpa

import com.vladmihalcea.hibernate.type.array.StringArrayType
import org.hibernate.dialect.PostgreSQL10Dialect

class CustomPostgreDialect : PostgreSQL10Dialect() {
    init {
        this.registerHibernateType(2003, StringArrayType::class.java.name)
    }
}
