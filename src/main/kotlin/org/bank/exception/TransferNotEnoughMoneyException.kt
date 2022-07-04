package org.bank.exception

class TransferNotEnoughMoneyException(
    id: Long,
    requiredMoney: Long,
    actualMoney: Long
) : RuntimeException() {
    override val message = "Transfer requires $requiredMoney, but account with id $id has only $actualMoney"
}
