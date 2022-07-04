package org.bank.controller

import org.bank.exception.AccountNotFoundException
import org.bank.exception.TransferNotEnoughMoneyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class GeneralExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(exception: ConstraintViolationException, webRequest: ServletWebRequest) =
        ResponseEntity(
            exception.message,
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFound(exception: AccountNotFoundException, webRequest: ServletWebRequest) = ResponseEntity(
        exception.message,
        HttpStatus.BAD_REQUEST,
    )

    @ExceptionHandler(TransferNotEnoughMoneyException::class)
    fun handleTransferEnoughMoney(exception: TransferNotEnoughMoneyException, webRequest: ServletWebRequest) =
        ResponseEntity(
            exception.message,
            HttpStatus.BAD_REQUEST,
        )
}
