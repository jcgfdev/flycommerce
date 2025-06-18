package com.jcgfdev.flycommerce.model.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id; // Mongo id en String

    private UUID customerId; // O String, depende de tu diseño
    private Instant createdAt;
    private String status; // Podría ser un enum, por simplicidad lo dejamos String
    private List<OrderItem> items;
}
