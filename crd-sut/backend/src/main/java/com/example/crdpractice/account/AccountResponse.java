package com.example.crdpractice.account;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

record AccountResponse(
        UUID id,
        String accountCode,
        String accountName,
        String accountType,
        String baseCurrency,
        String status,
        BigDecimal cashBalance,
        Instant createdAt,
        Instant updatedAt,
        Long version) {

    static AccountResponse from(AccountEntity account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountCode(),
                account.getAccountName(),
                account.getAccountType(),
                account.getBaseCurrency(),
                account.getStatus(),
                account.getCashBalance(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                account.getVersion());
    }
}
