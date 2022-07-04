package org.bank.entity

import org.hibernate.annotations.CreationTimestamp
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version

@Entity
@Table(name = "account")
class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @Column(nullable = false)
    var money: Long? = null

    @Column(name = "holder_name", nullable = false)
    var holderName: String? = null

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant? = null

    @Version
    var version: Int? = null
}
