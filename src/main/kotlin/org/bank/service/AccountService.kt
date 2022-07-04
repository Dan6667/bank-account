package org.bank.service

import org.bank.dto.AccountDto

interface AccountService {
    fun transferMoney(
        fromId: Long,
        toId: Long,
        amount: Long,
    )
    fun getAccountDetails(accountId: Long): AccountDto
}
