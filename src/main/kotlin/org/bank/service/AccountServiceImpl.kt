package org.bank.service

import org.bank.dto.AccountDto
import org.bank.exception.AccountNotFoundException
import org.bank.repository.AccountRepository
import org.bank.validation.AccountTransferValidator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class AccountServiceImpl(
    private val accountRepository: AccountRepository,
    private val accountTransferValidator: AccountTransferValidator,
) : AccountService {

    @Transactional
    override fun transferMoney(
        fromId: Long,
        toId: Long,
        amount: Long,
    ) {
        val fromAccount = accountRepository.findByIdOrNull(fromId) ?: throw AccountNotFoundException(fromId)
        val toAccount = accountRepository.findByIdOrNull(toId) ?: throw AccountNotFoundException(toId)

        accountTransferValidator.validateTransfer(amount, fromAccount.money!!, fromAccount.id!!)

        fromAccount.money = fromAccount.money!! - amount
        toAccount.money = toAccount.money!! + amount
        accountRepository.saveAll(listOf(fromAccount, toAccount))
    }

    override fun getAccountDetails(accountId: Long): AccountDto {
        val entity = accountRepository.findByIdOrNull(accountId) ?: throw AccountNotFoundException(accountId)

        return AccountDto.fromEntity(entity)
    }
}
