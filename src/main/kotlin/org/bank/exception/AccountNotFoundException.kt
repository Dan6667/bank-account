package org.bank.exception

class AccountNotFoundException(
    id: Long
) : RuntimeException() {
    override val message = "Account with id: $id does not exist"
}
