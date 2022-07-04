package org.bank.validation

import org.bank.exception.NotPositiveTransferException
import org.bank.exception.TransferNotEnoughMoneyException
import org.springframework.stereotype.Component

@Component
class AccountTransferValidator {

    fun validateTransfer(transferAmount: Long, accountAmount: Long, accountId: Long) {
        if (transferAmount < 1) {
            throw NotPositiveTransferException()
        }

        if (transferAmount > accountAmount) {
            throw TransferNotEnoughMoneyException(
                id = accountId,
                requiredMoney = transferAmount,
                actualMoney = accountAmount,
            )
        }
    }
}
