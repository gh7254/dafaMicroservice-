package com.dafa.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dafa.order.model.Order;
import com.dafa.order.repository.OrderRepository;
import com.dafa.order.vo.Produk;
import com.dafa.order.vo.ResponseTemplate;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // GET ALL
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // CREATE ORDER
    public Order createOrder(Order order) {

        if (order.getStatus() == null) {
            order.setStatus("Baru");
        }

        Order savedOrder = orderRepository.save(order);

        rabbitTemplate.convertAndSend("myQueue", savedOrder.toString());

        System.out.println("Order sent to RabbitMQ");

        return savedOrder;
    }

    // UPDATE
    @Transactional
    public void update(Long id, Integer jumlah, String tanggal, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No Order"));

        if (jumlah != null) {
            order.setJumlah(jumlah);
        }

        if (tanggal != null && tanggal.length() > 0 &&
                !Objects.equals(order.getTanggal(), tanggal)) {
            order.setTanggal(tanggal);
        }

        if (status != null && status.length() > 0) {
            order.setStatus(status);
        }
    }

    // GET BY ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // GET ORDER + PRODUK
    public List<ResponseTemplate> getOrderWithProdukById(Long id) {

        List<ResponseTemplate> responseList = new ArrayList<>();

        Order order = getOrderById(id);

        ServiceInstance serviceInstance = discoveryClient.getInstances("PRODUK").get(0);

        Produk produk = restTemplate.getForObject(
                serviceInstance.getUri() + "/api/produk/" + order.getProdukId(),
                Produk.class);

        ResponseTemplate vo = new ResponseTemplate();
        vo.setOrder(order);
        vo.setProduk(produk);

        responseList.add(vo);

        return responseList;
    }

    // DELETE
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}