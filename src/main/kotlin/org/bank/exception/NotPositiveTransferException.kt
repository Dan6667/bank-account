package org.bank.exception

class NotPositiveTransferException : RuntimeException() {
    override val message = "Transfer amount must be at least 1"
}
