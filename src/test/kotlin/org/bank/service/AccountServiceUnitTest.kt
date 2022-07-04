package org.bank.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.bank.entity.AccountEntity
import org.bank.exception.AccountNotFoundException
import org.bank.exception.NotPositiveTransferException
import org.bank.exception.TransferNotEnoughMoneyException
import org.bank.repository.AccountRepository
import org.bank.validation.AccountTransferValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class AccountServiceUnitTest {

    private val accountRepository = mockk<AccountRepository>()
    private val accountTransferValidator = AccountTransferValidator()
    private val accountService = AccountServiceImpl(accountRepository, accountTransferValidator)

    private lateinit var accountFrom: AccountEntity
    private lateinit var accountTo: AccountEntity

    @BeforeEach
    fun init() {
        accountFrom = AccountEntity()
        accountFrom.id = 1
        accountFrom.money = 10
        accountFrom.holderName = "accountFrom"

        accountTo = AccountEntity()
        accountTo.id = 2
        accountTo.money = 0
        accountTo.holderName = "accountTo"
    }

    @Test
    fun test_transferMoney() {
        every { accountRepository.findByIdOrNull(accountFrom.id!!) } answers { accountFrom }
        every { accountRepository.findByIdOrNull(accountTo.id!!) } answers { accountTo }
        every { accountRepository.saveAll(listOf(accountFrom, accountTo)) } answers { listOf(accountFrom, accountTo) }

        accountService.transferMoney(
            accountFrom.id!!,
            accountTo.id!!,
            amount = 10
        )

        accountFrom.money shouldBe 0
        accountTo.money shouldBe 10
    }

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
                amount = 10
            )
        }
    }

    @Test
    fun test_transferMoney_whenToAccountDontExist_thenThrow() {
        every { accountRepository.findByIdOrNull(accountFrom.id!!) } answers { accountFrom }
        every { accountRepository.findByIdOrNull(accountTo.id!!) } answers { null }

        shouldThrow<AccountNotFoundException> {
            accountService.transferMoney(
                fromId = accountFrom.id!!,
                toId = 2,
                amount = 10
            )
        }
    }

    @Test
    fun test_whenNotEnoughMoney_thenThrow() {
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

    @Test
    fun test_whenNotMoneyNonPositive_thenThrow() {
        every { accountRepository.findByIdOrNull(accountFrom.id!!) } answers { accountFrom }
        every { accountRepository.findByIdOrNull(accountTo.id!!) } answers { accountTo }

        shouldThrow<NotPositiveTransferException> {
            accountService.transferMoney(
                accountFrom.id!!,
                accountTo.id!!,
                amount = 0
            )
        }
    }
}
