package org.bank.dto

import org.bank.entity.AccountEntity
import java.time.Instant

class AccountDto(
    val id: Long,
    val money: Long,
    val holderName: String,
    val createdAt: Instant,
) {
    companion object {
        fun fromEntity(entity: AccountEntity) = with(entity) {
            AccountDto(
                requireNotNull(id),
                requireNotNull(money),
                requireNotNull(holderName),
                requireNotNull(createdAt),
            )
        }
    }
}