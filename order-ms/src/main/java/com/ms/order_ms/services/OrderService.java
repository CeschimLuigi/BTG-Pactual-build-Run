package com.ms.order_ms.services;

import com.ms.order_ms.controller.dto.OrderResponse;
import com.ms.order_ms.entities.Order;
import com.ms.order_ms.entities.OrderItem;
import com.ms.order_ms.listener.dto.OrderCreatedEvent;
import com.ms.order_ms.repository.OrderRepository;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final MongoTemplate template;


    public OrderService(OrderRepository repository, MongoTemplate template) {
        this.repository = repository;
        this.template = template;
    }

    public void save(OrderCreatedEvent event){

        var entity = new Order();
        entity.setOrderId(event.codigoPedido());
        entity.setCustomerId(event.codigoCliente());

        entity.setItens(getOrderItens(event));
        entity.setTotal(setTotalPrice(event));

        repository.save(entity);

    }

    public Page<OrderResponse>findAllByCustomerId(Long customerId, PageRequest request){
       var orders = repository.findAllByCustomerId(customerId, request);

       return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total")
        );

        var response = template.aggregate(aggregations, "tb_users", Document.class);

        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }



    private List<OrderItem> getOrderItens(OrderCreatedEvent event){
        return event.itens().stream()
                .map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco()))
                .toList();
    }

    private BigDecimal setTotalPrice(OrderCreatedEvent event){
        return event.itens().stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
