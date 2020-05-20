package com.foodapp.order.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.order.dto.Order;
import com.foodapp.order.service.OrderService;

@RestController
public class OrderController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	OrderService orderService;
	
	@PostMapping("/v1/order")
	public ResponseEntity<Void> createOrder(@RequestBody Order order) {
		LOGGER.info("Begin order creation");
		orderService.createOrder(order);
		LOGGER.info("Order created successfully");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@GetMapping("/v1/order")
	public ResponseEntity<List<Order>> getOrders() {
		LOGGER.info("Retrieving all order data");
		return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
	}
	
	@GetMapping("/v1/customer/{customerId}/order")
	public ResponseEntity<List<Order>> getOrdersForCustomerId(@PathVariable String customerId, @RequestParam String orderStatus) {
		LOGGER.info("Retrieving all order data");
		return new ResponseEntity<>(orderService.getAllOrdersForCustomerId(customerId, orderStatus), HttpStatus.OK);
	}
	
	@GetMapping("/v1/order/{orderId}")
	public ResponseEntity<Order> getOrderDetails(@PathVariable String orderId){
		LOGGER.info("Retrieving order data for Id "+orderId);
		return new ResponseEntity<>(orderService.getOrder(orderId), HttpStatus.OK);
	}
}
