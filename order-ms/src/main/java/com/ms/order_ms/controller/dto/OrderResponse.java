package com.ms.order_ms.controller.dto;

import com.ms.order_ms.entities.Order;

import java.math.BigDecimal;

public record OrderResponse(
        Long orderId,
        Long customerId,
        BigDecimal total

) {
    public static OrderResponse fromEntity(Order entity){
        return new OrderResponse(entity.getOrderId(), entity.getCustomerId(), entity.getTotal());
    }
}
