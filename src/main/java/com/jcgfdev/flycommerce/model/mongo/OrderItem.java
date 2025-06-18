package com.jcgfdev.flycommerce.model.mongo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    private String productId;
    private String name;
    private int quantity;
    private double price;
}
