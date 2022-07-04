package org.bank.service

import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.bank.entity.AccountEntity
import org.bank.executeConcurrentlyAndWaitForCompletion
import org.bank.executeInParallelAndWaitForCompletion
import org.bank.repository.AccountRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AccountServiceConcurrencyTest {

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var accountRepository: AccountRepository

    private lateinit var accountFrom: AccountEntity

    private lateinit var accountTo: AccountEntity

    @BeforeEach
    fun init() {
        accountFrom = AccountEntity()
        accountFrom.money = 100
        accountFrom.holderName = "holder1"
        accountFrom = accountRepository.save(accountFrom)

        accountTo = AccountEntity()
        accountTo.money = 0
        accountTo.holderName = "holder2"
        accountTo = accountRepository.save(accountTo)
    }

    @RepeatedTest(100)
    fun testTwoConcurrentTransactions() {
        runBlocking {
            executeInParallelAndWaitForCompletion(
                listOf(
                    { accountService.transferMoney(accountFrom.id!!, accountTo.id!!, 10) },
                    { accountService.transferMoney(accountFrom.id!!, accountTo.id!!, 50) },
                )
            )
        }

        val firstHolderMoney = accountService.getAccountDetails(accountFrom.id!!).money
        val secondHolderMoney = accountService.getAccountDetails(accountTo.id!!).money

        firstHolderMoney shouldBeIn arrayOf(90, 50, 40)
        when (firstHolderMoney) {
            90L -> secondHolderMoney shouldBe 10
            50L -> secondHolderMoney shouldBe 50
            40L -> secondHolderMoney shouldBe 60
        }
    }

    @Test
    fun testManyConcurrentTransactions() {
        // executing many concurrent transactions to check for a serialization anomaly
        runBlocking {
            executeConcurrentlyAndWaitForCompletion(120) {
                accountService.transferMoney(accountFrom.id!!, accountTo.id!!, 1)
            }
        }

        val firstHolderMoney = accountService.getAccountDetails(accountFrom.id!!).money
        val secondHolderMoney = accountService.getAccountDetails(accountTo.id!!).money
        // some transactions won't be executed due to OptimisticLockingException
        // we want to check our DB state is still consistent
        firstHolderMoney + secondHolderMoney shouldBe 100
    }
}
