package com.dafa.order.vo;

import com.dafa.order.model.Order;

import lombok.Data;

@Data
public class ResponseTemplate {
    Order order;
    Produk produk;
}
