package org.bank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@SpringBootApplication
open class BankApplication

fun main(args: Array<String>) {
    runApplication<BankApplication>(*args)
}
