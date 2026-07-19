package com.example.crdpractice.order;

import com.example.crdpractice.account.AccountEntity;
import com.example.crdpractice.account.AccountRepository;
import com.example.crdpractice.audit.AuditService;
import com.example.crdpractice.common.BusinessException;
import com.example.crdpractice.security.SecurityEntity;
import com.example.crdpractice.security.SecurityRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class OrderService {

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final SecurityRepository securityRepository;
    private final AuditService auditService;

    OrderService(OrderRepository orderRepository, AccountRepository accountRepository, SecurityRepository securityRepository, AuditService auditService) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
        this.securityRepository = securityRepository;
        this.auditService = auditService;
    }

    @Transactional
    OrderResponse createOrder(CreateOrderRequest request, String username, String correlationId) {
        if (orderRepository.existsByExternalOrderId(request.externalOrderId())) {
            throw new BusinessException(HttpStatus.CONFLICT, "DUPLICATE_EXTERNAL_ORDER_ID", "External order ID already exists.");
        }

        AccountEntity account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "ACCOUNT_NOT_FOUND", "Account was not found."));
        if (!"ACTIVE".equals(account.getStatus())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "INACTIVE_ACCOUNT", "Account must be active.");
        }

        SecurityEntity security = securityRepository.findById(request.securityId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "SECURITY_NOT_FOUND", "Security was not found."));
        if (!security.isActive()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "INACTIVE_SECURITY", "Security must be active.");
        }

        if (request.orderType() == OrderType.LIMIT && request.limitPrice() == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "INVALID_ORDER_PRICE", "Limit price is required for limit orders.");
        }
        if (request.orderType() == OrderType.MARKET && request.limitPrice() != null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "INVALID_ORDER_PRICE", "Market orders must not include a limit price.");
        }

        OrderEntity order = OrderEntity.create(
                request.externalOrderId(),
                request.accountId(),
                request.securityId(),
                request.side(),
                request.orderType(),
                request.quantity(),
                request.limitPrice(),
                username);
        OrderEntity saved = orderRepository.save(order);

        auditService.record(
                "ORDER",
                saved.getId(),
                "ORDER_CREATED",
                null,
                "externalOrderId=" + saved.getExternalOrderId() + ",status=" + saved.getStatus(),
                username,
                correlationId);

        return OrderResponse.from(saved);
    }

    @Transactional(readOnly = true)
    List<OrderResponse> listOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .toList();
    }
}
