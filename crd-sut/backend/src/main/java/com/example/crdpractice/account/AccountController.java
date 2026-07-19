package com.example.crdpractice.account;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
class AccountController {

    private final AccountRepository accountRepository;

    AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping
    List<AccountResponse> listAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    ResponseEntity<AccountResponse> getAccount(@PathVariable UUID id) {
        return accountRepository.findById(id)
                .map(AccountResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
