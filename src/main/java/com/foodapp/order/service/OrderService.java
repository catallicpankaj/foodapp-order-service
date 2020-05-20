package com.foodapp.order.service;

import java.util.List;

import com.foodapp.order.dto.Order;

public interface OrderService {
	
	public void createOrder(Order order);
	
	
	public List<Order> getAllOrders();

	public List<Order> getAllOrdersForCustomerId(String customerId, String orderStatus);

	public Order getOrder(String orderId);
	
	
}
