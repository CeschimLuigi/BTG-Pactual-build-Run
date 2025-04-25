# 📦 Order Service

Documentação do projeto de gerenciamento de pedidos utilizando **MongoDB** e **RabbitMQ**, com representação de modelo via **Mermaid** e ambiente configurado via **Docker Compose**.

---

## 📄 Visão Geral

Este projeto contém:

- Modelos de dados `Order` e `OrderItem` com persistência no MongoDB
- Comunicação assíncrona via RabbitMQ (configurável para eventos)
- Ambiente de desenvolvimento com Docker Compose

---


## 🧾 Diagrama de Classes

```mermaid
classDiagram

class Order {
    +Long orderId
    +Long customerId
    +BigDecimal total
    +List~OrderItem~ itens
}

class OrderItem {
    +String product
    +Integer quantity
    +BigDecimal price
}

Order "1" --> "0..*" OrderItem : contém
```

## 🔁 Fluxo de Processamento de Mensagens

```mermaid
flowchart TD
    A[RabbitMQ: Mensagens Pendentes] --> B[Microserviço: Consome Mensagem]
    B --> C[Processamento da Mensagem]
    C --> D[Salva no MongoDB]
    D --> E[Confirmação de Sucesso]
    E --> F[Publica Confirmação no RabbitMQ]
    F --> G[Finaliza Processamento]
```







