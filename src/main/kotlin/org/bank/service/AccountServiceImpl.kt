package org.bank.service

import org.bank.dto.AccountDto
import org.bank.exception.AccountNotFoundException
import org.bank.exception.TransferNotEnoughMoneyException
import org.bank.repository.AccountRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class AccountServiceImpl(
    private val accountRepository: AccountRepository,
) : AccountService {

    @Transactional
    override fun transferMoney(
        fromId: Long,
        toId: Long,
        amount: Long,
    ) {
        val fromAccount = accountRepository.findByIdOrNull(fromId) ?: throw AccountNotFoundException(fromId)
        val toAccount = accountRepository.findByIdOrNull(toId) ?: throw AccountNotFoundException(toId)

        if (fromAccount.money!! < amount) {
            throw TransferNotEnoughMoneyException(
                id = fromId,
                requiredMoney = amount,
                actualMoney = fromAccount.money!!,
            )
        }
        fromAccount.money = fromAccount.money!! - amount
        toAccount.money = toAccount.money!! + amount
        accountRepository.saveAll(listOf(fromAccount, toAccount))
    }

    override fun getAccountDetails(accountId: Long): AccountDto {
        val entity = accountRepository.findByIdOrNull(accountId) ?: throw AccountNotFoundException(accountId)

        return AccountDto.fromEntity(entity)
    }
}
