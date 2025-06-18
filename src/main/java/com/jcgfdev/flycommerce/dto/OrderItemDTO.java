package com.jcgfdev.flycommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {
    private String productId;
    private String name;
    private int quantity;
    private double price;
}
