package org.bank.controller

import io.swagger.annotations.ApiOperation
import org.bank.dto.AccountDto
import org.bank.service.AccountServiceImpl
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@RestController
@RequestMapping("/account")
@Validated
open class AccountController(
    private val accountService: AccountServiceImpl,
) {

    @GetMapping("/details")
    open fun getDetails(@RequestParam("id") id: Long): AccountDto {
        return accountService.getAccountDetails(id)
    }

    @PostMapping("/transfer")
    @ApiOperation("Sample docs")
    open fun transferMoney(
        @RequestParam("fromId") fromId: Long,
        @RequestParam("toId") toId: Long,
        @Min(1, message = "Amount must be at least 1")
        @Max(1_000_000, message = "Amount must be no more than 1,000,000")
        @RequestParam("amount") amount: Long,
    ) {
        accountService.transferMoney(fromId, toId, amount)
    }
}
