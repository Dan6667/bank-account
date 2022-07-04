package org.bank.service

import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import org.bank.entity.AccountEntity
import org.bank.exception.AccountNotFoundException
import org.bank.exception.TransferNotEnoughMoneyException
import org.bank.repository.AccountRepository
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class AccountServiceUnitTest {

    private val accountRepository = mockk<AccountRepository>()

    private val accountService = AccountServiceImpl(accountRepository)

    @Test
    fun test_getAccountDetails_whenAccountDontExist_thenThrow() {
        every { accountRepository.findByIdOrNull(any()) } answers { null }

        shouldThrow<AccountNotFoundException> {
            accountService.getAccountDetails(1)
        }
    }

    @Test
    fun test_transferMoney_whenFromAccountDontExist_thenThrow() {
        every { accountRepository.findByIdOrNull(any()) } answers { null }

        shouldThrow<AccountNotFoundException> {
            accountService.transferMoney(
                fromId = 1,
                toId = 2,
                amount = 100
            )
        }
    }

    @Test
    fun test_transferMoney_whenToAccountDontExist_thenThrow() {
        val accountFrom = AccountEntity()
        accountFrom.id = 1
        accountFrom.money = 10
        accountFrom.holderName = "accountFrom"
        every { accountRepository.findByIdOrNull(accountFrom.id!!) } answers { accountFrom }

        shouldThrow<AccountNotFoundException> {
            accountService.transferMoney(
                fromId = accountFrom.id!!,
                toId = 2,
                amount = 100
            )
        }
    }

    @Test
    fun test_whenNotEnoughMoney_thenThrow() {
        val accountFrom = AccountEntity()
        accountFrom.id = 1
        accountFrom.money = 10
        accountFrom.holderName = "accountFrom"

        val accountTo = AccountEntity()
        accountTo.id = 2
        accountTo.money = 0
        accountTo.holderName = "accountTo"

        every { accountRepository.findByIdOrNull(accountFrom.id!!) } answers { accountFrom }
        every { accountRepository.findByIdOrNull(accountTo.id!!) } answers { accountTo }

        shouldThrow<TransferNotEnoughMoneyException> {
            accountService.transferMoney(
                accountFrom.id!!,
                accountTo.id!!,
                amount = 11
            )
        }
    }
}
