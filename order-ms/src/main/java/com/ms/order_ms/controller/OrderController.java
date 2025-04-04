package com.ms.order_ms.controller;

import com.ms.order_ms.controller.dto.ApiResponse;
import com.ms.order_ms.controller.dto.OrderResponse;
import com.ms.order_ms.controller.dto.PaginationResponse;
import com.ms.order_ms.services.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/customers")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable Long customerId,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        var pageResponse = service.findAllByCustomerId(customerId, PageRequest.of(page,pageSize));
        var totalOnOrders = service.findTotalOnOrdersByCustomerId(customerId);

        return ResponseEntity.ok(new ApiResponse<>(
                Map.of("totalOnOrders", totalOnOrders),
                pageResponse.getContent(),
                PaginationResponse.fromPage(pageResponse)

        ));
    }
}
